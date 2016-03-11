package org.cmucreatelab.tasota.airprototype.helpers.http;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirNowReadable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONArray;

/**
 * Created by mike on 2/10/16.
 */
public class AirNowRequestHandler implements Response.ErrorListener {


    // Singleton Implementation


    private GlobalHandler globalHandler;
    private static AirNowRequestHandler classInstance;

    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized AirNowRequestHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new AirNowRequestHandler(globalHandler);
        }
        return classInstance;
    }

    // Nobody accesses the constructor
    private AirNowRequestHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    // Handler attributes and methods


    public void requestAirNowObservation(final AirNowReadable readable, Response.Listener<JSONArray> response) {
        int requestMethod;
        String requestUrl;

        try {
            requestMethod = Request.Method.GET;
            Location location = readable.getLocation();
            requestUrl = Constants.AirNow.API_URL + "/aq/observation/latLong/current/?format=application/json&distance=25&latitude="+location.latitude+"&longitude="+location.longitude+"&API_KEY="+Constants.AppSecrets.AIR_NOW_API_KEY;
            globalHandler.httpRequestHandler.sendJsonArrayRequest(requestMethod, requestUrl, null, response, this);
        } catch (Exception e) {
            Log.w(Constants.LOG_TAG, "Failed to request AirNow Observation.");
            e.printStackTrace();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(Constants.LOG_TAG, "ERROR in AirNowRequestHandler");
    }

}
