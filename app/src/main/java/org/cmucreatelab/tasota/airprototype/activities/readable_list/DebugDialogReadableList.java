package org.cmucreatelab.tasota.airprototype.activities.readable_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import org.cmucreatelab.tasota.airprototype.activities.secret_menu.DebugActivity;

/**
 * Created by mike on 6/8/15.
 */
public class DebugDialogReadableList {

    private class AlertDialogBuilder extends AlertDialog.Builder {
        public AlertDialogBuilder(final ReadableListActivity activityContext) {
            super(activityContext);

            this.setTitle("DEBUG");
            this.setMessage("View Debug Screen?");
            this.setNegativeButton("No", null);
            this.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(activity, DebugActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }

    final public ReadableListActivity activity;
    private AlertDialog alertDialog;


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public DebugDialogReadableList(final ReadableListActivity activityContext) {
        this.activity = activityContext;
        this.alertDialog = (new AlertDialogBuilder(activityContext)).create();
    }

}
