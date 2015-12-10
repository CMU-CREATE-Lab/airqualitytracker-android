package org.cmucreatelab.tasota.airprototype.classes;

import org.cmucreatelab.tasota.airprototype.activities.readable_list.ReadableListActivity;

/**
 * Created by mike on 11/3/15.
 */
public class TwoFingerLongPressTimer extends Timer {

    private ReadableListActivity activity;


    public TwoFingerLongPressTimer(ReadableListActivity activity, int timerInterval) {
        super(timerInterval);
        this.activity = activity;
    }


    @Override
    public void timerExpires() {
        stopTimer();
        activity.openDebugDialog();
    }

}
