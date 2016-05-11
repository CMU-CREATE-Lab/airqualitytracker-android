package org.cmucreatelab.tasota.airprototype.classes;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 5/9/16.
 */
public class DayFeedValue {

    public enum DirtyDaysValueType {
        MEAN, MEDIAN, MAX;
    }

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


    public double getDirtyDaysValue() {
        switch (Constants.DIRTY_DAYS_VALUE_TYPE) {
            case MEAN:
                return mean;
            case MEDIAN:
                return median;
            case MAX:
                return max;
            default:
                Log.e(Constants.LOG_TAG,"DIRTY_DAYS_VALUE_TYPE Undefined enum type");
                return 0;
        }
    }

}
