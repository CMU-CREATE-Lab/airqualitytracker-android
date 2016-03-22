package org.cmucreatelab.tasota.airprototype.activities.readable_show.air_now;

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
    protected final ArrayList<AirNowAdapter.AirNowItem> airNowItemsList = new ArrayList<>();
    protected AirNowAdapter airNowAdapter;
    private AirNowUIElements uiElements;


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

        airNowAdapter = new AirNowAdapter(this,airNowItemsList);
        uiElements = new AirNowUIElements(this);
        uiElements.populate();

        clearAndUpdateList(observations);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_show_airnow, menu);
        return true;
    }


    public void clearAndUpdateList(ArrayList<AirNowObservation> observations) {
        airNowItemsList.clear();
        if (observations.size() > 0) {
            airNowItemsList.add(new AirNowAdapter.AirNowItem("observed at "+observations.get(0).getReadableDate(), 0));
            for (AirNowObservation observation : observations) {
                airNowItemsList.add(new AirNowAdapter.AirNowItem(observation));
            }
        } else {
            airNowItemsList.add(new AirNowAdapter.AirNowItem("No AirNow observations", 0));
        }
        airNowAdapter.notifyDataSetChanged();
    }

}
