package org.cmucreatelab.tasota.airprototype.classes.readables;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;

import java.lang.*;
import java.util.ArrayList;

/**
 * Created by mike on 6/1/15.
 */
public class Feed implements Readable {


    // Readable implementation


    public enum ReadableValueType {
        INSTANTCAST, NOWCAST, NONE
    }


    public Type getReadableType() {
        return readableType;
    }


    public boolean hasReadableValue() {
        return readableValueType != ReadableValueType.NONE;
    }


    public double getReadableValue() {
        if (hasReadableValue()) {
            switch (readableValueType) {
                case INSTANTCAST:
                    return channels.get(0).getInstantCastValue();
                case NOWCAST:
                    return channels.get(0).getNowCastValue();
                default:
                    Log.e(Constants.LOG_TAG, "ERROR - Could not detect ReadableValueType");
            }
        }
        return 0;
    }


    public String getName() {
        return name;
    }


    // Class Attributes and Constructor(s)


    private static final Type readableType = Readable.Type.FEED;
    private ReadableValueType readableValueType;
//    private boolean hasReadableValue;
    // NOTE: if you want more attributes, be sure that they are included in the json response (for parsing)
    protected long feed_id;
    protected String name;
    // (FROM DOCS): an enum and must be one of indoor, outdoor, or virtual
    protected String exposure;
    protected boolean isMobile;
    protected Location location;
    protected long productId;
    protected ArrayList<Channel> channels;
//    protected double feedValue;
    protected double lastTime;


    public Feed() {
        this.channels = new ArrayList<>();
        this.name = "";
        this.exposure = "";
        this.readableValueType = ReadableValueType.NONE;
    }


    // Getters/Setters


    public void setName(String name) {
        this.name = name;
    }
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
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public ArrayList<Channel> getChannels() {
        return channels;
    }
    public double getLastTime() {
        return lastTime;
    }
    public void setLastTime(double lastTime) {
        this.lastTime = lastTime;
    }
    public ReadableValueType getReadableValueType() {
        return readableValueType;
    }
    public void setReadableValueType(ReadableValueType readableValueType) {
        this.readableValueType = readableValueType;
    }

}
