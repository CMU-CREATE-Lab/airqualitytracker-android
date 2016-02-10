package org.cmucreatelab.tasota.airprototype.helpers.static_classes;

import org.cmucreatelab.tasota.airprototype.classes.AirNowObservation;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mike on 2/10/16.
 */
public class AirNowJsonParser {


    private static AirNowObservation parseObservationFromJson() throws JSONException {
        AirNowObservation observation;
        Date observedDatetime;
        String reportingArea;
        String stateCode;
        Location location;
        String parameterName;
        double aqi;

        try {
            // TODO parse
            observedDatetime = new Date();
            reportingArea = "";
            stateCode = "";
            location = new Location(0,0);
            parameterName = "";
            aqi = 0;

            observation = new AirNowObservation(observedDatetime, reportingArea, stateCode, location, parameterName, aqi);
            return observation;
        } catch (Exception e) {
            throw e;
        }
    }


    public static ArrayList<AirNowObservation> parseObservationsFromJson() {
        ArrayList<AirNowObservation> results = new ArrayList<>();

        // TODO parse json

        return results;
    }

}
