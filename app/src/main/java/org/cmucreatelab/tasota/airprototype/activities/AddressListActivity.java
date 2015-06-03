package org.cmucreatelab.tasota.airprototype.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.cmucreatelab.tasota.airprototype.AddressListArrayAdapter;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

import java.util.ArrayList;


public class AddressListActivity extends ActionBarActivity {
    ArrayList<Address> addresses;
    AddressListArrayAdapter listAdapter;

    public final static String ADDRESS_INDEX = "org.cmucreatelab.tasota.airprototype.addressindex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

//        ActionBar actionBar = getSupportActionBar();

        addresses = GlobalHandler.getInstance(this.getApplicationContext()).addresses;

        ListView lv = (ListView)findViewById(R.id.listViewAddresses);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switchToMainActivity(i);
                    }
                }
        );
        listAdapter = new AddressListArrayAdapter(this, addresses);
        lv.setAdapter(listAdapter);
    }


    public void switchToMainActivity(int index) {
        Intent intent = new Intent(this, AddressShowActivity.class);
        intent.putExtra(ADDRESS_INDEX,index);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == R.id.action_new) {
//
//        }
//
//        return super.onOptionsItemSelected(item);

        switch (id) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_new:
                openNew();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void openSettings() {
        // TODO open settings
        Log.i("openSettings", "action bar selected.");
    }


    public void openNew() {
        // TODO open new (address input)
        Log.i("openNew", "action bar selected.");
    }
}
