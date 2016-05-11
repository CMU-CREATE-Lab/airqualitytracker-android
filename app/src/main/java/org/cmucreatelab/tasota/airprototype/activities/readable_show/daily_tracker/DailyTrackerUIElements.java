package org.cmucreatelab.tasota.airprototype.activities.readable_show.daily_tracker;

import android.widget.TextView;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.UIElements;

/**
 * Created by mike on 5/11/16.
 */
public class DailyTrackerUIElements extends UIElements<DailyTrackerActivity> {

    // ui elements
    private TextView textViewMean,textViewMedian,textViewMax;


    public DailyTrackerUIElements(DailyTrackerActivity activity) { super(activity); }


    public void populate() {
        // TODO populate
        this.textViewMean = (TextView)activity.findViewById(R.id.textViewMean);
        this.textViewMedian = (TextView)activity.findViewById(R.id.textViewMedian);
        this.textViewMax = (TextView)activity.findViewById(R.id.textViewMax);
    }

}
