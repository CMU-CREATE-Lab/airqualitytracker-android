package org.cmucreatelab.tasota.airprototype.activities.options_menu.manage_trackers;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.BaseActivity;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;


public class ManageTrackersActivity extends BaseActivity<ManageTrackersUIElements> {

    protected ManageTrackersUIElements uiElements;
    private ManageTrackersDeleteDialog deleteDialog;
    private ManageTrackersEditDialog editDialog;


    public void showDeleteDialog(ManageTrackersAdapter.TrackerListItem trackerListItem) {
        deleteDialog = new ManageTrackersDeleteDialog(this, trackerListItem);
        deleteDialog.getAlertDialog().show();
    }


    public void showEditDialog(ManageTrackersAdapter.TrackerListItem trackerListItem) {
        editDialog = new ManageTrackersEditDialog(this, trackerListItem);
        editDialog.getAlertDialog().show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__options_menu____manage_trackers__activity);

        uiElements = new ManageTrackersUIElements(this);
        uiElements.populate();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.v(Constants.LOG_TAG, "ManageTrackersActivity onRestoreInstanceState");
        if (savedInstanceState.getBoolean("deleteDialog")) {
            int index = savedInstanceState.getInt("deleteDialogReadableIndex");
            ManageTrackersAdapter.TrackerListItem item = GlobalHandler.getInstance(getApplicationContext()).readingsHandler.trackerList.get(index);
            showDeleteDialog(item);
        }
        if (savedInstanceState.getBoolean("editDialog")) {
            String input = savedInstanceState.getString("editDialogString");
            int index = savedInstanceState.getInt("editDialogReadableIndex");
            ManageTrackersAdapter.TrackerListItem item = GlobalHandler.getInstance(getApplicationContext()).readingsHandler.trackerList.get(index);
            showEditDialog(item);
            ((EditText)editDialog.getAlertDialog().findViewById(R.id.editDialogInputText)).setText(input);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (deleteDialog != null && deleteDialog.getAlertDialog().isShowing()) {
            outState.putBoolean("deleteDialog", true);
            outState.putInt("deleteDialogReadableIndex", GlobalHandler.getInstance(getApplicationContext()).
                    readingsHandler.trackerList.indexOf(deleteDialog.item));
            deleteDialog.getAlertDialog().dismiss();
        }
        if (editDialog != null && editDialog.getAlertDialog().isShowing()) {
            outState.putBoolean("editDialog", true);
            String input = ((EditText)editDialog.getAlertDialog().findViewById(R.id.editDialogInputText)).getText().toString();
            outState.putString("editDialogString", input);
            outState.putInt("editDialogReadableIndex", GlobalHandler.getInstance(getApplicationContext()).
                    readingsHandler.trackerList.indexOf(editDialog.item));
            editDialog.getAlertDialog().dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_trackers, menu);
        return true;
    }

}
