package org.cmucreatelab.tasota.airprototype.classes.readables.interfaces;

import org.cmucreatelab.tasota.airprototype.classes.channels.OzoneChannel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.AqiReadableValue;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

import java.util.ArrayList;

/**
 * Created by mike on 11/30/16.
 */

public interface OzoneReadable {

    // returns a list of channels that influence the readable value
    public ArrayList<OzoneChannel> getOzoneChannels();

    // return true if the Readable object has an Ozone value
    public boolean hasReadableOzoneValue();

    // return Ozone value
    public AqiReadableValue getReadableOzoneValue();

}
