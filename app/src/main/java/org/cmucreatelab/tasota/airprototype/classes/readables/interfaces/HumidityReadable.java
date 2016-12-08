package org.cmucreatelab.tasota.airprototype.classes.readables.interfaces;

import org.cmucreatelab.tasota.airprototype.classes.channels.HumidityChannel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

import java.util.ArrayList;

/**
 * Created by mike on 12/6/16.
 */

public interface HumidityReadable {

    // returns a list of channels that influence the readable value
    public ArrayList<HumidityChannel> getHumidityChannels();

    // return true if the Readable object has a Humidity value
    public boolean hasReadableHumidityValue();

    // return Humidity value
    public ReadableValue getReadableHumidityValue();

}
