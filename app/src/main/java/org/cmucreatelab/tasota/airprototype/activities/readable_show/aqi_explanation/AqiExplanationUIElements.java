package org.cmucreatelab.tasota.airprototype.activities.readable_show.aqi_explanation;

import android.widget.Button;
import org.cmucreatelab.tasota.airprototype.R;

/**
 * Created by mike on 3/22/16.
 */
public class AqiExplanationUIElements {

    private AqiExplanationActivity activity;
    private Button buttonAirNow;


    public AqiExplanationUIElements(AqiExplanationActivity activity) {
        this.activity = activity;
    }


    public void populate() {
        this.buttonAirNow = (Button)activity.findViewById(R.id.buttonAirNow);
        buttonAirNow.setOnClickListener(activity.buttonClickListener);
    }

}
