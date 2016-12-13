package org.cmucreatelab.tasota.airprototype.classes.aqi_scales;

/**
 * Created by mike on 6/9/16.
 */
public class WHOReading {

    // static class attributes
    private static final String[] colors = {
            "#a3ba5c", "#e9b642", "#e98c37",
            "#e24f36", "#b54382", "#b22651"
    };
    private static final String[] titles = {
            "Good", "Moderate", "Elevated", "High", "Very High", "Hazardous"
    };
    // ranges measured in ug/m^3
    private static final double[] ranges = {
            10.1, 25.5, 50.5, 149.5, 249.5
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

}
