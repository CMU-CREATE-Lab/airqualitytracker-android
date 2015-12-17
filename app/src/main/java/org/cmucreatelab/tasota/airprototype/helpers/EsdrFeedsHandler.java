package org.cmucreatelab.tasota.airprototype.helpers;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.classes.Channel;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by mike on 6/29/15.
 */
public class EsdrFeedsHandler {

    private GlobalHandler globalHandler;
    private static EsdrFeedsHandler classInstance;


    // Nobody accesses the constructor
    private EsdrFeedsHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    // Only way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized EsdrFeedsHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new EsdrFeedsHandler(globalHandler);
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
        requestUrl += "?whereJoin=AND&whereOr=productId=11,productId=1";
        // get bounding box
        la1 = latd-Constants.MapGeometry.BOUNDBOX_LAT;
        la2 = latd+Constants.MapGeometry.BOUNDBOX_LONG;
        lo1 = longd-Constants.MapGeometry.BOUNDBOX_LAT;
        lo2 = longd+Constants.MapGeometry.BOUNDBOX_LONG;
        // within bounds, within time, and exposure=outdoor
        requestUrl += "&whereAnd=latitude>="+la1+",latitude<="+la2+",longitude>="+lo1+",longitude<="+lo2+",maxTimeSecs>="+maxTime+",exposure=outdoor";
        // only request from ESDR the fields that we care about
        requestUrl += "&fields=id,name,exposure,isMobile,latitude,longitude,productId,channelBounds";

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    private void requestChannelReading(String authToken, final Feed feed, final Channel channel, final long maxTime) {
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
                    Log.w(Constants.LOG_TAG, "Failed to request Channel Readable for " + channelName);
                    e.printStackTrace();
                }
                if (resultValue != null && resultTime != null) {
                    Log.i(Constants.LOG_TAG, "got value \"" + resultValue + "\" at time " + resultTime + " for Channel " + channelName);
                    if (maxTime <= 0) {
                        feed.setFeedValue(Double.parseDouble(resultValue));
                        feed.setLastTime(Double.parseDouble(resultTime));
                    } else {
                        // TODO there might be a better (more organized) way to verify a channel's maxTime
                        Log.e(Constants.LOG_TAG,"COMPARE maxTime="+maxTime+", resultTime="+resultTime);
                        if (maxTime <= Long.parseLong(resultTime)) {
                            feed.setHasReadableValue(true);
                            feed.setFeedValue(Double.parseDouble(resultValue));
                            feed.setLastTime(Double.parseDouble(resultTime));
                        } else {
                            feed.setHasReadableValue(false);
                            feed.setFeedValue(0);
                            feed.setLastTime(Double.parseDouble(resultTime));
                            Log.i(Constants.LOG_TAG,"Ignoring channel updated later than maxTime.");
                        }
                    }
                    globalHandler.notifyGlobalDataSetChanged();
                }
            }
        };

        if (authToken.equals("")) {
            globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
        } else {
            globalHandler.httpRequestHandler.sendAuthorizedJsonRequest(authToken, requestMethod, requestUrl, null, response);
        }
    }


    public void requestChannelReading(final Feed feed, final Channel channel) {
        requestChannelReading("", feed, channel, 0);
    }

    public void requestAuthorizedChannelReading(String authToken, final Feed feed, final Channel channel) {
        requestChannelReading(authToken, feed, channel,
                (long) (new Date().getTime() / 1000.0) - Constants.SPECKS_MAX_TIME_RANGE);
    }

}
