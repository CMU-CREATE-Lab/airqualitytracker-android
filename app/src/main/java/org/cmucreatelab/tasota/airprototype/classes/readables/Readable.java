package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

import java.util.List;

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

    // return true if the Readable object has any values
    public boolean hasReadableValue();

    // return list of values
    public List<ReadableValue> getReadableValues();

}
