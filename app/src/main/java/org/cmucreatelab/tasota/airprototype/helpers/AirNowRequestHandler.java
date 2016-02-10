package org.cmucreatelab.tasota.airprototype.helpers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONObject;

/**
 * Created by mike on 2/10/16.
 */
public class AirNowRequestHandler implements Response.ErrorListener {

    private GlobalHandler globalHandler;


    // GlobalHandler accesses the constructor
    protected AirNowRequestHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void requestAirNowObservation(Location location, Response.Listener<JSONObject> response) {
        int requestMethod;
        JSONObject requestParams;
        String requestUrl;

        try {
            requestMethod = Request.Method.GET;
            requestUrl = Constants.Esdr.API_URL + "/aq/observation/latLong/current/?format=application/json&distance=25&latitude="+location.latitude+"&longitude="+location.longitude+"&API_KEY="+Constants.AppSecrets.AIR_NOW_API_KEY;
            globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response, this);
        } catch (Exception e) {
            Log.w(Constants.LOG_TAG, "Failed to request AirNow Observation.");
            e.printStackTrace();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO error handling on AirNow requests
    }

}
