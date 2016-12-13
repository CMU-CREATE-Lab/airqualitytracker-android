package org.cmucreatelab.tasota.airprototype.classes.readables.interfaces;

import org.cmucreatelab.tasota.airprototype.classes.channels.OzoneChannel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.AqiReadableValue;
import java.util.ArrayList;

/**
 * Created by mike on 11/30/16.
 */

public interface OzoneReadable {

    // returns a list of channels that influence the readable value
    ArrayList<OzoneChannel> getOzoneChannels();

    // return true if the Readable object has an Ozone value
    boolean hasReadableOzoneValue();

    // return Ozone value
    AqiReadableValue getReadableOzoneValue();

}
