package org.cmucreatelab.tasota.airprototype.classes;

/**
 * Created by mike on 5/9/16.
 */
public class DayFeedValue {

    // class attributes
    private long time;
    private double mean;
    private double median;
    private double max;
    // getters/setters
    public long getTime() { return time; }
    public double getMean() { return mean; }
    public double getMedian() { return median; }
    public double getMax() { return max; }


    public DayFeedValue(long time, double mean, double median, double max) {
        this.time = time;
        this.mean = mean;
        this.median = median;
        this.max = max;
    }

}
