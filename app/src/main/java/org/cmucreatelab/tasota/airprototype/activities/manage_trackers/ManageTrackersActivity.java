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

import java.util.ArrayList;


public class ManageTrackersActivity extends ActionBarActivity {

    private ListView listViewTrackers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__trackers__manage_trackers);

        listViewTrackers = (ListView)findViewById(R.id.listViewTrackers);
        ArrayList<String> list = new ArrayList<>();
        list.add("One");
        list.add("Two");
        list.add("3");
        for (int i=0;i<4;i++) {
            list.add("foo");
        }
        listViewTrackers.setAdapter(new TrackersAdapter(this,list));
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
