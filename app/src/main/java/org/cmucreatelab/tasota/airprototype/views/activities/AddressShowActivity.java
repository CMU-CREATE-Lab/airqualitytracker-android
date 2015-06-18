package org.cmucreatelab.tasota.airprototype.views.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.views.uielements.LinearViewAddressShow;
import java.util.ArrayList;

public class AddressShowActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        int addressIndex;
        ListView listView;
        SimpleAddress showSimpleAddress;
        ArrayList<Feed> feeds;
        ArrayAdapter<Feed> feedsListAdapter;

        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "AddressShowActivity onCreate");
        setContentView(R.layout.activity_address_show);
        intent = getIntent();
        addressIndex = intent.getIntExtra(AddressListActivity.ADDRESS_INDEX, -1);
        showSimpleAddress = GlobalHandler.getInstance(this.getApplicationContext()).getAddresses().get(addressIndex);
        feeds = GlobalHandler.getInstance(this.getApplicationContext()).getFeedsFromAddressInHashMap(showSimpleAddress);
        feedsListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, feeds);

        // generate content
        new LinearViewAddressShow(this,showSimpleAddress).populateLinearView();

        // setup ListView
        listView = (ListView)findViewById(R.id.listShowAddressFeeds);
        listView.setAdapter(feedsListAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_show, menu);
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
