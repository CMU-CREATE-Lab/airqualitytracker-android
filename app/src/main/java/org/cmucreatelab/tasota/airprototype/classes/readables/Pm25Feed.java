package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Pm25Readable;

/**
 * Created by mike on 11/30/16.
 */

public abstract class Pm25Feed extends Feed implements Pm25Readable {

    // class attributes
    private ReadableValue readablePm25Value;
    // getters/setters
    public void setReadablePm25Value(ReadableValue readableValue) { this.readablePm25Value = readableValue; }


    // Pm25Readable implementation


    @Override
    public boolean hasReadablePm25Value() {
        return (readablePm25Value != null);
    }


    @Override
    public ReadableValue getReadablePm25Value() {
        return readablePm25Value;
    }

}
