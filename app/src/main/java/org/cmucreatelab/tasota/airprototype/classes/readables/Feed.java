package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import java.lang.*;
import java.util.ArrayList;

/**
 * Created by mike on 6/1/15.
 */
public abstract class Feed implements Readable {

    // class attributes
    private ReadableValueType readableValueType;
    protected long feed_id;
    protected String name;
    protected String exposure; // (FROM DOCS): an enum and must be one of indoor, outdoor, or virtual
    protected boolean isMobile;
    protected Location location;
    protected long productId;
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
    public double getLastTime() { return lastTime; }
    public void setLastTime(double lastTime) { this.lastTime = lastTime; }
    public ReadableValueType getReadableValueType() { return readableValueType; }
    public void setReadableValueType(ReadableValueType readableValueType) { this.readableValueType = readableValueType; }


    public enum ReadableValueType {
        INSTANTCAST, NOWCAST, NONE
    }


    // class constructor
    public Feed() {
        this.name = "";
        this.exposure = "";
        this.readableValueType = ReadableValueType.NONE;
    }


    // Readable implementation


    private static final Type readableType = Readable.Type.FEED;
    private final ArrayList<ReadableValue> readableValues = new ArrayList<>();


    @Override
    public Type getReadableType() {
        return readableType;
    }

}
