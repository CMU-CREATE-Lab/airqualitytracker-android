package org.cmucreatelab.tasota.airprototype.classes;

/**
 * Created by mike on 6/1/15.
 */
public class SimpleAddress {

    private long _id;
    private String name;
    private double latitude,longitude;
    private Feed closestFeed = null;

    public long get_id() {
        return _id;
    }
    public void set_id(long _id) {
        this._id = _id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public Feed getClosestFeed() {
        return closestFeed;
    }
    public void setClosestFeed(Feed closestFeed) {
        this.closestFeed = closestFeed;
    }


    public SimpleAddress(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
