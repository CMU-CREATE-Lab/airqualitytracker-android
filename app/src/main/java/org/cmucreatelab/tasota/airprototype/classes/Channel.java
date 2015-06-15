package org.cmucreatelab.tasota.airprototype.classes;


/**
 * Created by mike on 6/1/15.
 */
public class Channel {

    private String name;
    private long feed_id;
    // TODO ensure that minTimeSecs/MaxTimeSecs are integers (not floats)
    private long minTimeSecs;
    private long maxTimeSecs;
    private float minValue;
    private float maxValue;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getFeed_id() {
        return feed_id;
    }
    public void setFeed_id(long feed_id) {
        this.feed_id = feed_id;
    }
    public long getMinTimeSecs() {
        return minTimeSecs;
    }
    public void setMinTimeSecs(long minTimeSecs) {
        this.minTimeSecs = minTimeSecs;
    }
    public long getMaxTimeSecs() {
        return maxTimeSecs;
    }
    public void setMaxTimeSecs(long maxTimeSecs) {
        this.maxTimeSecs = maxTimeSecs;
    }
    public float getMinValue() {
        return minValue;
    }
    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }
    public float getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

}
