package org.cmucreatelab.tasota.airprototype.classes;

import org.json.JSONObject;

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


    // Helper function to create an object from a channel's JSON
    public static Channel parseChannelFromJson(String channelName, long feedId, JSONObject entry) {
        Channel c = new Channel();
        try {
            String name = channelName;
            long feed_id = feedId;
            long minTimeSecs = Long.parseLong(entry.get("minTimeSecs").toString());
            long maxTimeSecs = Long.parseLong(entry.get("maxTimeSecs").toString());;
            float minValue = Float.parseFloat(entry.get("minValue").toString());
            float maxValue = Float.parseFloat(entry.get("maxValue").toString());

            c.setName(name);
            c.setFeed_id(feed_id);
            c.setMinTimeSecs(minTimeSecs);
            c.setMaxTimeSecs(maxTimeSecs);
            c.setMinValue(minValue);
            c.setMaxValue(maxValue);
        } catch (Exception e) {
            // TODO catch exception "failed to find JSON attr"
            e.printStackTrace();
        }
        return c;
    }

}
