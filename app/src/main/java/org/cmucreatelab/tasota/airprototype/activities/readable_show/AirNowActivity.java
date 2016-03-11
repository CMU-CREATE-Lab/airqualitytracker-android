package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirNowReadable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;

public class AirNowActivity extends ActionBarActivity {

    private AirNowReadable reading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_show__airnow_activity);

        this.reading = GlobalHandler.getInstance(getApplicationContext()).readableShowToAirNow;
        // TODO get most recent airnow observations
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_show_airnow, menu);
        return true;
    }

}
