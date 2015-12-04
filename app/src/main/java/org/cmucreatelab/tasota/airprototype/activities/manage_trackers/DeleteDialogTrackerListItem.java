package org.cmucreatelab.tasota.airprototype.activities.manage_trackers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.activities.readable_list.ReadableListActivity;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.SpeckDbHelper;

/**
 * Created by mike on 6/8/15.
 */
public class DeleteDialogTrackerListItem {

    private AlertDialog alertDialog;

    // TODO define Readable

    private class AlertDialogBuilder extends AlertDialog.Builder {
        public AlertDialogBuilder(final ManageTrackersActivity activityContext) {
            super(activityContext);

            this.setMessage("Remove this from your list?");
            this.setNegativeButton("Cancel", null);
            this.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.v(Constants.LOG_TAG, "Clicked 'remove' on DeleteDialogTrackerListItem.");
                }
            });
        }
    }


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public DeleteDialogTrackerListItem(final ManageTrackersActivity activityContext) {
        this.alertDialog = (new AlertDialogBuilder(activityContext)).create();
    }

}
