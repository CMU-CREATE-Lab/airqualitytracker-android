package org.cmucreatelab.tasota.airprototype.classes;

import java.util.ArrayList;

/**
 * Created by mike on 6/1/15.
 */
public class Feed {

    // NOTE: if you want more attributes, be sure that they are included in the json response (for parsing)
    private long feed_id;
    private String name;
    // (FROM DOCS): an enum and must be one of indoor, outdoor, or virtual
    private String exposure;
    private boolean isMobile;
    private double latitude;
    private double longitude;
    private long productId;
    private ArrayList<Channel> channels;

    // what we want to display to the user (ug/m^3)
    private String feedDisplay;

    public long getProductId() {
        return productId;
    }
    public void setProductId(long productId) {
        this.productId = productId;
    }
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
    public String getFeedDisplay() {
        if (feedDisplay == null) {
            updateChannelReadings();
        }
        return feedDisplay;
    }


    public Feed() {
        this.channels = new ArrayList<Channel>();
        this.name = new String();
        this.exposure = new String();
    }


    public void updateChannelReadings() {
        // TODO grab most recent readings from the Feed's channels
        // TODO this is where Chris' API call will come in handy
        feedDisplay = "feed="+String.valueOf(this.feed_id);
    }


    @Override
    public String toString() {
        // TODO generate a proper label from the class attributes
        return "(" + this.feed_id + ")" + this.name;
    }

}
