package org.cmucreatelab.tasota.airprototype.helpers.http;

import android.net.Uri;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.cmucreatelab.tasota.airprototype.classes.AuthorizedJsonObjectRequest;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by mike on 5/29/15.
 */
public class HttpRequestHandler implements Response.ErrorListener {


    // Singleton Implementation


    private GlobalHandler globalHandler;
    private static HttpRequestHandler classInstance;

    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized HttpRequestHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new HttpRequestHandler(globalHandler);
        }
        return classInstance;
    }

    // Nobody accesses the constructor
    private HttpRequestHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.queue = Volley.newRequestQueue(globalHandler.appContext);
    }


    // Handler attributes and methods


    private RequestQueue queue;


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


    public void sendJsonArrayRequest(int requestMethod, String requestUrl, JSONArray requestParams, Response.Listener<JSONArray> response, Response.ErrorListener error) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(requestMethod, requestUrl, requestParams, response, error);
        if (requestParams != null) {
            Log.v(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl + ", params=" + requestParams.toString());
        } else {
            Log.v(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl);
        }
        this.queue.add(jsonArrayRequest);
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
