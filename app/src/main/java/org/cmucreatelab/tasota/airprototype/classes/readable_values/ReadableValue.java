package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;

/**
 * Created by mike on 11/22/16.
 */

public interface ReadableValue {

    // returns the Channel that this value came from
    public Channel getChannel();

    // returns human-readable units that the value is measured in
    public String getReadableUnits();

    // return a value (this value should always be set)
    public double getValue();

}
