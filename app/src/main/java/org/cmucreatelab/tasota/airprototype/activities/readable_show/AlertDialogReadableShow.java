package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import org.cmucreatelab.tasota.airprototype.classes.Feed;

/**
 * Created by mike on 6/8/15.
 */
public class AlertDialogReadableShow {

    private class AlertDialogBuilder extends AlertDialog.Builder {
        public AlertDialogBuilder(final Context context, Feed feed) {
            super(context);
            this.setTitle(feed.getName());
            this.setMessage("Latitude: "+feed.getLocation().latitude+"\nLongitude: "+feed.getLocation().longitude);
        }
    }

    private AlertDialog alertDialog;
    public final Context context;


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public AlertDialogReadableShow(final Context context, Feed feed) {
        this.context = context;
        this.alertDialog = (new AlertDialogBuilder(context,feed)).create();
    }

}
