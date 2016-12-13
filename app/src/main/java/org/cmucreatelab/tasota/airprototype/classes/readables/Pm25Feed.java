package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.channels.Pm25Channel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.AqiReadableValue;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Pm25Readable;

import java.util.ArrayList;

/**
 * Created by mike on 11/30/16.
 */

public abstract class Pm25Feed extends Feed implements Pm25Readable {

    // class attributes
    private AqiReadableValue readablePm25Value;
    private final ArrayList<Pm25Channel> pm25Channels = new ArrayList<>();
    // getters/setters
    public void setReadablePm25Value(AqiReadableValue readableValue) { this.readablePm25Value = readableValue; }


    // Pm25Readable implementation


    @Override
    public ArrayList<Pm25Channel> getPm25Channels() {
        return pm25Channels;
    }


    @Override
    public boolean hasReadablePm25Value() {
        return (readablePm25Value != null);
    }


    @Override
    public AqiReadableValue getReadablePm25Value() {
        return readablePm25Value;
    }

}
