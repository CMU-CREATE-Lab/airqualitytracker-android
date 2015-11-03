package org.cmucreatelab.tasota.airprototype.classes;

import android.location.Address;
import android.location.Geocoder;
import org.cmucreatelab.tasota.airprototype.activities.address_search.AddressSearchActivity;
import java.util.List;
import java.util.Locale;

/**
 * Created by mike on 11/3/15.
 */
public class AutocompleteTimer extends Timer {

    private AddressSearchActivity activity;


    public AutocompleteTimer(AddressSearchActivity activity, int timerInterval) {
        super(timerInterval);
        this.activity = activity;
    }


    @Override
    public void timerExpires() {
        Geocoder geocoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
        try {
            List<Address> results = geocoder.getFromLocationName(activity.searchText.toString(), 5);
            activity.listAdapter.clear();
            for (Address address : results) {
                activity.listAdapter.add(address);
            }
            activity.listAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
