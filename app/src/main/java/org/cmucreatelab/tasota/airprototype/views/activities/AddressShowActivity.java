package org.cmucreatelab.tasota.airprototype.views.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import java.util.ArrayList;

public class AddressShowActivity extends ActionBarActivity {

    Address showAddress;
    ArrayList<Feed> feeds;
    ArrayAdapter<Feed> feedsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        int index;
        TextView textView;
        ListView listView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_show);
        intent = getIntent();
        index = intent.getIntExtra(AddressListActivity.ADDRESS_INDEX, -1);
        showAddress = GlobalHandler.getInstance(this.getApplicationContext()).addresses.get(index);
        feeds = GlobalHandler.getInstance(this.getApplicationContext()).addressFeedHash.get(showAddress);
        feedsListAdapter = new ArrayAdapter<Feed>(this, android.R.layout.simple_list_item_1, feeds);

        // generate content for TextViews
        textView = (TextView)findViewById(R.id.textShowAddressName);
        textView.setText(showAddress.getName());
        textView = (TextView)findViewById(R.id.textShowAddressLat);
        textView.setText(String.valueOf(showAddress.getLatitude()));
        textView = (TextView)findViewById(R.id.textShowAddressLong);
        textView.setText(String.valueOf(showAddress.getLongitude()));

        // setup ListView
        listView = (ListView)findViewById(R.id.listShowAddressFeeds);
        listView.setAdapter(feedsListAdapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switchToFeedActivity(i);
                    }
                }
        );
    }


    public void switchToFeedActivity(int index) {
        // TODO switch to FeedActivity
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
