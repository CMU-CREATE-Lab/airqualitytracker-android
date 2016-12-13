package org.cmucreatelab.tasota.airprototype.classes.readables.interfaces;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;
import java.util.List;

/**
 * Created by mike on 8/14/15.
 */
public interface Readable {

    enum Type {
        ADDRESS, FEED, SPECK
    }

    // returns the Type that the Readable object is
    Type getReadableType();

    // returns a human-readable name associated with the Reading
    String getName();

    // return true if the Readable object has any values
    boolean hasReadableValue();

    // return list of values
    List<ReadableValue> getReadableValues();

}
