package org.cmucreatelab.tasota.airprototype.helpers.http.esdr;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.classes.readables.Channel;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers.EsdrJsonParser;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mike on 1/14/16.
 */
public class EsdrTilesHandler {


    // Singleton Implementation


    private GlobalHandler globalHandler;
    private static EsdrTilesHandler classInstance;

    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized EsdrTilesHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new EsdrTilesHandler(globalHandler);
        }
        return classInstance;
    }

    // Nobody accesses the constructor
    private EsdrTilesHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    // Handler attributes and methods


    // ASSERT: a and b will never share keys (timestamps)
    private HashMap<Integer, ArrayList<Double>> union(HashMap<Integer, ArrayList<Double>> a, HashMap<Integer, ArrayList<Double>> b) {
        HashMap<Integer, ArrayList<Double>> results = new HashMap<>();

        Set<Integer> set1 = new HashSet<>(a.keySet());
        set1.retainAll(b.keySet());
        if ( set1.retainAll(b.keySet()) && set1.size() > 0) {
            Log.e(Constants.LOG_TAG, "non-empty intersection of keys in union (this shouldn't be happening; information will be lost.");
        }

        // lazily set results (overwrites in the case of intersection, which shouldn't happen)
        for (Integer key: a.keySet()) {
            results.put(key, a.get(key));
        }
        for (Integer key: b.keySet()) {
            results.put(key, b.get(key));
        }

        return results;
    }


    public void requestTilesFromChannel(final Channel channel, final int timestamp) {
        final int maxTime,minTime;
        final HashMap<Integer, ArrayList<Double>> firstResponse,secondResponse;
        firstResponse = new HashMap<>();
        secondResponse = new HashMap<>();

        maxTime = timestamp;
        // TODO we use 13 hours since ESDR won't always report the previous hour to us
        //minTime = timestamp - 3600*12;
        minTime = timestamp - 3600*13;

        // Level is 2**11 => 2048 seconds
        final int level = 11;
        // our tile offset (calculates most recent tile at the current level)
        final int offset = (int)(maxTime / (512.0*Math.pow(2.0,level) ));

        final Response.Listener<JSONObject> firstHandler,secondHandler;
        secondHandler = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HashMap<Integer, ArrayList<Double>> result;

                // response handling
                EsdrJsonParser.parseTiles(response, minTime, maxTime, secondResponse);

                // union both responses then call completion handler
                result = union(firstResponse, secondResponse);
                channel.responseHandler.onResponse(globalHandler.appContext, result, timestamp);
            }
        };
        firstHandler = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // response handling
                EsdrJsonParser.parseTiles(response, minTime, maxTime, firstResponse);

                // send 2nd request
                int requestMethod;
                String requestUrl;
                requestMethod = Request.Method.GET;
                requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds/" + channel.getFeed_id() + "/channels/" + channel.getName() + "/tiles/" + level + "." + (offset-1);
                globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, secondHandler);
            }
        };

        int requestMethod;
        String requestUrl;
        requestMethod = Request.Method.GET;
        requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds/" + channel.getFeed_id() + "/channels/" + channel.getName() + "/tiles/" + level + "." + offset;
        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, firstHandler);
    }

}
