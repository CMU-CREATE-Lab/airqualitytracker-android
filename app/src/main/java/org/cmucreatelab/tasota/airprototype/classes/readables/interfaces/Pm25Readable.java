package org.cmucreatelab.tasota.airprototype.classes.readables.interfaces;

import org.cmucreatelab.tasota.airprototype.classes.channels.Pm25Channel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

import java.util.ArrayList;

/**
 * Created by mike on 11/30/16.
 */

public interface Pm25Readable {

    // returns a list of channels that influence the readable value
    public ArrayList<Pm25Channel> getPm25Channels();

    // return true if the Readable object has a PM2.5 value
    public boolean hasReadablePm25Value();

    // return PM2.5 value
    public ReadableValue getReadablePm25Value();

}
