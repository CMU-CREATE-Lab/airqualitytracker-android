package org.cmucreatelab.tasota.airprototype.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mike on 2/18/16.
 */
public class SessionExpiredDialog {

    private AlertDialog alertDialog;

    private class AlertDialogBuilder extends AlertDialog.Builder {
        public AlertDialogBuilder(final AppCompatActivity activity) {
            super(activity);
            this.setTitle("www.specksensor.com");
            this.setMessage("Your session has timed out. Please log in.");
            this.setNegativeButton("OK", null);
        }
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public SessionExpiredDialog(final AppCompatActivity activity) {
        this.alertDialog = (new AlertDialogBuilder(activity)).create();
    }

}
