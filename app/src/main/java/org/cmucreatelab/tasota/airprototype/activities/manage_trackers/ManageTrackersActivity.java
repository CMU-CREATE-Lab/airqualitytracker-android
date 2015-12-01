package org.cmucreatelab.tasota.airprototype.activities.manage_trackers;

import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

import java.util.ArrayList;

import listviewdragginganimation.Cheeses;
import listviewdragginganimation.DynamicListView;
import listviewdragginganimation.StableArrayAdapter;


public class ManageTrackersActivity extends ActionBarActivity {

    private DynamicListView listViewTrackers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__trackers__manage_trackers);

        ArrayList<TrackersAdapter.TrackerListItem> list = GlobalHandler.getInstance(getApplicationContext()).headerReadingsHashMap.trackerList;

        TrackersAdapter adapter = new TrackersAdapter(this,list);
        listViewTrackers = (DynamicListView)findViewById(R.id.listViewTrackers);

        listViewTrackers.setCheeseList(list);
        listViewTrackers.setAdapter(adapter);
        listViewTrackers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
