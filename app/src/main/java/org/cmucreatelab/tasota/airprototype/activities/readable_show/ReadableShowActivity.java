package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.BaseActivity;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;

public class ReadableShowActivity extends BaseActivity<ReadableShowUIElements> {

    protected Readable reading;
    protected ReadableShowFrameClickListener frameClickListener;
    protected ReadableShowTrackerFrameClickListener frameDailyTrackerListener;


    public void feedTrackerResponse(String text) {
        TextView textViewDirtyDays = (TextView) findViewById(R.id.textViewDirtyDays);
        textViewDirtyDays.setText(text);
        frameDailyTrackerListener.isEnabled = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        int itemIndex;
        ActionBar actionBar;
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_show__activity);

        intent = getIntent();
        itemIndex = intent.getIntExtra(Constants.AddressList.ADDRESS_INDEX, -1);

        // if we don't have an Intent, grab from what it's supposed to be
        if (itemIndex >= 0) {
            globalHandler.readableShowItemIndex = itemIndex;
        } else {
            itemIndex = globalHandler.readableShowItemIndex;
        }

        this.reading = globalHandler.readingsHandler.adapterList.get(itemIndex).readable;
        String value;
        switch(reading.getReadableType()) {
            case ADDRESS:
                SimpleAddress simpleAddress = (SimpleAddress)reading;
                Log.i(Constants.LOG_TAG,"OPENING ADDRESS");
                value = simpleAddress.hasReadablePm25Value() ? String.valueOf(simpleAddress.getReadablePm25Value().getAqiValue()) : "n/a";
                Log.i(Constants.LOG_TAG,"PM2.5(aqi)="+value);
                value = simpleAddress.hasReadableOzoneValue() ? String.valueOf(simpleAddress.getReadableOzoneValue().getAqiValue()) : "n/a";
                Log.i(Constants.LOG_TAG,"OZONE(aqi)="+value);
                break;
            case SPECK:
                Speck speck = (Speck)reading;
                Log.i(Constants.LOG_TAG,"OPENING SPECK");
                value = speck.hasReadablePm25Value() ? String.valueOf(speck.getReadablePm25Value().getAqiValue()) : "n/a";
                Log.i(Constants.LOG_TAG,"PM2.5(aqi)="+value);
                value = speck.hasReadableHumidityValue() ? String.valueOf(speck.getReadableHumidityValue().getValue()) : "n/a";
                Log.i(Constants.LOG_TAG,"HUMID="+value);
                value = speck.hasReadableTemperatureValue() ? String.valueOf(speck.getReadableTemperatureValue().getValue()) : "n/a";
                Log.i(Constants.LOG_TAG,"TEMP="+value);

                break;
            default:
                Log.e(Constants.LOG_TAG, "Tried to show non-SimpleAddress Readable (not implemented)");
                finish();
                return;
        }

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(reading.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        frameClickListener = new ReadableShowFrameClickListener(this);
        frameDailyTrackerListener = new ReadableShowTrackerFrameClickListener(this);
        new ReadableShowUIElements(this, reading).populate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_show, menu);
        return true;
    }

}
