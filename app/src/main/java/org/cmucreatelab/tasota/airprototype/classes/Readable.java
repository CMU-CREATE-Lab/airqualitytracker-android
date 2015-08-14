package org.cmucreatelab.tasota.airprototype.classes;

/**
 * Created by mike on 8/14/15.
 */
public interface Readable {
    public enum Type {
        ADDRESS, FEED, SPECK
    }
    // returns the Type that the Readable object is
    public Type getReadableType();
}
