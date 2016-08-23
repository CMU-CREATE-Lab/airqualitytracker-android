package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import java.lang.*;
import java.util.Collection;

/**
 * Created by mike on 8/14/15.
 */
public class Speck extends Feed {

    // class attributes
    protected long deviceId;
    private int positionId;
    private long _id;
    private String apiKeyReadOnly;
    // getters/setters
    public void setChannels(Collection<Channel> channels) { this.channels.clear(); this.channels.addAll(channels); }
    public long getDeviceId() { return deviceId; }
    public void setDeviceId(long deviceId) { this.deviceId = deviceId; }
    public int getPositionId() { return positionId; }
    public void setPositionId(int positionId) { this.positionId = positionId; }
    public long get_id() { return _id; }
    public void set_id(long _id) { this._id = _id; }
    public String getApiKeyReadOnly() { return apiKeyReadOnly; }
    public void setApiKeyReadOnly(String apiKeyReadOnly) { this.apiKeyReadOnly = apiKeyReadOnly; }


    // class constructor
    public Speck(Feed feed, long deviceId) {
        this.feed_id = feed.feed_id;
        this.name = feed.name;
        this.exposure = feed.exposure;
        this.isMobile = feed.isMobile;
        this.location = feed.location;
        this.productId = feed.productId;
        this.channels = feed.channels;
        this.lastTime = feed.lastTime;
        this.deviceId = deviceId;
    }


    // class constructor
    public Speck(String apiKeyReadOnly, long deviceId, String exposure, long feedId, boolean isMobile, Location location, String name, int positionId, long productId) {
        this.apiKeyReadOnly = apiKeyReadOnly;
        this.deviceId = deviceId;
        this.exposure = exposure;
        this.feed_id = feedId;
        this.isMobile = isMobile;
        this.location = location;
        this.name = name;
        this.positionId = positionId;
        this.productId = productId;
    }


    // Readable implementation


    private static final Type readableType = Readable.Type.SPECK;


    public Type getReadableType() {
        return readableType;
    }

}
