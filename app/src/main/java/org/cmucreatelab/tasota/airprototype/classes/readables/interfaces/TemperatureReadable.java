package org.cmucreatelab.tasota.airprototype.classes.readables.interfaces;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

/**
 * Created by mike on 12/6/16.
 */

public interface TemperatureReadable {

    // return true if the Readable object has a Temperature value
    public boolean hasReadableTemperatureValue();

    // return Temperature value
    public ReadableValue getReadableTemperatureValue();

}
