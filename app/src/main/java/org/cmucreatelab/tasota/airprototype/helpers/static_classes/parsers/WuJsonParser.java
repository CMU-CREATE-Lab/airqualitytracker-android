package org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers;

import android.util.Log;

import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mike on 2/10/16.
 */
public class WuJsonParser {


    private static SimpleAddress parseAddressFromJson(JSONObject row) throws JSONException {
        SimpleAddress result;
        double latitude,longitude;
        String name,zipcode;

        try {
            latitude = row.getDouble("lat");
            longitude = row.getDouble("lon");
            name = row.getString("name");
            zipcode = row.getString("zmw");
            result = new SimpleAddress(name, zipcode, new Location(latitude, longitude));
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to parse Address from JSON.");
            throw e;
        }

        return result;
    }


    public static ArrayList<SimpleAddress> parseAddressesFromJson(JSONObject response) {
        ArrayList<SimpleAddress> result = new ArrayList<>();
        JSONArray jsonAddresses;
        int i, size;

        try {
            jsonAddresses = response.getJSONArray("RESULTS");
            size = jsonAddresses.length();
            for (i = 0; i < size; i++) {
                result.add(parseAddressFromJson(jsonAddresses.getJSONObject(i)));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "JSON Format error (missing \"RESULTS\" or other field).");
        }

        return result;
    }

}
