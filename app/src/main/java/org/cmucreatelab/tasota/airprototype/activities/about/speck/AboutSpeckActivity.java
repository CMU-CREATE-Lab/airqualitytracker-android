package org.cmucreatelab.tasota.airprototype.activities.about.speck;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import org.cmucreatelab.tasota.airprototype.R;

public class AboutSpeckActivity extends ActionBarActivity {

    private AboutSpeckUIElements uiElements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__about__speck);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("About SpeckSensor");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        uiElements = new AboutSpeckUIElements(this);
        uiElements.populate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_speck, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
