package org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.AirNowObservation;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mike on 2/10/16.
 */
public class AirNowJsonParser {


    private static AirNowObservation parseObservationFromJson(JSONObject row) throws JSONException {
        AirNowObservation observation;
        Date observedDatetime;
        String reportingArea;
        String stateCode;
        Location location;
        String parameterName;
        double aqi;

        try {
            // parse
            String dateObserved = row.getString("DateObserved");
            int hourObserved = row.getInt("HourObserved");
            String localTimeZone = row.getString("LocalTimeZone");
            double latitude = row.getDouble("Latitude");
            double longitude = row.getDouble("Longitude");
            reportingArea = row.getString("ReportingArea");
            stateCode = row.getString("StateCode");
            parameterName = row.getString("ParameterName");
            aqi = row.getDouble("AQI");

            // intermediate objects
            String formattedString = dateObserved + " " + hourObserved + " " + localTimeZone;
            try {
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd kk z");
                observedDatetime = dateFormatter.parse(formattedString);
            } catch (ParseException e) {
                Log.e(Constants.LOG_TAG, "ERROR - failed to parse date from formattedString=" + formattedString);
                observedDatetime = new Date();
            }
            location = new Location(latitude, longitude);

            // return instance
            observation = new AirNowObservation(observedDatetime, reportingArea, stateCode, location, parameterName, aqi);
            return observation;
        } catch (JSONException e) {
            throw e;
        }
    }


    public static ArrayList<AirNowObservation> parseObservationsFromJson(JSONArray data) {
        ArrayList<AirNowObservation> results = new ArrayList<>();

        int size = data.length();
        for (int i=0; i<size; i++) {
            try {
                JSONObject row = data.getJSONObject(i);
                results.add(parseObservationFromJson(row));
            } catch (JSONException e) {
                Log.e(Constants.LOG_TAG, "ERROR on parseObservationsFromJson");
            }
        }

        return results;
    }

}
