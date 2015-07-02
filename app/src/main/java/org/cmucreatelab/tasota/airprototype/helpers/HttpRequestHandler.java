package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.cmucreatelab.tasota.airprototype.classes.AuthorizedJsonObjectRequest;
import org.cmucreatelab.tasota.airprototype.classes.Channel;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
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


    // helper method which assumes that you want to use this instance as the default ErrorListener
    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response) {
        sendJsonRequest(requestMethod,requestUrl,requestParams,response,this);
    }


    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestMethod, requestUrl, requestParams, response, error);
        if (requestParams != null) {
            Log.d(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl + ", params=" + requestParams.toString());
        } else {
            Log.d(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl);
        }
        this.queue.add(jsonRequest);
    }


    public void sendAuthorizedJsonRequest(String authToken, int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response) {
        AuthorizedJsonObjectRequest jsonRequest = new AuthorizedJsonObjectRequest(authToken, requestMethod, requestUrl, requestParams, response, this);
        if (requestParams != null) {
            Log.d(Constants.LOG_TAG, "sending Authorized JSON request (authToken="+authToken+") with requestUrl=" + requestUrl + ", params=" + requestParams.toString());
        } else {
            Log.d(Constants.LOG_TAG, "sending Authorized JSON request (authToken="+authToken+") with requestUrl=" + requestUrl);
        }
        this.queue.add(jsonRequest);
    }


    public void requestFeeds(double latd, double longd, double maxTime, Response.Listener<JSONObject> response) {
        esdrFeedsHandler.requestFeeds(latd, longd, maxTime, response);
    }

    public void requestPrivateFeeds(String authToken, Response.Listener<JSONObject> response) {
        esdrFeedsHandler.requestPrivateFeeds(authToken, response);
    }


    public void requestChannelReading(final Feed feed, final Channel channel) {
        esdrFeedsHandler.requestChannelReading(feed, channel);
    }


    public void requestEsdrToken(String username, String password, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        esdrAuthHandler.requestEsdrToken(username,password,response,error);
    }


    public void requestEsdrRefresh(String refreshToken) {
        esdrAuthHandler.requestEsdrRefresh(refreshToken);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(Constants.LOG_TAG, "Received error from Volley in HttpRequestHandler (default): " + error.getLocalizedMessage());
    }

}
