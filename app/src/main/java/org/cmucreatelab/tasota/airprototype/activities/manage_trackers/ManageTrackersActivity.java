package org.cmucreatelab.tasota.airprototype.activities.manage_trackers;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;
import listviewdragginganimation.DynamicListView;


public class ManageTrackersActivity extends ActionBarActivity {

    protected DynamicListView listViewTrackers;

    private DeleteDialogTrackerListItem deleteDialog;
    private EditDialogTrackerListItem editDialog;
    private CheckBox checkBoxCurrentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__trackers__manage_trackers);

        final GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        ArrayList<TrackersAdapter.TrackerListItem> list = globalHandler.readingsHandler.trackerList;

        listViewTrackers = (DynamicListView)findViewById(R.id.listViewTrackers);
        checkBoxCurrentLocation = (CheckBox)findViewById(R.id.checkBoxCurrentLocation);

        checkBoxCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = checkBoxCurrentLocation.isChecked();
                Log.i(Constants.LOG_TAG, "Clicked checkbox: now set to " + isChecked);
                globalHandler.settingsHandler.setAppUsesLocation(isChecked);
            }
        });
        checkBoxCurrentLocation.setChecked(globalHandler.settingsHandler.appUsesLocation());

        TrackersAdapter adapter = new TrackersAdapter(this,list);
        listViewTrackers.setCheeseList(list);
        listViewTrackers.setAdapter(adapter);
        listViewTrackers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void showDeleteDialog(TrackersAdapter.TrackerListItem trackerListItem) {
        deleteDialog = new DeleteDialogTrackerListItem(this, trackerListItem);
        deleteDialog.getAlertDialog().show();
    }

    public void showEditDialog(TrackersAdapter.TrackerListItem trackerListItem) {
        editDialog = new EditDialogTrackerListItem(this, trackerListItem);
        editDialog.getAlertDialog().show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // TODO restore objects
        Log.v(Constants.LOG_TAG, "ManageTrackersActivity onRestoreInstanceState");
        if (savedInstanceState.getBoolean("deleteDialog")) {
            int index = savedInstanceState.getInt("deleteDialogReadableIndex");
            TrackersAdapter.TrackerListItem item = GlobalHandler.getInstance(getApplicationContext()).readingsHandler.trackerList.get(index);
            showDeleteDialog(item);
        }
        if (savedInstanceState.getBoolean("editDialog")) {
            String input = savedInstanceState.getString("editDialogString");
            int index = savedInstanceState.getInt("editDialogReadableIndex");
            TrackersAdapter.TrackerListItem item = GlobalHandler.getInstance(getApplicationContext()).readingsHandler.trackerList.get(index);
            showEditDialog(item);
            ((EditText)editDialog.getAlertDialog().findViewById(R.id.editDialogInputText)).setText(input);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // TODO save objects
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
