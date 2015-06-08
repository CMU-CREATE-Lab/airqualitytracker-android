package org.cmucreatelab.tasota.airprototype.views.uielements;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressListActivity;

/**
 * Created by mike on 6/8/15.
 */
public class AlertDialogAddressListDelete {

    private Address addressToBeDeleted;
    private AlertDialog alertDialog;
    public Address getAddressToBeDeleted() {
        return addressToBeDeleted;
    }
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public AlertDialogAddressListDelete(final AddressListActivity activityContext, final Address address) {
        this.addressToBeDeleted = address;
        this.alertDialog = (new AlertDialogBuilderAddressListDelete(activityContext,this.addressToBeDeleted)).create();
    }


    private class AlertDialogBuilderAddressListDelete extends AlertDialog.Builder {
        public AlertDialogBuilderAddressListDelete(final AddressListActivity activityContext, final Address address) {
            super(activityContext);
            final Context ctx;
            ctx = activityContext.getApplicationContext();
            this.setMessage("Remove this Address from your list?");
            this.setPositiveButton("Erase", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    GlobalHandler.getInstance(ctx).removeAddress(address);
                    address.destroy(ctx);
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
