package org.cmucreatelab.tasota.airprototype.activities.options_menu.manage_trackers;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 6/8/15.
 */
public class ManageTrackersEditDialog {

    private AlertDialog alertDialog;
    public final ManageTrackersActivity activityContext;
    public ManageTrackersAdapter.TrackerListItem item;

    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            String input = ((EditText)alertDialog.findViewById(R.id.editDialogInputText)).getText().toString();
            Log.v(Constants.LOG_TAG, "Saved string " + input + " on ManageTrackersDeleteDialog.");
            GlobalHandler.getInstance(activityContext).readingsHandler.renameReading(item.readable,input);

            ArrayList<ManageTrackersAdapter.TrackerListItem> list = GlobalHandler.getInstance(activityContext).readingsHandler.trackerList;
            ManageTrackersAdapter adapter = new ManageTrackersAdapter(activityContext,list);
            activityContext.uiElements.listViewTrackers.setList(list);
            activityContext.uiElements.listViewTrackers.setAdapter(adapter);
        }
    };

    private class AlertDialogBuilder extends AlertDialog.Builder {
        public AlertDialogBuilder(final ManageTrackersActivity activityContext) {
            super(activityContext);

            this.setView(R.layout.edit_dialog);

            if (item.readable.getReadableType() == Readable.Type.ADDRESS) {
                this.setTitle("Change Address Name");
            } else if (item.readable.getReadableType() == Readable.Type.SPECK) {
                this.setTitle("Change Speck Name");
            }
            this.setMessage(item.readable.getName());
            this.setNegativeButton("Cancel", null);
            this.setPositiveButton("Save", listener);
        }
    }


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public ManageTrackersEditDialog(final ManageTrackersActivity activityContext, ManageTrackersAdapter.TrackerListItem item) {
        this.activityContext = activityContext;
        this.item = item;
        this.alertDialog = (new AlertDialogBuilder(activityContext)).create();
    }

}
