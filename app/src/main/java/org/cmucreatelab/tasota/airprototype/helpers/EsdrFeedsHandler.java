package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.classes.Channel;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.json.JSONObject;

/**
 * Created by mike on 6/29/15.
 */
public class EsdrFeedsHandler {

    private Context appContext;
    private HttpRequestHandler httpRequestHandler;
    private static EsdrFeedsHandler classInstance;


    // Nobody accesses the constructor
    private EsdrFeedsHandler(Context ctx, HttpRequestHandler httpRequestHandler) {
        this.appContext = ctx;
        this.httpRequestHandler = httpRequestHandler;
    }


    // Only way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized EsdrFeedsHandler getInstance(Context ctx, HttpRequestHandler httpRequestHandler) {
        if (classInstance == null) {
            classInstance = new EsdrFeedsHandler(ctx,httpRequestHandler);
        }
        return classInstance;
    }


    public void requestFeeds(double latd, double longd, double maxTime, Response.Listener<JSONObject> response) {
        int requestMethod;
        String requestUrl;
        double la1,lo1,la2,lo2;  // given lat, long, create a bounding box and search from that

        requestMethod = Request.Method.GET;
        requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds";
        // only request AirNow (11) or ACHD (1)
        requestUrl += "?whereOr=ProductId=11,ProductId=1";
        // get bounding box
        la1 = latd-Constants.MapGeometry.BOUNDBOX_LAT;
        la2 = latd+Constants.MapGeometry.BOUNDBOX_LONG;
        lo1 = longd-Constants.MapGeometry.BOUNDBOX_LAT;
        lo2 = longd+Constants.MapGeometry.BOUNDBOX_LONG;
        // within bounds, within time, and exposure=outdoor
        requestUrl += "&whereAnd=latitude>="+la1+",latitude<="+la2+",longitude>="+lo1+",longitude<="+lo2+",maxTimeSecs>="+maxTime+",exposure=outdoor";
        // only request from ESDR the fields that we care about
        requestUrl += "&fields=id,name,exposure,isMobile,latitude,longitude,productId,channelBounds";

        httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void requestPrivateFeeds(String authToken, Response.Listener<JSONObject> response) {
        int requestMethod = Request.Method.GET;
        String requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds?where=isPublic=0";
        httpRequestHandler.sendAuthorizedJsonRequest(authToken, requestMethod, requestUrl, null, response);
    }


    public void requestChannelReading(final Feed feed, final Channel channel) {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;
        final String channelName = channel.getName();

        requestMethod = Request.Method.GET;
        requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds/" + channel.getFeed_id() + "/channels/" + channelName + "/most-recent";
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String resultValue = null;
                String resultTime = null;
                try {
                    // NOTE (from Chris)
                    // "don't expect mostRecentDataSample to always exist in the response for every channel,
                    // and don't expect channelBounds.maxTimeSecs to always equal mostRecentDataSample.timeSecs"
                    resultValue = response.getJSONObject("data")
                            .getJSONObject("channels")
                            .getJSONObject(channelName)
                            .getJSONObject("mostRecentDataSample")
                            .getString("value");
                    resultTime = response.getJSONObject("data")
                            .getJSONObject("channels")
                            .getJSONObject(channelName)
                            .getJSONObject("mostRecentDataSample")
                            .getString("timeSecs");
                } catch (Exception e) {
                    Log.w(Constants.LOG_TAG, "Failed to request Channel Reading for " + channelName);
                    e.printStackTrace();
                }
                if (resultValue != null && resultTime != null) {
                    Log.i(Constants.LOG_TAG,"got value \""+resultValue+"\" at time "+resultTime+" for Channel "+channelName);
                    feed.setFeedValue(Double.parseDouble(resultValue));
                    feed.setLastTime(Double.parseDouble(resultTime));
                    GlobalHandler.getInstance(appContext).notifyGlobalDataSetChanged();
                }
            }
        };

        httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }

}
