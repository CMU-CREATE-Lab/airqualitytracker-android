package org.cmucreatelab.tasota.airprototype.classes.readables.interfaces;

import org.cmucreatelab.tasota.airprototype.classes.channels.TemperatureChannel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;
import java.util.ArrayList;

/**
 * Created by mike on 12/6/16.
 */

public interface TemperatureReadable {

    // returns a list of channels that influence the readable value
    ArrayList<TemperatureChannel> getTemperatureChannels();

    // return true if the Readable object has a Temperature value
    boolean hasReadableTemperatureValue();

    // return Temperature value
    ReadableValue getReadableTemperatureValue();

}
