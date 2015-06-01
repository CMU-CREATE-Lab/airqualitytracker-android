package org.cmucreatelab.tasota.airprototype;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ArrayList<String> dataset;
    ArrayAdapter<String> listAdapter;

    Address myAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this is a temp address (for testing API calls)
        myAddress = new Address("15235", 40.4586216, -79.8184684);
//        updateFeeds(myAddress);

        ListView lv = (ListView)findViewById(R.id.listView);
        dataset = new ArrayList<String>();
        dataset.add("one");
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataset);
        lv.setAdapter(listAdapter);

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


    public void clickHttpRequest(View view) {
        updateFeeds(myAddress);
    }


    private void updateFeeds(Address addr) {

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dataset.clear();

                try {
                    JSONArray feeds = response.getJSONObject("data").getJSONArray("rows");
                    int size = feeds.length();
                    for (int i=0;i<size;i++) {
                        JSONObject feed = (JSONObject)feeds.get(i);
                        // TODO construct Feed and Channels from JSON
                        String label = "(" + feed.get("id").toString() + ")" + feed.get("name").toString();
                        dataset.add(label);
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
