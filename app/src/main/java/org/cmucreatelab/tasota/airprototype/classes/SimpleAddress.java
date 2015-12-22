package org.cmucreatelab.tasota.airprototype.classes;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 6/1/15.
 */
public class SimpleAddress implements Readable {


    // Readable implementation


    public Type getReadableType() {
        return readableType;
    }


    public boolean hasReadableValue() {
        return this.getClosestFeed() != null;
    }


    public double getReadableValue() {
        if (hasReadableValue())
            return this.getClosestFeed().getFeedValue();
        Log.w(Constants.LOG_TAG,"Tried getReadableValue on SimpleAddress with hasReadableValue=false; returning 0");
        return 0.0;
    }


    public String getName() {
        return name;
    }


    // Class Attributes and Constructor(s)


    private static final Type readableType = Readable.Type.ADDRESS;
    private long _id;
    private String name;
    private String zipcode;
    private double latitude,longitude;
    private Feed closestFeed = null;
    public final ArrayList<Feed> feeds = new ArrayList<>();
    private boolean isCurrentLocation;
    private int positionId;


    public SimpleAddress(String name, String zipcode, double latitude, double longitude) {
        this.name = name;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isCurrentLocation = false;
    }


    public SimpleAddress(String name, String zipcode, double latitude, double longitude, boolean isCurrentLocation) {
        this(name, zipcode, latitude, longitude);
        this.isCurrentLocation = isCurrentLocation;
    }


    // Getters/Setters


    public boolean isCurrentLocation() {
        return isCurrentLocation;
    }
    public long get_id() {
        return _id;
    }
    public void set_id(long _id) {
        this._id = _id;
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
    public String getZipcode() {
        return zipcode;
    }
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    public int getPositionId() {
        return positionId;
    }
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

}
