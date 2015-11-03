package org.cmucreatelab.tasota.airprototype.classes;

import android.os.Handler;

/**
 * Created by mike on 10/27/15.
 */
public abstract class Timer {

    final private Handler handler = new Handler();

    final private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timerExpires();
        }
    };

    public Timer(int timerInterval) {
        this.timerInterval = timerInterval;
    }

    public void startTimer() {
        stopTimer();
        handler.postDelayed(runnable, timerInterval);
    }

    public void stopTimer() {
        handler.removeCallbacks(runnable);
    }

    // length of time for the timer (in milliseconds)
    private int timerInterval;

    // actions to perform when timer expires
    public abstract void timerExpires();

}
