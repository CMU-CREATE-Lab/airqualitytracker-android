package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import org.cmucreatelab.tasota.airprototype.classes.Channel;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.json.JSONObject;
import java.util.Date;

/**
 * Created by mike on 5/29/15.
 */
public class HttpRequestHandler {

    private Context appContext;
    private RequestQueue queue;
    private static HttpRequestHandler classInstance;


    // Nobody accesses the constructor
    private HttpRequestHandler(Context ctx) {
        this.appContext = ctx;
        this.queue = Volley.newRequestQueue(this.appContext);
    }


    // Only way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized HttpRequestHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new HttpRequestHandler(ctx);
        }
        return classInstance;
    }


    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        JsonObjectRequest jsonRequest;

        jsonRequest = new JsonObjectRequest(requestMethod, requestUrl, requestParams, response, error);
        Log.d(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl);
        this.queue.add(jsonRequest);
    }


    public void requestFeeds(double latd, double longd, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        int requestMethod;
        String requestUrl;
        JSONObject requestParams;
        double la1,lo1,la2,lo2;  // given lat, long, create a bounding box and search from that
        long maxTime;

        requestMethod = Request.Method.GET;
        requestUrl = "https://esdr.cmucreatelab.org/api/v1/feeds";
        requestParams = null;

        // only request AirNow (11) or ACHD (1)
        requestUrl += "?whereOr=ProductId=11,ProductId=1";
        // the past 24 hours
        maxTime = new Date().getTime() / 1000 - 86400;
        // get bounding box
        la1 = latd-Constants.MapGeometry.BOUNDBOX_LAT;
        la2 = latd+Constants.MapGeometry.BOUNDBOX_LONG;
        lo1 = longd-Constants.MapGeometry.BOUNDBOX_LAT;
        lo2 = longd+Constants.MapGeometry.BOUNDBOX_LONG;
        requestUrl += "&whereAnd=latitude>="+la1+",latitude<="+la2+",longitude>="+lo1+",longitude<="+lo2+",maxTimeSecs>="+maxTime;

        // only request from ESDR the fields that we care about
        requestUrl += "&fields=id,name,exposure,isMobile,latitude,longitude,productId,channelBounds";

        this.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
    }


    public void requestChannelReading(final Feed feed, final Channel channel) {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;
        final String channelName = channel.getName();

        requestMethod = Request.Method.GET;
        requestUrl = "https://esdr.cmucreatelab.org/api/v1/feeds/"+channel.getFeed_id()+"/channels/"+channelName+"/most-recent";

        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String result = null;
                try {
                    result = response.getJSONObject("data").getJSONObject("channels").getJSONObject(channelName).getJSONObject("mostRecentDataSample").getString("value");
                } catch (Exception e) {
                    // TODO handle exception
                    Log.w(Constants.LOG_TAG,"Failed to request Channel Reading for "+channelName);
                    e.printStackTrace();
                }
                if (result != null) {
                    Log.i(Constants.LOG_TAG,"got value \""+result+"\" for Channel "+channelName);
                    feed.updateChannelReadings(Double.parseDouble(result));
                    GlobalHandler.getInstance(HttpRequestHandler.this.appContext).notifyGlobalDataSetChanged();
                }
            }
        };


        this.sendJsonRequest(requestMethod, requestUrl, null, response, null);
    }


    public void requestGoogleGeocode(String addressName, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        int requestMethod;
        String requestUrl;
        JSONObject requestParams;

        requestMethod = Request.Method.GET;
        requestUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        requestParams = null;
        try {
            requestUrl += java.net.URLEncoder.encode(addressName, "ISO-8859-1");
        } catch (Exception e) {
            Log.wtf(Constants.LOG_TAG,"Failed to encode string \"" + addressName + "\" using ISO-8859-1");
        }
        this.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
    }

}
