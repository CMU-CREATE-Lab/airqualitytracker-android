package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import org.cmucreatelab.tasota.airprototype.R;

public class AqiExplanationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_show__aqi_explanation_activity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_show_aqi_explanation, menu);
        return true;
    }

}
