package org.cmucreatelab.tasota.airprototype;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by mike on 5/29/15.
 */
public class HttpRequestHandler {

    private static HttpRequestHandler classInstance;
    private Context appContext;
    private RequestQueue queue;


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
}
