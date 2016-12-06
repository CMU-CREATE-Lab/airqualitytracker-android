package org.cmucreatelab.tasota.airprototype.classes.readables.interfaces;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

/**
 * Created by mike on 11/30/16.
 */

public interface Pm25Readable {

    // return true if the Readable object has a PM2.5 value
    public boolean hasReadablePm25Value();

    // return PM2.5 value
    public ReadableValue getReadablePm25Value();

}
