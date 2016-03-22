package org.cmucreatelab.tasota.airprototype.activities.readable_show.aqi_explanation;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirNowReadable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;

public class AqiExplanationActivity extends ActionBarActivity {

    private AirNowReadable reading;
    protected AqiExplanationButtonClickListener buttonClickListener;
    private AqiExplanationUIElements uiElements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_show__aqi_explanation_activity);

        this.reading = GlobalHandler.getInstance(getApplicationContext()).readableShowToAirNow;
        buttonClickListener = new AqiExplanationButtonClickListener(this);
        uiElements = new AqiExplanationUIElements(this);
        uiElements.populate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_show_aqi_explanation, menu);
        return true;
    }

}
