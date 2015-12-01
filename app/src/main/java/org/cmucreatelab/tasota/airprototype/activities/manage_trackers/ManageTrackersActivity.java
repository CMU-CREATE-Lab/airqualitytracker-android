package org.cmucreatelab.tasota.airprototype.activities.manage_trackers;

import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

import java.util.ArrayList;

import listviewdragginganimation.Cheeses;
import listviewdragginganimation.DynamicListView;
import listviewdragginganimation.StableArrayAdapter;


public class ManageTrackersActivity extends ActionBarActivity {

    protected DynamicListView listViewTrackers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__trackers__manage_trackers);

        ArrayList<TrackersAdapter.TrackerListItem> list = GlobalHandler.getInstance(getApplicationContext()).headerReadingsHashMap.trackerList;

        listViewTrackers = (DynamicListView)findViewById(R.id.listViewTrackers);
        TrackersAdapter adapter = new TrackersAdapter(this,list);

        listViewTrackers.setCheeseList(list);
        listViewTrackers.setAdapter(adapter);
        listViewTrackers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

//        for (final TrackersAdapter.TrackerListItem item: list) {
//            FrameLayout frameTrackerMove = (FrameLayout) item.view.findViewById(R.id.frameTrackerMove);
//            frameTrackerMove.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    Log.i(Constants.LOG_TAG, "Touched Frame!");
//                    listViewTrackers.startListMovementFromItem(item);
//                    return false;
//                }
//            });
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_trackers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
