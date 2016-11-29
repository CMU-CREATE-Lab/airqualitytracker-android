package org.cmucreatelab.tasota.airprototype.classes.readable_values;

import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 11/22/16.
 */

public class Ozone_NowCast implements ReadableValue {

    private final double value;


    public Ozone_NowCast(double value) {
        this.value = value;
    }


    @Override
    public String getReadableUnits() {
        return Constants.Units.PARTS_PER_MILLION;
    }


    @Override
    public double getValue() {
        return value;
    }

}
