package org.cmucreatelab.tasota.airprototype.classes.timers;

import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.activities.address_search.AddressSearchActivity;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers.WuJsonParser;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by mike on 11/3/15.
 */
public class AutocompleteTimer extends Timer {

    // class attributes
    private AddressSearchActivity activity;
    final Response.Listener<JSONObject> completionHandler = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            activity.clearAdapter();
            ArrayList<SimpleAddress> results = WuJsonParser.parseAddressesFromJson(response);
            for (SimpleAddress address : results) {
                activity.addAddressToAdapter(address);
            }
        }
    };


    // class constructor
    public AutocompleteTimer(AddressSearchActivity activity, int timerInterval) {
        super(timerInterval);
        this.activity = activity;
    }


    @Override
    public void timerExpires() {
        GlobalHandler globalhandler = GlobalHandler.getInstance(activity.getApplicationContext());
        globalhandler.httpRequestHandler.requestGeocodingFromApi(activity.getTextFromWatcher(), completionHandler);
    }

}
