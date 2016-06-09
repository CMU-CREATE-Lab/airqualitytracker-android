package org.cmucreatelab.tasota.airprototype.classes.aqi_scales;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 6/9/16.
 */
public class WHOReading {

    // static class attributes
    private static final String[] colors = {
            "#a3ba5c", "#e9b642", "#e98c37",
            "#e24f36"
    };
    private static final String[] titles = {
            "Good", "Moderate", "Elevated", "High"
    };
    // ranges measured in ug/m^3
    private static final double[] ranges = {
            10.1, 25.5, 50.5
    };
    // class attributes
    private final double reading;
    private final int index;
    // getters
    public String getColor() { return colors[this.index]; }
    public String getTitle() { return titles[this.index]; }


    public WHOReading(double reading) {
        this.reading = reading;
        this.index = getIndexFromReading(this.reading);
    }


    public boolean withinRange() {
        return (index >= 0);
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
        return ranges.length;
    }


    public String getRangeFromIndex() {
        String result;
        if (index < 0) {
            Log.e(Constants.LOG_TAG, "getRangeFromIndex received index < 0.");
            result = "";
        } else if (index == 0) {
            result = "0-" + ranges[0];
        } else if (index == 3) {
            result = ranges[2] + "+";
        } else {
            result = ranges[index-1] + "-" + ranges[index];
        }
        return result;
    }

}
