package org.cmucreatelab.tasota.airprototype.classes;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
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
    // The relevant value (PM2.5) pulled for the given Feed
    private double feedValue;
    private double lastTime;

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
    public double getFeedValue() {
        return feedValue;
    }
    public void setFeedValue(double feedValue) {
        if (feedValue < 0.0) {
            this.feedValue = 0.0;
            Log.w(Constants.LOG_TAG, "received negative feedValue " + feedValue + " to set.");
        } else {
            this.feedValue = feedValue;
        }
    }
    public double getLastTime() {
        return lastTime;
    }
    public void setLastTime(double lastTime) {
        this.lastTime = lastTime;
    }


    public Feed() {
        this.channels = new ArrayList<>();
        this.name = "";
        this.exposure = "";
    }


    @Override
    public String toString() {
        // TODO this is used by android.R.layout.simple_list_item_1 as a simple ArrayAdapter in AddressShowActivity and LoginActivity; should be deleted when no longer used
        return "(" + this.feed_id + ")" + this.name;
    }

}
