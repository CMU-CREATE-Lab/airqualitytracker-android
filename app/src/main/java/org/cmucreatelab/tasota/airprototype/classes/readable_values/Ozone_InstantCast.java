package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 11/22/16.
 */

public class Ozone_InstantCast implements ReadableValue {

    private final double value;
    private final Channel channel;


    public Ozone_InstantCast(double value, Channel channel) {
        this.value = value;
        this.channel = channel;
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
