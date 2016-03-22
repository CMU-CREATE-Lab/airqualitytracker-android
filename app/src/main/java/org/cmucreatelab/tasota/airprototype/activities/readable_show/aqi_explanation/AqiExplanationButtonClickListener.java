package org.cmucreatelab.tasota.airprototype.activities.readable_show.aqi_explanation;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import org.cmucreatelab.tasota.airprototype.activities.readable_show.air_now.AirNowActivity;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 3/22/16.
 */
public class AqiExplanationButtonClickListener implements View.OnClickListener {

    private AqiExplanationActivity activity;


    public AqiExplanationButtonClickListener(AqiExplanationActivity activity) {
        this.activity = activity;
    }


    @Override
    public void onClick(View view) {
        Log.i(Constants.LOG_TAG, "clicked buttonAirNow");
        activity.startActivity(new Intent(activity.getApplicationContext(), AirNowActivity.class));
    }

}
