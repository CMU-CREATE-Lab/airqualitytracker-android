package org.cmucreatelab.tasota.airprototype.activities.readable_show.daily_tracker;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.UIElements;
import org.cmucreatelab.tasota.airprototype.classes.DailyFeedTracker;
import org.cmucreatelab.tasota.airprototype.classes.DayFeedValue;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.AqiConverter;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 5/11/16.
 */
public class DailyTrackerUIElements extends UIElements<DailyTrackerActivity> implements AdapterView.OnItemSelectedListener {

    // ui elements
    private WebView webView;
    private DayFeedValue.DaysValueType displayType = DayFeedValue.DaysValueType.MEAN;
    private DailyFeedTracker tracker;


    public DailyTrackerUIElements(DailyTrackerActivity activity) { super(activity); }


    private String constructColorsList() {
        String result = "";
        ArrayList<DayFeedValue> values = tracker.getValues();
        Log.v(Constants.LOG_TAG,String.format("sampling %d points",values.size()));

        for (DayFeedValue value : values) {
            double reading = value.getCount(displayType);
            int index = Constants.AqiReading.getIndexFromReading(AqiConverter.microgramsToAqi(reading));
            result += Constants.AqiReading.aqiColors[index].replaceAll("#","");
            result += ",";
        }
        result = result.substring(0,result.length()-2);

        Log.v(Constants.LOG_TAG,"returning colorList="+result);
        return result;
    }


    public void populate() {
        this.tracker = activity.address.getDailyFeedTracker();
        
        this.webView = (WebView)activity.findViewById(R.id.webView);
        Spinner spinner = (Spinner) activity.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.trackers_spinner,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selected = adapterView.getItemAtPosition(i).toString();
        Log.v(Constants.LOG_TAG,"SPINNER: Selected "+selected);
        switch (selected) {
            case "Mean":
                displayType = DayFeedValue.DaysValueType.MEAN;
                break;
            case "Median":
                displayType = DayFeedValue.DaysValueType.MEDIAN;
                break;
            case "Max":
                displayType = DayFeedValue.DaysValueType.MAX;
                break;
            default:
                Log.e(Constants.LOG_TAG,"Failed to grab selected; defaulting to dirty days.");
                displayType = Constants.DIRTY_DAYS_VALUE_TYPE;
        }

        String colorsList = constructColorsList();
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl("file:///android_asset/webviews/daily_tracker_grid.html?table-colors="+colorsList);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
