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
import org.cmucreatelab.tasota.airprototype.classes.aqi_scales.AQIReading;
import org.cmucreatelab.tasota.airprototype.classes.aqi_scales.Scalable;
import org.cmucreatelab.tasota.airprototype.classes.aqi_scales.WHOReading;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 5/11/16.
 */
public class DailyTrackerUIElements extends UIElements<DailyTrackerActivity> implements AdapterView.OnItemSelectedListener {

    // ui elements
    private WebView webView;
    private DailyFeedTracker tracker;
    private Spinner spinner1,spinner2;
    // display attributes
    private DayFeedValue.DaysValueType displayType = DayFeedValue.DaysValueType.MAX;
    private Scalable.ScaleType scaleType = Scalable.ScaleType.EPA_AQI;


    public DailyTrackerUIElements(DailyTrackerActivity activity) { super(activity); }


    private String constructColorsList() {
        String result = "";
        ArrayList<DayFeedValue> values = tracker.getValues();
        Log.v(Constants.LOG_TAG,String.format("sampling %d points",values.size()));

        int day,index=0;
        long startTime = tracker.getStartTime();
        for (day=0;day<364;day++) {
            startTime += Constants.TWENTY_FOUR_HOURS;

            // only check for values we still have
            if (index<values.size()) {
                DayFeedValue value = values.get(index);
                // add color value if value was in the past day
                if (value.getTime() <= startTime) {
                    index++;
                    double reading = value.getCount(displayType);
                    switch(scaleType) {
                        case EPA_AQI:
                            result += new AQIReading(reading).getColor().replaceAll("#", "");
                            break;
                        case WHO:
                            result += new WHOReading(reading).getColor().replaceAll("#", "");
                            break;
                        default:
                            Log.e(Constants.LOG_TAG,"Unrecognized ScaleType for Daily Tracker colors list.");
                    }
                }
            }
            // next value (even if empty day, then just an empty string)
            result += ",";
        }
        result = result.substring(0,result.length()-2);

        Log.v(Constants.LOG_TAG,"returning colorList="+result);
        return result;
    }


    public void populate() {
        this.tracker = activity.address.getDailyFeedTracker();

        this.webView = (WebView)activity.findViewById(R.id.webView);

        // spinner 1
        spinner1 = (Spinner) activity.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.trackers_spinner,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        // spinner 2
        spinner2 = (Spinner) activity.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(activity, R.array.trackers_spinner_scale,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
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
            case "EPA AQI":
                scaleType = Scalable.ScaleType.EPA_AQI;
                break;
            case "WHO":
                scaleType = Scalable.ScaleType.WHO;
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
