package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

/**
 * Created by mike on 11/30/16.
 */

public class AirQualityFeed extends Feed implements OzoneReadable, Pm25Readable {

    private ReadableValue ozoneReadableValue, pm25ReadableValue;


    // OzoneReadable implementation


    @Override
    public boolean hasReadableOzoneValue() {
        return (ozoneReadableValue != null);
    }


    @Override
    public ReadableValue getReadableOzoneValue() {
        return ozoneReadableValue;
    }


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
