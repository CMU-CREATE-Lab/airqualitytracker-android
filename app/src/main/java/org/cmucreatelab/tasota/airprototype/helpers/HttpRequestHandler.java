package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.cmucreatelab.tasota.airprototype.classes.Channel;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.json.JSONObject;

/**
 * Created by mike on 5/29/15.
 */
public class HttpRequestHandler implements Response.ErrorListener {

    private Context appContext;
    private RequestQueue queue;
    private EsdrFeedsHandler esdrFeedsHandler;
    private EsdrAuthHandler esdrAuthHandler;
    private static HttpRequestHandler classInstance;


    // Nobody accesses the constructor
    private HttpRequestHandler(Context ctx) {
        this.appContext = ctx;
        this.queue = Volley.newRequestQueue(this.appContext);
        this.esdrFeedsHandler = EsdrFeedsHandler.getInstance(ctx, this);
        this.esdrAuthHandler = EsdrAuthHandler.getInstance(ctx, this);
    }


    // Only way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized HttpRequestHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new HttpRequestHandler(ctx);
        }
        return classInstance;
    }


    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response) {
        JsonObjectRequest jsonRequest;

        jsonRequest = new JsonObjectRequest(requestMethod, requestUrl, requestParams, response, this);
        Log.d(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl);
        this.queue.add(jsonRequest);
    }


    public void requestFeeds(double latd, double longd, double maxTime, Response.Listener<JSONObject> response) {
        esdrFeedsHandler.requestFeeds(latd,longd,maxTime,response);
    }


    public void requestChannelReading(final Feed feed, final Channel channel) {
        esdrFeedsHandler.requestChannelReading(feed,channel);
    }


    public void requestEsdrToken(String username, String password) {
        esdrAuthHandler.requestEsdrToken(username,password);
    }


    public void requestEsdrRefresh(String refreshToken) {
        esdrAuthHandler.requestEsdrRefresh(refreshToken);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(Constants.LOG_TAG, "Received error from Volley: " + error.getLocalizedMessage());
    }

}
