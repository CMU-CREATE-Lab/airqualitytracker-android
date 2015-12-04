package org.cmucreatelab.tasota.airprototype.activities.manage_trackers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 6/8/15.
 */
public class EditDialogTrackerListItem {

    private AlertDialog alertDialog;

    // TODO define Readable 

    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            String input = ((EditText)alertDialog.findViewById(R.id.editDialogInputText)).getText().toString();
            Log.v(Constants.LOG_TAG, "Saved string " + input + " on DeleteDialogTrackerListItem.");
        }
    };

    private class AlertDialogBuilder extends AlertDialog.Builder {
        public AlertDialogBuilder(final ManageTrackersActivity activityContext) {
            super(activityContext);

            this.setView(R.layout.edit_dialog);

            this.setMessage("Edit Tracker");
            this.setNegativeButton("Cancel", null);
            this.setPositiveButton("Save", listener);
        }
    }


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public EditDialogTrackerListItem(final ManageTrackersActivity activityContext) {
        this.alertDialog = (new AlertDialogBuilder(activityContext)).create();
    }

}
