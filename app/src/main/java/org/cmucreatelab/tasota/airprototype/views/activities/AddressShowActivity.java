package org.cmucreatelab.tasota.airprototype.views.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import java.util.ArrayList;

public class AddressShowActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        int index;
        ListView listView;
        SimpleAddress showSimpleAddress;
        ArrayList<Feed> feeds;
        ArrayAdapter<Feed> feedsListAdapter;

        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "AddressShowActivity onCreate");
        setContentView(R.layout.activity_address_show);
        intent = getIntent();
        index = intent.getIntExtra(AddressListActivity.ADDRESS_INDEX, -1);
        showSimpleAddress = GlobalHandler.getInstance(this.getApplicationContext()).getAddresses().get(index);
        feeds = GlobalHandler.getInstance(this.getApplicationContext()).getFeedsFromAddressInHashMap(showSimpleAddress);
        feedsListAdapter = new ArrayAdapter<Feed>(this, android.R.layout.simple_list_item_1, feeds);

        // generate content for TextViews
        ((TextView)findViewById(R.id.textShowAddressName)).setText(showSimpleAddress.getName());
        ((TextView)findViewById(R.id.textShowAddressLat)).setText(String.valueOf(showSimpleAddress.getLatitude()));
        ((TextView)findViewById(R.id.textShowAddressLong)).setText(String.valueOf(showSimpleAddress.getLongitude()));
        if (showSimpleAddress.getClosestFeed() == null) {
            ((TextView)findViewById(R.id.textShowAddressClosestFeed)).setText("null");
        } else {
            ((TextView)findViewById(R.id.textShowAddressClosestFeed)).setText(String.valueOf(showSimpleAddress.getClosestFeed().getName()));
        }

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
