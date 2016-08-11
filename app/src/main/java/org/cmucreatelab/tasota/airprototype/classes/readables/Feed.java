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

    // class attributes
    // NOTE: if you want more attributes, be sure that they are included in the json response (for parsing)
    private ReadableValueType readableValueType;
    protected long feed_id;
    protected String name;
    protected String exposure; // (FROM DOCS): an enum and must be one of indoor, outdoor, or virtual
    protected boolean isMobile;
    protected Location location;
    protected long productId;
    protected ArrayList<Channel> channels;
    protected double lastTime;
    // getters/setters
    public long getFeed_id() { return feed_id; }
    public void setFeed_id(long feed_id) { this.feed_id = feed_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getExposure() { return exposure; }
    public void setExposure(String exposure) { this.exposure = exposure; }
    public boolean isMobile() { return isMobile; }
    public void setIsMobile(boolean isMobile) { this.isMobile = isMobile; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    public ArrayList<Channel> getChannels() { return channels; }
    public double getLastTime() { return lastTime; }
    public void setLastTime(double lastTime) { this.lastTime = lastTime; }
    public ReadableValueType getReadableValueType() { return readableValueType; }
    public void setReadableValueType(ReadableValueType readableValueType) { this.readableValueType = readableValueType; }


    public enum ReadableValueType {
        INSTANTCAST, NOWCAST, NONE
    }


    // class constructor
    public Feed() {
        this.channels = new ArrayList<>();
        this.name = "";
        this.exposure = "";
        this.readableValueType = ReadableValueType.NONE;
    }


    public ArrayList<Channel> getPmChannels() {
        ArrayList<Channel> result = new ArrayList<>();
        String channelName;

        for (Channel channel : this.channels) {
            channelName = channel.getName();
            for (String cn : Constants.channelNamesPm) {
                if (channelName.equals(cn)) {
                    result.add(channel);
                    break;
                }
            }
        }

        return result;
    }


    public ArrayList<Channel> getOzoneChannels() {
        ArrayList<Channel> result = new ArrayList<>();
        String channelName;

        for (Channel channel : this.channels) {
            channelName = channel.getName();
            for (String cn : Constants.channelNamesOzone) {
                if (channelName.equals(cn)) {
                    result.add(channel);
                    break;
                }
            }
        }

        return result;
    }


    // Readable implementation


    private static final Type readableType = Readable.Type.FEED;


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

}
