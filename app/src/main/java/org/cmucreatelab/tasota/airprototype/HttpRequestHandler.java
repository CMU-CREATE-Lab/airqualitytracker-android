package org.cmucreatelab.tasota.airprototype;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by mike on 5/29/15.
 */
public class HttpRequestHandler {

    private static HttpRequestHandler classInstance;
    private Context appContext;
    private RequestQueue queue;

    // Distance from central point, in kilometers (box dimension will be 2x larger)
    private final double BOUNDBOX_HEIGHT = 10.0;
    // Distance from central point, in kilometers (box dimension will be 2x larger)
    private final double BOUNDBOX_LENGTH = 10.0;
    // radius of Earth
    private final double RADIUS_EARTH = 6371.0;
    // ASSERT these values will be less than 90.0
    private final double BOUNDBOX_LAT = BOUNDBOX_HEIGHT / ( RADIUS_EARTH * 2 * Math.PI ) * 360.0;
    private final double BOUNDBOX_LONG = BOUNDBOX_LENGTH / ( RADIUS_EARTH * 2 * Math.PI ) * 360.0;


    // Nobody accesses the constructor
    private HttpRequestHandler(Context ctx) {
        this.appContext = ctx;
        this.queue = Volley.newRequestQueue(this.appContext);
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized HttpRequestHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new HttpRequestHandler(ctx);
        }
        return classInstance;
    }


    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestMethod, requestUrl, requestParams, response, error);
        this.queue.add(jsonRequest);
    }


    public void requestFeeds(double latd, double longd, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        int requestMethod = Request.Method.GET;
        String requestUrl = "https://esdr.cmucreatelab.org/api/v1/feeds";
        JSONObject requestParams = null;

        // only request AirNow (11) or ACHD (1)
        requestUrl += "?whereOr=ProductId=11,ProductId=1";

//        if (latd != 0.0 && longd != 0.0) {
        // given lat, long, create a bounding box and search from that
        double la1,lo1,la2,lo2;
        // the past 24 hours
        long maxTime = new Date().getTime() / 1000 - 86400;
        la1 = latd-BOUNDBOX_LAT;
        la2 = latd+BOUNDBOX_LONG;
        lo1 = longd-BOUNDBOX_LAT;
        lo2 = longd+BOUNDBOX_LONG;
        requestUrl += "&whereAnd=latitude>="+la1+",latitude<="+la2+",longitude>="+lo1+",longitude<="+lo2+",maxTimeSecs>="+maxTime;
//        }

        // only request from ESDR the fields that we care about
        requestUrl += "&fields=id,name,exposure,isMobile,latitude,longitude,channelBounds";

        Log.i("requestFeeds", "requestUrl=" + requestUrl);
        HttpRequestHandler curl = HttpRequestHandler.getInstance(this.appContext);
        curl.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
    }
}
