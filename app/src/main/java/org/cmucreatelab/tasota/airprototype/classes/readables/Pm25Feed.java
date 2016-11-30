package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

/**
 * Created by mike on 11/30/16.
 */

public abstract class Pm25Feed extends Feed implements Pm25Readable {

    // class attributes
    private ReadableValue pm25ReadableValue;
    // getters/setters
    public void setPm25ReadableValue(ReadableValue readableValue) { this.pm25ReadableValue = readableValue; }


    // Pm25Readable implementation


    @Override
    public boolean hasReadablePm25Value() {
        return (pm25ReadableValue != null);
    }


    @Override
    public ReadableValue getReadablePm25Value() {
        return pm25ReadableValue;
    }

}
