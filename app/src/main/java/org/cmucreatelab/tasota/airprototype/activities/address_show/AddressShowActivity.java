package org.cmucreatelab.tasota.airprototype.activities.address_show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.SettingsActivity;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;

public class AddressShowActivity extends ActionBarActivity {

    private SimpleAddress showSimpleAddress;


    protected void shareStationAqi() {
        String message;
        double aqi;
        aqi = (long)(100 * Converter.microgramsToAqi(showSimpleAddress.getClosestFeed().getFeedValue()))/100.0;
        message = "Address " + showSimpleAddress.getName() + " has AQI " + String.valueOf(aqi) + " from station " + showSimpleAddress.getClosestFeed().getName();
        Log.d(Constants.LOG_TAG,"Sharing string: ''" + message + "''");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Air Quality Index"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        int addressIndex;

        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "AddressShowActivity onCreate");
        setContentView(R.layout.activity_address_show_nofeeds);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
        }

        intent = getIntent();
        addressIndex = intent.getIntExtra(Constants.AddressList.ADDRESS_INDEX, -1);
        showSimpleAddress = GlobalHandler.getInstance(this.getApplicationContext()).requestAddressesForDisplay().get(addressIndex);

        // generate content (no feed list)
        new LinearViewAddressShowNoFeeds(this,showSimpleAddress).populateLinearView();

        // no longer list feeds (for now)
//        ListView listView;
//        ArrayList<Feed> feeds;
//        ArrayAdapter<Feed> feedsListAdapter;
//
//        feeds = GlobalHandler.getInstance(this.getApplicationContext()).addressFeedsHashMap.getFeedsFromAddressInHashMap(showSimpleAddress);
//        feedsListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, feeds);
//
//        // generate content
//        new LinearViewAddressShow(this,showSimpleAddress).populateLinearView();
//
//        // setup ListView
//        listView = (ListView)findViewById(R.id.listShowAddressFeeds);
//        listView.setAdapter(feedsListAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_show, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: settings selected.");
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_share:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: action share selected.");
                this.shareStationAqi();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
