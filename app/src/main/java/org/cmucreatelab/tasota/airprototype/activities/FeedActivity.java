package org.cmucreatelab.tasota.airprototype.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Channel;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

import java.util.ArrayList;


// TODO unused function (should be deleted)
public class FeedActivity extends ActionBarActivity {
    ArrayList<Feed> feeds;
    ArrayList<Channel> channels;
    ArrayAdapter<Channel> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

//        feeds = GlobalHandler.getInstance(this.getApplicationContext()).feeds;

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.FEED_MESSAGE);
        channels = feeds.get( intent.getIntExtra(MainActivity.FEED_ID, -1) ).getChannels();

        TextView tv = (TextView) findViewById(R.id.textViewFeed);
        tv.setText(message);

        ListView lv = (ListView)findViewById(R.id.listViewFeed);
        listAdapter = new ArrayAdapter<Channel>(this, android.R.layout.simple_list_item_1, channels);
        lv.setAdapter(listAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
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
