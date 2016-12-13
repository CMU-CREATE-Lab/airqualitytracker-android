package org.cmucreatelab.tasota.airprototype.classes.readable_values;

/**
 * Created by mike on 12/13/16.
 */

public abstract class AqiReadableValue implements ReadableValue {


    protected static double calculateLinearAqi(double ihi, double ilo, double chi, double clo, double units) {
        return (ihi-ilo) / (chi-clo) * (units-clo) + ilo;
    }


    // convert stored class value into AQI
    public abstract double getAqiValue();

}
