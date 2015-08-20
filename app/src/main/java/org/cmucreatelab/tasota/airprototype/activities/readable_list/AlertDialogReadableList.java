package org.cmucreatelab.tasota.airprototype.activities.readable_list;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;

/**
 * Created by mike on 6/8/15.
 */
public class AlertDialogReadableList {

    private StickyGridAdapter.LineItem lineItemToBeDeleted;
    private AlertDialog alertDialog;
    public StickyGridAdapter.LineItem getLineItemToBeDeleted() {
        return lineItemToBeDeleted;
    }
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    private class AlertDialogBuilder extends AlertDialog.Builder {
        public AlertDialogBuilder(final ReadableListActivity activityContext, final StickyGridAdapter.LineItem lineItem) {
            super(activityContext);
            final Context ctx;
            ctx = activityContext.getApplicationContext();
            this.setMessage("Remove this Address from your list?");
            this.setPositiveButton("Erase", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    GlobalHandler.getInstance(ctx).headerReadingsHashMap.removeReading(lineItem.readable);
                    switch (lineItem.readable.getReadableType()) {
                        case SPECK:
                            // TODO speck delete
                            break;
                        case ADDRESS:
                            AddressDbHelper.destroy((SimpleAddress)lineItem.readable, ctx);
                            break;
                        default:
                            Log.e(Constants.LOG_TAG,"Unknown Readable type.");
                    }
                    GlobalHandler.getInstance(activityContext).notifyGlobalDataSetChanged();
                }
            });
            this.setNegativeButton("Cancel",null);
        }
    }


    public AlertDialogReadableList(final ReadableListActivity activityContext, final StickyGridAdapter.LineItem lineItem) {
        this.lineItemToBeDeleted = lineItem;
        this.alertDialog = (new AlertDialogBuilder(activityContext,this.lineItemToBeDeleted)).create();
    }

}
