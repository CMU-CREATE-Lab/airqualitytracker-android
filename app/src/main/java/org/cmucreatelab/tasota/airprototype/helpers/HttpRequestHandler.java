package org.cmucreatelab.tasota.airprototype.helpers;

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

        Log.i("sendJsonRequest", "requestUrl=" + requestUrl);
        jsonRequest = new JsonObjectRequest(requestMethod, requestUrl, requestParams, response, error);
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
        la1 = latd-MapGeometry.BOUNDBOX_LAT;
        la2 = latd+MapGeometry.BOUNDBOX_LONG;
        lo1 = longd-MapGeometry.BOUNDBOX_LAT;
        lo2 = longd+MapGeometry.BOUNDBOX_LONG;
        requestUrl += "&whereAnd=latitude>="+la1+",latitude<="+la2+",longitude>="+lo1+",longitude<="+lo2+",maxTimeSecs>="+maxTime;

        // only request from ESDR the fields that we care about
        requestUrl += "&fields=id,name,exposure,isMobile,latitude,longitude,productId,channelBounds";

        this.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
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
            // TODO handle unexpected encoding exception
            e.printStackTrace();
        }
        this.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
    }

}
