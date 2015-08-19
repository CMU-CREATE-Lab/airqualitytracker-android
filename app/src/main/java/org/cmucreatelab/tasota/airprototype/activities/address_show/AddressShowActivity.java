package org.cmucreatelab.tasota.airprototype.activities.address_show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.AboutAirQualityActivity;
import org.cmucreatelab.tasota.airprototype.activities.AboutSpeckActivity;
import org.cmucreatelab.tasota.airprototype.activities.SettingsActivity;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;

public class AddressShowActivity extends ActionBarActivity {

//    private SimpleAddress showSimpleAddress;
    private Readable readable;


    protected void shareStationAqi() {
        try {
            String message, label;
            double aqi;
//            aqi = (long) (100 * Converter.microgramsToAqi(showSimpleAddress.getClosestFeed().getFeedValue())) / 100.0;
            aqi = (long) (100 * Converter.microgramsToAqi(readable.getReadableValue())) / 100.0;
            label = Constants.AqiReading.titles[Constants.AqiReading.getIndexFromReading(aqi)];
            // "My air quality is <AQI label>. Learn more at https://www.specksensor.com/"
            message = "My air quality is " + label + ". Learn more at https://www.specksensor.com/";
            Log.d(Constants.LOG_TAG, "Sharing string: ''" + message + "''");
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Air Quality Index"));
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG,"Received error while trying to share station's AQI on AddressShowActivity.");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        int itemIndex;

        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "AddressShowActivity onCreate");
        setContentView(R.layout.__address_show__activity);

        intent = getIntent();
        itemIndex = intent.getIntExtra(Constants.AddressList.ADDRESS_INDEX, -1);
        this.readable = GlobalHandler.getInstance(getApplicationContext()).headerReadingsHashMap.adapterList.get(itemIndex).readable;
        switch(readable.getReadableType()) {
            case ADDRESS:
                break;
            case SPECK:
                break;
            default:
                Log.e(Constants.LOG_TAG, "Tried to show non-SimpleAddress Readable (not implemented)");
                finish();
                return;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(readable.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        new LinearViewAddressShow(this,readable).populateLinearView();
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
            case R.id.action_about_airquality:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: about selected.");
                startActivity(new Intent(this, AboutAirQualityActivity.class));
                return true;
            case R.id.action_about_speck:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: about speck selected.");
                startActivity(new Intent(this, AboutSpeckActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
