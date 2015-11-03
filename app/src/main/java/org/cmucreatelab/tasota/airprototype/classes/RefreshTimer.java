package org.cmucreatelab.tasota.airprototype.classes;

/**
 * Created by mike on 11/3/15.
 */
public class RefreshTimer extends Timer {


    public RefreshTimer(int timerInterval) {
        super(timerInterval);
    }


    @Override
    public void timerExpires() {
        // TODO add code for refresh calls to esdr
    }

}
