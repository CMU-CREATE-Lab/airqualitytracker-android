package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 11/22/16.
 */

public class Pm25_InstantCast implements ReadableValue {

    private final double value;


    public Pm25_InstantCast(double value) {
        this.value = value;
    }


    @Override
    public String getReadableUnits() {
        return Constants.Units.MICROGRAMS_PER_CUBIC_METER;
    }


    @Override
    public double getValue() {
        return value;
    }

}
