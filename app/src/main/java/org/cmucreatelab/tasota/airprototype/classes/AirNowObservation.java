package org.cmucreatelab.tasota.airprototype.classes;

import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import java.util.Date;

/**
 * Created by mike on 2/10/16.
 */
public class AirNowObservation {

//    public static String[] fields = {
//            "DateObserved",
//            "HourObserved",
//            "LocalTimeZone",
//            "ReportingArea",
//            "StateCode",
//            "Latitude",
//            "Longitude",
//            "ParameterName",
//            "AQI",
//            "Category", // Number, Name
//            ""
//    };
//    // kk is HH without leading zeroes
//    // "year-date-month hour zone"
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM kk Z");
//    String input="";
//    try {
//        this.datetimeObserved = dateFormat.parse(input);
//    } catch (Exception e) {
//        throw e;
//    }

//    private String dateObserved;
//    private String hourObserved;
//    private String localTimeZone;
    private Date observedDatetime;
    private String reportingArea;
    private String stateCode;
    private Location location; // Latitude, Longitude
    private String parameterName; // we want PM2.5
    private double aqi;


    public AirNowObservation(Date observedDatetime, String reportingArea, String stateCode, Location location, String parameterName, double aqi) {
        this.observedDatetime = observedDatetime;
        this.reportingArea = reportingArea;
        this.stateCode = stateCode;
        this.location = location;
        this.parameterName = parameterName;
        this.aqi = aqi;
    }

}
