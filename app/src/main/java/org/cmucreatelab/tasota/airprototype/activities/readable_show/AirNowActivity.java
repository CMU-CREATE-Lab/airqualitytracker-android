package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.AirNowObservation;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirNowReadable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

public class AirNowActivity extends ActionBarActivity {

    private AirNowReadable reading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_show__airnow_activity);

        this.reading = GlobalHandler.getInstance(getApplicationContext()).readableShowToAirNow;

        ArrayList<AirNowObservation> observations = reading.getMostRecentAirNowObservations();
        if (observations.size() == 0) {
            Log.i(Constants.LOG_TAG, "Requesting observations from airnow for reading=" + reading.getName());
            reading.requestAirNow(this);
        }

        // TODO list adapter stuff
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_show_airnow, menu);
        return true;
    }

}
