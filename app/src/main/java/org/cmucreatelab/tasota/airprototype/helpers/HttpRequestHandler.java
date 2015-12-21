package org.cmucreatelab.tasota.airprototype.helpers;

import android.net.Uri;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.cmucreatelab.tasota.airprototype.classes.AuthorizedJsonObjectRequest;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.json.JSONObject;

/**
 * Created by mike on 5/29/15.
 */
public class HttpRequestHandler implements Response.ErrorListener {

    private GlobalHandler globalHandler;
    private RequestQueue queue;


    // GlobalHandler accesses the constructor
    protected HttpRequestHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.queue = Volley.newRequestQueue(globalHandler.appContext);
    }


    // helper method which assumes that you want to use this instance as the default ErrorListener
    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response) {
        sendJsonRequest(requestMethod, requestUrl, requestParams, response, this);
    }


    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestMethod, requestUrl, requestParams, response, error);
        if (requestParams != null) {
            Log.v(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl + ", params=" + requestParams.toString());
        } else {
            Log.v(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl);
        }
        this.queue.add(jsonRequest);
    }


    public void sendAuthorizedJsonRequest(String authToken, int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response) {
        AuthorizedJsonObjectRequest jsonRequest = new AuthorizedJsonObjectRequest(authToken, requestMethod, requestUrl, requestParams, response, this);
        if (requestParams != null) {
            Log.v(Constants.LOG_TAG, "sending Authorized JSON request (authToken="+authToken+") with requestUrl=" + requestUrl + ", params=" + requestParams.toString());
        } else {
            Log.v(Constants.LOG_TAG, "sending Authorized JSON request (authToken="+authToken+") with requestUrl=" + requestUrl);
        }
        this.queue.add(jsonRequest);
    }


    public void requestGeocodingFromApi(String input, Response.Listener<JSONObject> response) {
        String requestUrl = "http://autocomplete.wunderground.com/aq?query="+Uri.encode(input)+"&c=US";
        this.sendJsonRequest(Request.Method.GET, requestUrl, null, response);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(Constants.LOG_TAG, "Received error from Volley in HttpRequestHandler (default): " + error.getLocalizedMessage());
    }

}
