package org.cmucreatelab.tasota.airprototype.activities.readable_show.daily_tracker;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.BaseActivity;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;

/**
 * Created by mike on 5/11/16.
 */
public class DailyTrackerActivity extends BaseActivity<DailyTrackerUIElements> {

    public SimpleAddress address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_show____daily_tracker__activity);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        address = (SimpleAddress) globalHandler.readingsHandler.adapterList.get(globalHandler.readableShowItemIndex).readable;

        new DailyTrackerUIElements(this).populate();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Trends: "+address.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
