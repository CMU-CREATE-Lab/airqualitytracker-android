package org.cmucreatelab.tasota.airprototype.activities.options_menu.manage_trackers;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.system.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.system.database.SpeckDbHelper;

import java.util.ArrayList;

/**
 * Created by mike on 6/8/15.
 */
public class ManageTrackersDeleteDialog {

    private AlertDialog alertDialog;
    public final ManageTrackersActivity activityContext;
    public ManageTrackersAdapter.TrackerListItem item;

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
                    Log.v(Constants.LOG_TAG, "Clicked 'remove' on ManageTrackersDeleteDialog.");
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

                    ArrayList<ManageTrackersAdapter.TrackerListItem> list = GlobalHandler.getInstance(activityContext).readingsHandler.trackerList;
                    ManageTrackersAdapter adapter = new ManageTrackersAdapter(activityContext,list);
                    activityContext.uiElements.listViewTrackers.setList(list);
                    activityContext.uiElements.listViewTrackers.setAdapter(adapter);
                }
            });
        }
    }


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public ManageTrackersDeleteDialog(final ManageTrackersActivity activityContext, ManageTrackersAdapter.TrackerListItem item) {
        this.activityContext = activityContext;
        this.item = item;
        this.alertDialog = (new AlertDialogBuilder(activityContext)).create();
    }

}
