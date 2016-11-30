package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 11/30/16.
 */

public class AirQualityFeed extends Pm25Feed implements OzoneReadable {

    // class attributes
    private ReadableValue ozoneReadableValue;
    // getters/setters
    public void setOzoneReadableValue(ReadableValue readableValue) { this.ozoneReadableValue = readableValue; }


    // OzoneReadable implementation


    @Override
    public boolean hasReadableOzoneValue() {
        return (ozoneReadableValue != null);
    }


    @Override
    public ReadableValue getReadableOzoneValue() {
        return ozoneReadableValue;
    }


    // Readable implementation


    private ArrayList<ReadableValue> generateReadableValues() {
        ArrayList<ReadableValue> result = new ArrayList<>();
        if (hasReadablePm25Value()) {
            result.add(getReadablePm25Value());
        }
        if (hasReadableOzoneValue()) {
            result.add(ozoneReadableValue);
        }
        return result;
    }


    public boolean hasReadableValue() {
        return (generateReadableValues().size() > 0);
    }


    public List<ReadableValue> getReadableValues() {
        return generateReadableValues();
    }

}
