package org.cmucreatelab.tasota.airprototype.classes.readables;

import org.cmucreatelab.tasota.airprototype.classes.AirNowObservation;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

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


    // TODO multiple values sharing same timestamp
    public AirNowObservation getMostRecentAirNowObservation() {
        if (airNowObservations.size() > 0) {
            return airNowObservations.get(0);
        }
        return null;
    }


    public void appendAndSort(Collection<AirNowObservation> values) {
        class AirNowDateComparator implements Comparator<AirNowObservation> {

            @Override
            public int compare(AirNowObservation a, AirNowObservation b) {
                return a.getObservedDatetime().compareTo(b.getObservedDatetime());
            }
        }

        airNowObservations.addAll(values);
        Collections.sort(airNowObservations, new AirNowDateComparator());
    }

}
