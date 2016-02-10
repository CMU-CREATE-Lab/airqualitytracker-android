package org.cmucreatelab.tasota.airprototype.classes;

import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import java.util.ArrayList;

/**
 * Created by mike on 2/10/16.
 */
public abstract class AirNowReadable implements Readable {


    // Readable implementation (as abstract methods)

    public abstract Type getReadableType();
    public abstract String getName();
    public abstract boolean hasReadableValue();
    public abstract double getReadableValue();


    // attributes/methods for AirNow-related calculations

    protected Location location;
    // ASSERT: list is sorted from newest to oldest
    final protected ArrayList<AirNowObservation> airNowObservations = new ArrayList<>();

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public ArrayList<AirNowObservation> getAirNowObservations() {
        return airNowObservations;
    }


    public AirNowObservation getMostRecentAirNowObservation() {
        return airNowObservations.get(0);
    }


    public void sortAirNowObservations() {
        // TODO sort list from newest to oldest; should call when adding new objects to list
    }

}
