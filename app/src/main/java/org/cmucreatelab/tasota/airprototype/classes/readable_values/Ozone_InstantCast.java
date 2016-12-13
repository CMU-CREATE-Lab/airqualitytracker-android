package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.Arrays;

/**
 * Created by mike on 11/22/16.
 */

public class Ozone_InstantCast extends AqiReadableValue {

    private final double value;
    private final Channel channel;


    public Ozone_InstantCast(double value, Channel channel) {
        // check if we are actually using PPB instead of PPM
        if (Arrays.asList(Constants.ppbOzoneNames).contains(channel.getName())) {
            this.value = value/1000.0;
        } else {
            this.value = value;
        }
        this.channel = channel;
    }


    @Override
    public double getAqiValue() {
        double aqi = 0.0;
        // EPA caluclation rounds to a whole PPB (and we have PPM)
        double ppm = ((int)(this.getValue()*1000))/1000.0;
        if (ppm < 0) {
            Log.e(Constants.LOG_TAG, "tried to convert negative PPM.");
            aqi = 0.0;
        } else if (ppm < 0.124) {
            Log.w(Constants.LOG_TAG, "InstantCast AQI for Ozone is not in 1-hr threshold; setting aqi to 0.");
            aqi = 0.0;
        } else if (ppm < 0.164) {
            aqi = calculateLinearAqi(150.0, 101.0, 0.164, 0.125, ppm);
        } else if (ppm < 0.204) {
            aqi = calculateLinearAqi(200.0, 151.0, 0.204, 0.165, ppm);
        } else if (ppm < 0.404) {
            aqi = calculateLinearAqi(300.0, 201.0, 0.404, 0.205, ppm);
        } else if (ppm < 0.504) {
            aqi = calculateLinearAqi(400.0, 301.0, 0.504, 0.405, ppm);
        } else if (ppm < 0.604) {
            aqi = calculateLinearAqi(500.0, 401.0, 0.604, 0.505, ppm);
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
