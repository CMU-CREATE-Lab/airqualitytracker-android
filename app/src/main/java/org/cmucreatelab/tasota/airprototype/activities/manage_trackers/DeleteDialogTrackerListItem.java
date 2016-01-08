package org.cmucreatelab.tasota.airprototype.activities.manage_trackers;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.SpeckDbHelper;

import java.util.ArrayList;

/**
 * Created by mike on 6/8/15.
 */
public class DeleteDialogTrackerListItem {

    private AlertDialog alertDialog;
    public final ManageTrackersActivity activityContext;
    public TrackersAdapter.TrackerListItem item;

    private class AlertDialogBuilder extends AlertDialog.Builder {
        public AlertDialogBuilder(final ManageTrackersActivity activityContext) {
            super(activityContext);

            this.setTitle(item.readable.getName());
            if (item.readable.getReadableType() == Readable.Type.ADDRESS) {
                this.setMessage("Remove this Address from your list?");
            } else if (item.readable.getReadableType() == Readable.Type.SPECK) {
                this.setMessage("Remove this Speck from your list?");
            }
            this.setNegativeButton("Cancel", null);
            this.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.v(Constants.LOG_TAG, "Clicked 'remove' on DeleteDialogTrackerListItem.");
                    GlobalHandler globalHandler = GlobalHandler.getInstance(activityContext);
                    globalHandler.readingsHandler.removeReading(item.readable);
                    switch (item.readable.getReadableType()) {
                        case SPECK:
                            Speck speck = (Speck)item.readable;
                            SpeckDbHelper.destroy(speck, activityContext);
                            globalHandler.settingsHandler.addToBlacklistedDevices(speck.getDeviceId());
                            break;
                        case ADDRESS:
                            AddressDbHelper.destroy((SimpleAddress) item.readable, activityContext);
                            break;
                        default:
                            Log.e(Constants.LOG_TAG, "Unknown Readable type.");
                            break;
                    }

                    ArrayList<TrackersAdapter.TrackerListItem> list = GlobalHandler.getInstance(activityContext).readingsHandler.trackerList;
                    TrackersAdapter adapter = new TrackersAdapter(activityContext,list);
                    activityContext.listViewTrackers.setCheeseList(list);
                    activityContext.listViewTrackers.setAdapter(adapter);
                }
            });
        }
    }


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public DeleteDialogTrackerListItem(final ManageTrackersActivity activityContext, TrackersAdapter.TrackerListItem item) {
        this.activityContext = activityContext;
        this.item = item;
        this.alertDialog = (new AlertDialogBuilder(activityContext)).create();
    }

}
