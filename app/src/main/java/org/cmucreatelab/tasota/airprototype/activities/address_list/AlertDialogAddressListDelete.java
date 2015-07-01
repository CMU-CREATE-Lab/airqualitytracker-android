package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;

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

    private class AlertDialogBuilderAddressListDelete extends AlertDialog.Builder {
        public AlertDialogBuilderAddressListDelete(final AddressListActivity activityContext, final SimpleAddress simpleAddress) {
            super(activityContext);
            final Context ctx;
            ctx = activityContext.getApplicationContext();
            this.setMessage("Remove this Address from your list?");
            this.setPositiveButton("Erase", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    GlobalHandler.getInstance(ctx).addressFeedsHashMap.removeAddress(simpleAddress);
                    AddressDbHelper.destroy(simpleAddress, ctx);
                    activityContext.listAdapter.notifyDataSetChanged();
                }
            });
            this.setNegativeButton("Cancel",null);
        }
    }


    public AlertDialogAddressListDelete(final AddressListActivity activityContext, final SimpleAddress simpleAddress) {
        this.addressToBeDeleted = simpleAddress;
        this.alertDialog = (new AlertDialogBuilderAddressListDelete(activityContext,this.addressToBeDeleted)).create();
    }

}
