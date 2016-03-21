package org.cmucreatelab.tasota.airprototype.classes.timers;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.ReadableListActivity;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 11/3/15.
 */
public class RefreshTimer extends Timer {

    private ReadableListActivity activity;
    public boolean isStarted = false;


    public RefreshTimer(ReadableListActivity activity, int timerInterval) {
        super(timerInterval);
        this.activity = activity;
    }


    @Override
    public void stopTimer() {
        super.stopTimer();
        isStarted = false;
    }


    @Override
    public void startTimer() {
        super.startTimer();
        isStarted = true;
    }


    @Override
    public void timerExpires() {
        Log.i(Constants.LOG_TAG, "RefreshTimer expired");
        isStarted = false;
        GlobalHandler.getInstance(activity).updateReadings();
        // repeating timer (only repeats while active)
        if (activity.activityIsActive)
            startTimer();
    }

}
