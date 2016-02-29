package org.cmucreatelab.tasota.airprototype.helpers;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.classes.AirNowObservation;
import org.cmucreatelab.tasota.airprototype.classes.AirNowReadable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.AirNowJsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONArray;
import java.util.ArrayList;

/**
 * Created by mike on 2/10/16.
 */
public class AirNowRequestHandler implements Response.ErrorListener {

    private GlobalHandler globalHandler;


    // GlobalHandler accesses the constructor
    protected AirNowRequestHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void requestAirNowObservation(final AirNowReadable readable) {
        int requestMethod;
        String requestUrl;

        Response.Listener<JSONArray> response = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<AirNowObservation> results = AirNowJsonParser.parseObservationsFromJson(response);
                readable.appendAndSort(results);
            }
        };

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
