package org.cmucreatelab.tasota.airprototype.classes;

import java.util.ArrayList;

/**
 * Created by mike on 6/1/15.
 */
public class Feed {
    private long feed_id;
    private String name;
    // (FROM DOCS): an enum and must be one of indoor, outdoor, or virtual
    private String exposure;
    private boolean isMobile;
    private double latitude;
    private double longitude;
    private ArrayList<Channel> channels;


    public long getFeed_id() {
        return feed_id;
    }
    public void setFeed_id(long feed_id) {
        this.feed_id = feed_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getExposure() {
        return exposure;
    }
    public void setExposure(String exposure) {
        this.exposure = exposure;
    }
    public boolean isMobile() {
        return isMobile;
    }
    public void setIsMobile(boolean isMobile) {
        this.isMobile = isMobile;
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
    public ArrayList<Channel> getChannels() {
        return channels;
    }
    // You should not need to directly access the array list of channels
//    public void setChannels(ArrayList<Channel> channels) {
//        this.channels = channels;
//    }

    public Feed() {
        this.channels = new ArrayList<Channel>();
    }
}
