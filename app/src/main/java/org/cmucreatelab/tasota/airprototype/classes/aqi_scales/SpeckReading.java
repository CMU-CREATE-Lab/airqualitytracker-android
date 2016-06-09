package org.cmucreatelab.tasota.airprototype.classes.aqi_scales;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 6/9/16.
 */
public class SpeckReading extends Scalable {

    // static class attributes
    // TODO descriptions for Speck
    private static final String[] descriptions = {
            "Air quality is considered satisfactory, and air pollution poses little or no risk.",
            "Air quality is acceptable; however, for some pollutants there may be a moderate " +
                    "health concern for a very small number of people. For example, people " +
                    "who are unusually sensitive to ozone may experience respiratory symptoms.",
            "Although general public is not likely to be affected at this AQI range, people " +
                    "with lung disease, older adults and children are at a greater risk from " +
                    "exposure to ozone, whereas persons with heart and lung disease, older " +
                    "adults and children are at greater risk from the presence of particles " +
                    "in the air.",
            "Everyone may begin to experience some adverse health effects, and members of the " +
                    "sensitive groups may experience more serious effects.",
            "This would trigger a health alert signifying that everyone may experience more " +
                    "serious health effects.",
            "This would trigger a health warning of emergency conditions. The entire " +
                    "population is more likely to be affected."
    };
    private static final String[] normalColors = {
            "#1a9850", "#91cf60", "#d9ef8b",
            "#FEE08B", "#FC8D59", "#D73027"
    };
    private static final String[] titles = {
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
    public String getDescription() { return descriptions[this.index]; }


    public SpeckReading(double reading) {
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
        return -1;
    }


    public String getRangeFromIndex() {
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
