package org.cmucreatelab.tasota.airprototype.classes;


/**
 * Created by mike on 6/1/15.
 */
public class Channel {

    private String name;
    private Feed feed;
    private double minTimeSecs;
    private double maxTimeSecs;
    private double minValue;
    private double maxValue;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getMinTimeSecs() {
        return minTimeSecs;
    }
    public void setMinTimeSecs(double minTimeSecs) {
        this.minTimeSecs = minTimeSecs;
    }
    public double getMaxTimeSecs() {
        return maxTimeSecs;
    }
    public void setMaxTimeSecs(double maxTimeSecs) {
        this.maxTimeSecs = maxTimeSecs;
    }
    public double getMinValue() {
        return minValue;
    }
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }
    public double getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    public Feed getFeed() {
        return feed;
    }
    public void setFeed(Feed feed) {
        this.feed = feed;
    }


    public long getFeed_id() {
        return feed.getFeed_id();
    }


    // TODO getters/setters
    private double nowcastValue;
    public void requestNowCast() {
        // TODO grab info from esdr, then process and calculate nowcast and store in attribute
    }

}
