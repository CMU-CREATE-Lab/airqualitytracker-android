package org.cmucreatelab.tasota.airprototype.classes;


/**
 * Created by mike on 6/1/15.
 */
public class SimpleAddress {

    public enum IconType {
        GPS, SPECK, DEFAULT
    }
    private long _id;
    private String name;
    private String zipcode;
    private double latitude,longitude;
    private Feed closestFeed = null;
    private IconType iconType;
    private boolean isCurrentLocation;

    public long get_id() {
        return _id;
    }
    public boolean isCurrentLocation() {
        return isCurrentLocation;
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
    public IconType getIconType() {
        return iconType;
    }
    public void setIconType(IconType iconType) {
        this.iconType = iconType;
    }
    public String getZipcode() {
        return zipcode;
    }
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    public SimpleAddress(String name, String zipcode, double latitude, double longitude) {
        this.name = name;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.iconType = IconType.DEFAULT;
        this.isCurrentLocation = false;
    }


    public SimpleAddress(String name, String zipcode, double latitude, double longitude, boolean isCurrentLocation) {
        this(name, zipcode, latitude, longitude);
        this.isCurrentLocation = isCurrentLocation;
    }

}
