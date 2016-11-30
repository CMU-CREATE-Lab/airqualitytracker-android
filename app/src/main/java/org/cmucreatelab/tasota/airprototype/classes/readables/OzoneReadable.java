package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

/**
 * Created by mike on 11/30/16.
 */

public interface OzoneReadable {

    // return true if the Readable object has an Ozone value
    public boolean hasReadableOzoneValue();

    // return Ozone value
    public ReadableValue getReadableOzoneValue();

}
