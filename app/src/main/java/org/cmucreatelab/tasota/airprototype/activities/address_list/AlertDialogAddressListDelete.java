package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;

/**
 * Created by mike on 6/8/15.
 */
public class AlertDialogAddressListDelete {

    private Readable readingToBeDeleted;
    private AlertDialog alertDialog;
    public Readable getReadingToBeDeleted() {
        return readingToBeDeleted;
    }
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    private class AlertDialogBuilderAddressListDelete extends AlertDialog.Builder {
        public AlertDialogBuilderAddressListDelete(final AddressListActivity activityContext, final Readable readable) {
            super(activityContext);
            final Context ctx;
            ctx = activityContext.getApplicationContext();
            this.setMessage("Remove this Address from your list?");
            this.setPositiveButton("Erase", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    GlobalHandler.getInstance(ctx).headerReadingsHashMap.removeReading(readable);
                    // TODO find type (and do actions if speck)
                    switch (readable.getReadableType()) {
                        case SPECK:
                            // TODO speck delete
                            break;
                        case ADDRESS:
                            AddressDbHelper.destroy((SimpleAddress)readable, ctx);
                            break;
                        default:
                            Log.e(Constants.LOG_TAG,"Unknown Readable type.");
                    }
//                    activityContext.listAdapter.notifyDataSetChanged();
                    GlobalHandler.getInstance(activityContext).notifyGlobalDataSetChanged();
                }
            });
            this.setNegativeButton("Cancel",null);
        }
    }


    public AlertDialogAddressListDelete(final AddressListActivity activityContext, final Readable readable) {
        this.readingToBeDeleted = readable;
        this.alertDialog = (new AlertDialogBuilderAddressListDelete(activityContext,this.readingToBeDeleted)).create();
    }

}
