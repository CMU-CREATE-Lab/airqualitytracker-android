package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

public class AqiExplanationActivity extends ActionBarActivity {

    private Button buttonAirNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_show__aqi_explanation_activity);

        this.buttonAirNow = (Button)findViewById(R.id.buttonAirNow);

        buttonAirNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(Constants.LOG_TAG, "clicked buttonAirNow");
                startActivity(new Intent(getApplicationContext(), AirNowActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_show_aqi_explanation, menu);
        return true;
    }

}
