package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.content.Intent;
import android.view.View;
import org.cmucreatelab.tasota.airprototype.activities.readable_show.daily_tracker.DailyTrackerActivity;

/**
 * Created by mike on 3/22/16.
 */
public class ReadableShowTrackerFrameClickListener implements View.OnClickListener {

    private ReadableShowActivity activity;
    public boolean isEnabled = false;


    public ReadableShowTrackerFrameClickListener(ReadableShowActivity activity) {
        this.activity = activity;
    }


    @Override
    public void onClick(View view) {
        if (isEnabled)
            activity.startActivity(new Intent(activity, DailyTrackerActivity.class));
    }

}
