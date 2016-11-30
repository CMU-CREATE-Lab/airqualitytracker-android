package org.cmucreatelab.tasota.airprototype.classes.readables;

import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.activities.readable_show.air_now.AirNowActivity;
import org.cmucreatelab.tasota.airprototype.classes.AirNowObservation;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers.AirNowJsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by mike on 2/10/16.
 */
public abstract class AirNowReadable implements Readable {

    // class attributes
    protected Location location;
    final protected ArrayList<AirNowObservation> airNowObservations = new ArrayList<>(); // ASSERT: list is sorted from newest to oldest
    // getters/setters
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public ArrayList<AirNowObservation> getAirNowObservations() { return airNowObservations; }
    // abstract methods (Readable)
    public abstract Type getReadableType();
    public abstract String getName();
    public abstract boolean hasReadableValue();
    public abstract List<ReadableValue> getReadableValues();


    public ArrayList<AirNowObservation> getMostRecentAirNowObservations() {
        ArrayList<AirNowObservation> values = new ArrayList<>();

        if (airNowObservations.size() > 0) {
            Date date = airNowObservations.get(0).getObservedDatetime();
            for (AirNowObservation observation: airNowObservations) {
                if (observation.getObservedDatetime().compareTo(date) == 0) {
                    values.add(observation);
                }
            }
        }

        return values;
    }


    public void requestAirNow(final AirNowActivity context) {
        Response.Listener<JSONArray> response = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<AirNowObservation> results = AirNowJsonParser.parseObservationsFromJson(response);
                appendAndSort(results);
                context.clearAndUpdateList(getMostRecentAirNowObservations());
            }
        };

        GlobalHandler.getInstance(context).airNowRequestHandler.requestAirNowObservation(this, response);
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
