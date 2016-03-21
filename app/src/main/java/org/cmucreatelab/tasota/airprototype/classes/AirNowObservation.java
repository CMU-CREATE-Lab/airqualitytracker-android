package org.cmucreatelab.tasota.airprototype.classes;

import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import java.util.Date;

/**
 * Created by mike on 2/10/16.
 */
public class AirNowObservation {

    // class attributes
    private Date observedDatetime;
    private String readableDate;
    private String reportingArea;
    private String stateCode;
    private Location location;
    private String parameterName;
    private double aqi;
    // getters/setters
    public Date getObservedDatetime() { return observedDatetime; }
    public void setObservedDatetime(Date observedDatetime) { this.observedDatetime = observedDatetime; }
    public String getReadableDate() { return readableDate; }
    public void setReadableDate(String readableDate) { this.readableDate = readableDate; }
    public String getReportingArea() { return reportingArea; }
    public void setReportingArea(String reportingArea) { this.reportingArea = reportingArea; }
    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public String getParameterName() { return parameterName; }
    public void setParameterName(String parameterName) { this.parameterName = parameterName; }
    public double getAqi() { return aqi; }
    public void setAqi(double aqi) { this.aqi = aqi; }


    // class constructor
    public AirNowObservation(Date observedDatetime, String readableDate, String reportingArea, String stateCode, Location location, String parameterName, double aqi) {
        this.observedDatetime = observedDatetime;
        this.readableDate = readableDate;
        this.reportingArea = reportingArea;
        this.stateCode = stateCode;
        this.location = location;
        this.parameterName = parameterName;
        this.aqi = aqi;
    }

}
