package org.cmucreatelab.tasota.airprototype.classes.aqi_scales;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 6/9/16.
 */
public class SpeckReading extends Scalable {

    // static class attributes
    public static final String[] normalColors = {
            "#1a9850", "#91cf60", "#d9ef8b",
            "#FEE08B", "#FC8D59", "#D73027"
    };
    public static final String[] titles = {
            "Good", "Moderate", "Slightly Elevated",
            "Elevated", "High", "Very High"
    };
    // ranges measured in ug/m^3
    private static final int[] ranges = {
            21, 41, 81,
            161, 321
    };
    // class attributes
    private final double reading;
    private final int index;
    // getters
    public String getColor() { return normalColors[this.index]; }
    public String getTitle() { return titles[this.index]; }


    public SpeckReading(double reading) {
        this.reading = reading;
        this.index = getIndexFromReading(this.reading);
    }


    public int getIndexFromReading(double reading) {
        if (reading < 0) {
            return -1;
        }
        int i;
        for (i=0;i<ranges.length;i++) {
            if (reading < ranges[i]) {
                return i;
            }
        }
        return -1;
    }


    public String getRangeFromIndex(int index) {
        String result;
        if (index < 0) {
            Log.e(Constants.LOG_TAG, "getRangeFromIndex received index < 0.");
            result = "";
        } else if (index == 0) {
            result = "0-" + ranges[0];
        } else if (index == 5) {
            result = ranges[4] + "+";
        } else {
            result = ranges[index-1] + "-" + ranges[index];
        }
        return result;
    }

}
