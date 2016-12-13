package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.classes.channels.TemperatureChannel;

/**
 * Created by mike on 12/9/16.
 */

public class TemperatureValue implements ReadableValue {

    private final double value;
    private final TemperatureChannel channel;


    public TemperatureValue(double value, TemperatureChannel channel) {
        this.value = value;
        this.channel = channel;
    }


    @Override
    public Channel getChannel() {
        return channel;
    }


    @Override
    public String getReadableUnits() {
        return "C";
    }


    @Override
    public double getValue() {
        return value;
    }

}
