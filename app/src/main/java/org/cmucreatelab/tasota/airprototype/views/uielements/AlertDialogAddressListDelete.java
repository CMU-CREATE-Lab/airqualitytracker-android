package org.cmucreatelab.tasota.airprototype.views.uielements;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressListActivity;

/**
 * Created by mike on 6/8/15.
 */
public class AlertDialogAddressListDelete {

    private SimpleAddress addressToBeDeleted;
    private AlertDialog alertDialog;
    public SimpleAddress getAddressToBeDeleted() {
        return addressToBeDeleted;
    }
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public AlertDialogAddressListDelete(final AddressListActivity activityContext, final SimpleAddress simpleAddress) {
        this.addressToBeDeleted = simpleAddress;
        this.alertDialog = (new AlertDialogBuilderAddressListDelete(activityContext,this.addressToBeDeleted)).create();
    }


    private class AlertDialogBuilderAddressListDelete extends AlertDialog.Builder {
        public AlertDialogBuilderAddressListDelete(final AddressListActivity activityContext, final SimpleAddress simpleAddress) {
            super(activityContext);
            final Context ctx;
            ctx = activityContext.getApplicationContext();
            this.setMessage("Remove this Address from your list?");
            this.setPositiveButton("Erase", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    GlobalHandler.getInstance(ctx).removeAddress(simpleAddress);
                    simpleAddress.destroy(ctx);
                    activityContext.listAdapter.notifyDataSetChanged();
                }
            });
            this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // does nothing
                }
            });
        }
    }

}