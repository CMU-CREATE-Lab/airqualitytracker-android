package org.cmucreatelab.tasota.airprototype.classes.aqi_scales;

/**
 * Created by mike on 6/8/16.
 */
public abstract class Scalable {

    public abstract boolean withinRange();

    public abstract int getIndexFromReading(double reading);

    public abstract String getRangeFromIndex();

    public abstract String getColor();

    public abstract String getTitle();

}
