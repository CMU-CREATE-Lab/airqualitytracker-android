package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.classes.channels.HumidityChannel;

/**
 * Created by mike on 12/9/16.
 */

public class HumidityValue implements ReadableValue {

    private final double value;
    private final HumidityChannel channel;


    public HumidityValue(double value, HumidityChannel channel) {
        this.value = value;
        this.channel = channel;
    }


    @Override
    public Channel getChannel() {
        return channel;
    }


    @Override
    public String getReadableUnits() {
        return "%";
    }


    @Override
    public double getValue() {
        return value;
    }

}
