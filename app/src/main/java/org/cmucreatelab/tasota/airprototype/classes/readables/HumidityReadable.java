package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

/**
 * Created by mike on 12/6/16.
 */

public interface HumidityReadable {

    // return true if the Readable object has a Humidity value
    public boolean hasReadableHumidityValue();

    // return Humidity value
    public ReadableValue getReadableHumidityValue();

}
