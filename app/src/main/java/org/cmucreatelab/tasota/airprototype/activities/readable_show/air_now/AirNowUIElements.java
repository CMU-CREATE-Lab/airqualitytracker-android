package org.cmucreatelab.tasota.airprototype.activities.readable_show.air_now;

import android.widget.ListView;
import org.cmucreatelab.tasota.airprototype.R;

/**
 * Created by mike on 3/22/16.
 */
public class AirNowUIElements {

    private AirNowActivity activity;
    private ListView listViewAirNow;


    public AirNowUIElements(AirNowActivity activity) {
        this.activity = activity;
    }


    public void populate() {
        // array adapter stuff
        this.listViewAirNow = (ListView)activity.findViewById(R.id.listViewAirNow);
        listViewAirNow.setAdapter(activity.airNowAdapter);
    }

}
