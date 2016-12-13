package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import android.util.Log;

import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 12/13/16.
 */

public abstract class Pm25AqiReadableValue extends AqiReadableValue {


    public double getAqiValue() {
        double aqi = 0.0;
        // round to tenths
        double micrograms = ((int)(this.getValue()*10))/10.0;
        if (micrograms < 0) {
            Log.e(Constants.LOG_TAG, "tried to convert negative Micrograms.");
            aqi = 0.0;
        } else if (micrograms < 12.0) {
            aqi = calculateLinearAqi(50.0,0.0,12.0,0.0,micrograms);
        } else if (micrograms < 35.4) {
            aqi = calculateLinearAqi(100.0,50.0,35.4,12.1,micrograms);
        } else if (micrograms < 55.4) {
            aqi = calculateLinearAqi(150.0,101.0,55.4,35.5,micrograms);
        } else if (micrograms < 150.4) {
            aqi = calculateLinearAqi(200.0,151.0,150.4,55.5,micrograms);
        } else if (micrograms < 250.4) {
            aqi = calculateLinearAqi(300.0,201.0,250.4,150.5,micrograms);
        } else if (micrograms < 350.4) {
            aqi = calculateLinearAqi(400.0,301.0,350.4,250.5,micrograms);
        } else if (micrograms < 500.4) {
            aqi = calculateLinearAqi(500.0,401.0,500.4,350.5,micrograms);
        } else {
            Log.e(Constants.LOG_TAG, "Micrograms out of range.");
            aqi = 0.0;
        }
        return aqi;
    }

}
