package org.cmucreatelab.tasota.airprototype.activities.readable_show;

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
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;

public class ReadableShowActivity extends ActionBarActivity {

    private Readable readable;


    protected void shareReading() {
        String message, label;
        Intent sendIntent = new Intent();

        try {
            switch(readable.getReadableType()) {
                case SPECK:
                    long micrograms = (long)readable.getReadableValue();
                    label = Constants.SpeckReading.titles[Constants.SpeckReading.getIndexFromReading(micrograms)];
                    break;
                case ADDRESS:
                    double aqi = (long) (100 * Converter.microgramsToAqi(readable.getReadableValue())) / 100.0;
                    label = Constants.AqiReading.titles[Constants.AqiReading.getIndexFromReading(aqi)];
                    break;
                default:
                    Log.e(Constants.LOG_TAG,"shareReading could not find Readable type.");
                    return;
            }
            message = "My air quality is " + label + ". Learn more at https://www.specksensor.com/";

            Log.d(Constants.LOG_TAG, "Sharing string: ''" + message + "''");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Air Quality Index"));
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG,"Received error while trying to share station's AQI on ReadableShowActivity.");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        int itemIndex;
        ActionBar actionBar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_show__activity);

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

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(readable.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        new LinearViewReadableShow(this,readable).populateLinearView();
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
//            case R.id.action_settings:
//                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: settings selected.");
//                startActivity(new Intent(this, SettingsActivity.class));
//                return true;
            case R.id.action_share:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: action share selected.");
                this.shareReading();
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
