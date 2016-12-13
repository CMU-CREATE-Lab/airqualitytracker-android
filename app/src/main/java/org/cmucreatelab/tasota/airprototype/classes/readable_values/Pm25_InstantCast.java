package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 11/22/16.
 */

public class Pm25_InstantCast extends Pm25AqiReadableValue {

    private final double value;
    private final Channel channel;


    public Pm25_InstantCast(double value, Channel channel) {
        this.value = value;
        this.channel = channel;
    }


    @Override
    public Channel getChannel() {
        return channel;
    }


    @Override
    public String getReadableUnits() {
        return Constants.Units.MICROGRAMS_PER_CUBIC_METER;
    }


    @Override
    public double getValue() {
        return value;
    }

}
