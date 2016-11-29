package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

/**
 * Created by mike on 8/14/15.
 */
public interface Readable {

    public enum Type {
        ADDRESS, FEED, SPECK
    }

    // returns the Type that the Readable object is
    public Type getReadableType();

    // returns a human-readable name associated with the Reading
    public String getName();

    // return true if the Readable object has a value
    public boolean hasReadableValue();

    // return a value (should be 0.0 by default but verified with hasReadableValue)
    public ReadableValue getReadableValue();

}
