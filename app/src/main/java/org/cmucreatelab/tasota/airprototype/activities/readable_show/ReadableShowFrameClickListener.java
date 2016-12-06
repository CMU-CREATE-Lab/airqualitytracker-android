package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import org.cmucreatelab.tasota.airprototype.activities.readable_show.aqi_explanation.AqiExplanationActivity;
import org.cmucreatelab.tasota.airprototype.classes.readables.*;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 3/22/16.
 */
public class ReadableShowFrameClickListener implements View.OnClickListener {

    private ReadableShowActivity activity;


    public ReadableShowFrameClickListener(ReadableShowActivity activity) {
        this.activity = activity;
    }


    @Override
    public void onClick(View view) {
        Log.i(Constants.LOG_TAG, "clicked frameAqiButton");
        // TODO sending Readable via GlobalHandler but should be handled through the activities
        Readable readable = activity.reading;
        if (readable instanceof AirNowReadable) {
            GlobalHandler.getInstance(activity).readableShowToAirNow = (AirNowReadable)readable;
            activity.startActivity(new Intent(activity, AqiExplanationActivity.class));
        } else {
            Log.e(Constants.LOG_TAG, "ERROR - reading is not an instance of AirNowReadable");
        }
    }

}
