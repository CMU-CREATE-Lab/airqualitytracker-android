package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import android.util.Log;

import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

import java.util.Arrays;

/**
 * Created by mike on 11/22/16.
 */

public class Ozone_NowCast extends AqiReadableValue {

    private final double value;
    private final Channel channel;


    public Ozone_NowCast(double value, Channel channel) {
        // check if we are actually using PPB instead of PPM
        if (Arrays.asList(Constants.ppbOzoneNames).contains(channel.getName())) {
            this.value = value/1000.0;
        } else {
            this.value = value;
        }
        this.channel = channel;
    }


    public double getAqiValue() {
        double aqi = 0.0;
        // EPA caluclation rounds to a whole PPB (and we have PPM)
        double ppm = ((int)(this.getValue()*1000))/1000.0;
        if (ppm < 0) {
            Log.e(Constants.LOG_TAG, "tried to convert negative PPM.");
            aqi = 0.0;
        } else if (ppm < 0.054) {
            aqi = calculateLinearAqi(50.0, 0.0, 0.054, 0.0, ppm);
        } else if (ppm < 0.070) {
            aqi = calculateLinearAqi(100.0, 51.0, 0.070, 0.055, ppm);
        } else if (ppm < 0.085) {
            aqi = calculateLinearAqi(150.0, 101.0, 0.085, 0.071, ppm);
        } else if (ppm < 0.105) {
            aqi = calculateLinearAqi(200.0, 151.0, 0.105, 0.086, ppm);
        } else if (ppm < 0.200) {
            aqi = calculateLinearAqi(300.0, 201.0, 0.200, 0.106, ppm);
        } else {
            Log.e(Constants.LOG_TAG, "PPM out of range.");
            aqi = 0.0;
        }
        return aqi;
    }


    @Override
    public Channel getChannel() {
        return channel;
    }


    @Override
    public String getReadableUnits() {
        return Constants.Units.PARTS_PER_MILLION;
    }


    @Override
    public double getValue() {
        return value;
    }

}
