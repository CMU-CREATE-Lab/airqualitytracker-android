package org.cmucreatelab.tasota.airprototype.activities;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.HttpRequestHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ArrayList<Feed> feeds;
    ArrayAdapter<Feed> listAdapter;

    Address myAddress;
    public final static String FEED_MESSAGE = "org.cmucreatelab.tasota.airprototype.feedmessage";
    public final static String FEED_ID= "org.cmucreatelab.tasota.airprototype.feedid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this is a temp address (for testing API calls)
        myAddress = GlobalHandler.getInstance(this.getApplicationContext()).addresses.get(0);

        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("onItemClick", "Clicked with i=" + i + ",l=" + l + ".");
                        String item = adapterView.getAdapter().getItem(i).toString();
                        Log.i("onItemClick", "GRABBED ITEM: " + item);

                        switchToFeedActivity(item,i);
                    }
                }
        );
        feeds = GlobalHandler.getInstance(this.getApplicationContext()).feeds;
        listAdapter = new ArrayAdapter<Feed>(this, android.R.layout.simple_list_item_1, feeds);
        lv.setAdapter(listAdapter);
        updateFeeds(myAddress);
    }

    // TODO is there a way to create an intent without issues scoping "this" ?
    public void switchToFeedActivity(String message, int index) {
        Intent intent = new Intent(this, FeedActivity.class);
        intent.putExtra(FEED_MESSAGE, message);
        intent.putExtra(FEED_ID,index);
        //using parcels for objects
        //intent.putParcelableArrayListExtra("",new ArrayList<Feed>());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


//    public void clickHttpRequest(View view) {
//        updateFeeds(myAddress);
//    }


    private void updateFeeds(Address addr) {

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                feeds.clear();

                try {
                    JSONArray jsonFeeds = response.getJSONObject("data").getJSONArray("rows");
                    int size = jsonFeeds .length();
                    for (int i=0;i<size;i++) {
                        JSONObject feed = (JSONObject)jsonFeeds.get(i);
                        Feed f = Feed.parseFeedFromJson(feed);
                        feeds.add(f);
                    }
                } catch (Exception e) {
                    // TODO catch exception "failed to find JSON attr"
                    e.printStackTrace();
                }


                listAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle errors
            }
        };
        HttpRequestHandler.getInstance(this.getApplicationContext()).requestFeeds(addr.getLatitude(),addr.getLongitude(),response,error);
    }
}
