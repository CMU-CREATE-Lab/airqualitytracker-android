package org.cmucreatelab.tasota.airprototype.classes.readables.interfaces;

import org.cmucreatelab.tasota.airprototype.classes.channels.Pm25Channel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.AqiReadableValue;
import java.util.ArrayList;

/**
 * Created by mike on 11/30/16.
 */

public interface Pm25Readable {

    // returns a list of channels that influence the readable value
    ArrayList<Pm25Channel> getPm25Channels();

    // return true if the Readable object has a PM2.5 value
    boolean hasReadablePm25Value();

    // return PM2.5 value
    AqiReadableValue getReadablePm25Value();

}
