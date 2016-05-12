package org.cmucreatelab.tasota.airprototype.activities.readable_show.daily_tracker;

import android.webkit.WebView;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.UIElements;
import org.cmucreatelab.tasota.airprototype.classes.DailyFeedTracker;
import org.cmucreatelab.tasota.airprototype.classes.DayFeedValue;

/**
 * Created by mike on 5/11/16.
 */
public class DailyTrackerUIElements extends UIElements<DailyTrackerActivity> {

    // ui elements
    private TextView textViewMean,textViewMedian,textViewMax;
    private WebView webView;


    public DailyTrackerUIElements(DailyTrackerActivity activity) { super(activity); }


    public void populate() {
        this.textViewMean = (TextView)activity.findViewById(R.id.textViewMean);
        this.textViewMedian = (TextView)activity.findViewById(R.id.textViewMedian);
        this.textViewMax = (TextView)activity.findViewById(R.id.textViewMax);
        this.webView = (WebView)activity.findViewById(R.id.webView);

        DailyFeedTracker tracker = activity.address.getDailyFeedTracker();
        this.textViewMean.setText("Mean: " + tracker.getDaysCount(DayFeedValue.DaysValueType.MEAN));
        this.textViewMedian.setText("Median: " + tracker.getDaysCount(DayFeedValue.DaysValueType.MEDIAN));
        this.textViewMax.setText("Max: " + tracker.getDaysCount(DayFeedValue.DaysValueType.MAX));
        this.webView.getSettings().setJavaScriptEnabled(true);

        // TODO construct comma-separated list of hex colors and append to URL
        this.webView.loadUrl("file:///android_asset/webviews/daily_tracker_grid.html?table-colors=a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c,a3ba5c,e9b642,e98c37,a3ba5c,a3ba5c,e9b642,a3ba5c,a3ba5c,a3ba5c");
    }

}
