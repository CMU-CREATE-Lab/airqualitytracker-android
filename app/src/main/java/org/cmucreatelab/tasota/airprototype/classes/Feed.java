package org.cmucreatelab.tasota.airprototype.classes;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 6/1/15.
 */
public class Feed implements Readable {

    private static final Type readableType = Readable.Type.FEED;
    public Type getReadableType() {
        return readableType;
    }
    public boolean hasReadableValue() {
        return (channels.size() > 0);
    }
    public double getReadableValue() {
        return this.feedValue;
    }
    public String getName() {
        return name;
    }

    // NOTE: if you want more attributes, be sure that they are included in the json response (for parsing)
    protected long feed_id;
    protected String name;
    // (FROM DOCS): an enum and must be one of indoor, outdoor, or virtual
    protected String exposure;
    protected boolean isMobile;
    protected double latitude;
    protected double longitude;
    protected long productId;
    protected ArrayList<Channel> channels;
    protected double feedValue;
    protected double lastTime;
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

}
