package org.cmucreatelab.tasota.airprototype.activities.options_menu.manage_trackers;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;
import listviewdragginganimation.DynamicListView;

/**
 * Created by mike on 3/22/16.
 */
public class ManageTrackersUIElements {

    private ManageTrackersActivity activity;
    protected DynamicListView listViewTrackers;
    private CheckBox checkBoxCurrentLocation;


    public ManageTrackersUIElements(ManageTrackersActivity activity) {
        this.activity = activity;
    }


    public void populate() {
        final GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
        ArrayList<ManageTrackersAdapter.TrackerListItem> list = globalHandler.readingsHandler.trackerList;

        listViewTrackers = (DynamicListView)activity.findViewById(R.id.listViewTrackers);
        checkBoxCurrentLocation = (CheckBox)activity.findViewById(R.id.checkBoxCurrentLocation);
        checkBoxCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = checkBoxCurrentLocation.isChecked();
                Log.i(Constants.LOG_TAG, "Clicked checkbox: now set to " + isChecked);
                globalHandler.settingsHandler.setAppUsesLocation(isChecked);
            }
        });
        checkBoxCurrentLocation.setChecked(globalHandler.settingsHandler.appUsesLocation());

        ManageTrackersAdapter adapter = new ManageTrackersAdapter(activity,list);
        listViewTrackers.setCheeseList(list);
        listViewTrackers.setAdapter(adapter);
        listViewTrackers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

}
