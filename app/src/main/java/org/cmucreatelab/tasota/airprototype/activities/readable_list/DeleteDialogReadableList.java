package org.cmucreatelab.tasota.airprototype.activities.readable_list;

import android.content.Context;
import android.content.DialogInterface;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.SpeckDbHelper;

/**
 * Created by mike on 6/8/15.
 */
public class DeleteDialogReadableList {

    private StickyGridAdapter.LineItem lineItemToBeDeleted;
    private AlertDialog alertDialog;

    private class AlertDialogBuilder extends AlertDialog.Builder {
        public AlertDialogBuilder(final ReadableListActivity activityContext, final StickyGridAdapter.LineItem lineItem) {
            super(activityContext);

            final Context context = activityContext.getApplicationContext();
            this.setMessage("Remove this from your list?");
            this.setNegativeButton("Cancel", null);
            this.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    GlobalHandler globalHandler = GlobalHandler.getInstance(context);
                    globalHandler.headerReadingsHashMap.removeReading(lineItem.readable);
                    switch (lineItem.readable.getReadableType()) {
                        case SPECK:
                            Speck speck = (Speck)lineItem.readable;
                            SpeckDbHelper.destroy(speck, context);
                            GlobalHandler.getInstance(context).settingsHandler.addToBlacklistedDevices(speck.getDeviceId());
                            break;
                        case ADDRESS:
                            AddressDbHelper.destroy((SimpleAddress) lineItem.readable, context);
                            break;
                        default:
                            Log.e(Constants.LOG_TAG, "Unknown Readable type.");
                            break;
                    }
                    globalHandler.notifyGlobalDataSetChanged();
                }
            });
        }
    }


    public StickyGridAdapter.LineItem getLineItemToBeDeleted() {
        return lineItemToBeDeleted;
    }


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public DeleteDialogReadableList(final ReadableListActivity activityContext, final StickyGridAdapter.LineItem lineItem) {
        this.lineItemToBeDeleted = lineItem;
        this.alertDialog = (new AlertDialogBuilder(activityContext,this.lineItemToBeDeleted)).create();
    }

}
