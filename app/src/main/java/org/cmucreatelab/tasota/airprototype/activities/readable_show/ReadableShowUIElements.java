package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.UIElements;
import org.cmucreatelab.tasota.airprototype.classes.aqi_scales.AQIReading;
import org.cmucreatelab.tasota.airprototype.classes.aqi_scales.SpeckReading;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.AqiReadableValue;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirQualityFeed;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 6/18/15.
 */
public class ReadableShowUIElements extends UIElements<ReadableShowActivity> {

    private Readable reading;
    private TextView textShowAddressAqiValue;
    private TextView textShowAddressAqiRange;
    private TextView textShowAddressAqiTitle;
    private TextView textShowAddressAqiDescription;
    private TextView textShowAddressAqiLabel;
    private RelativeLayout layoutShowAddress;
    private FrameLayout framePm25AqiButton,frameOzoneAqiButton;
    private FrameLayout frameDailyTrackerButton;
    private TextView textViewPm25ReadingName, textViewOzoneReadingName;
    private TextView textViewPm25ReadingValue, textViewOzoneReadingValue;


    public ReadableShowUIElements(ReadableShowActivity activity, Readable reading) {
        super(activity);
        this.reading = reading;
        this.textShowAddressAqiValue = (TextView)activity.findViewById(R.id.textShowAddressAqiValue);
        this.textShowAddressAqiRange = (TextView)activity.findViewById(R.id.textShowAddressAqiRange);
        this.textShowAddressAqiTitle = (TextView)activity.findViewById(R.id.textShowAddressAqiTitle);
        this.textShowAddressAqiDescription = (TextView)activity.findViewById(R.id.textShowAddressAqiDescription);
        this.textShowAddressAqiLabel = (TextView)activity.findViewById(R.id.textShowAddressAqiLabel);
        this.layoutShowAddress = (RelativeLayout)activity.findViewById(R.id.layoutShowAddress);
        this.framePm25AqiButton = (FrameLayout)activity.findViewById(R.id.framePm25AqiButton);
        this.frameOzoneAqiButton= (FrameLayout)activity.findViewById(R.id.frameOzoneAqiButton);
        this.frameDailyTrackerButton = (FrameLayout)activity.findViewById(R.id.frameDailyTracker);
        this.textViewPm25ReadingName = (TextView)activity.findViewById(R.id.textViewPm25ReadingName);
        this.textViewOzoneReadingName = (TextView)activity.findViewById(R.id.textViewOzoneReadingName);
        this.textViewPm25ReadingValue = (TextView)activity.findViewById(R.id.textViewPm25ReadingValue);
        this.textViewOzoneReadingValue = (TextView)activity.findViewById(R.id.textViewOzoneReadingValue);

        // use custom fonts
        Typeface fontAqi = Typeface.createFromAsset(activity.getAssets(), "fonts/Dosis-Light.ttf");
        textShowAddressAqiValue.setTypeface(fontAqi);
        textViewPm25ReadingValue.setTypeface(fontAqi);
        textViewOzoneReadingValue.setTypeface(fontAqi);

        framePm25AqiButton.setOnClickListener(activity.frameClickListener);
        frameOzoneAqiButton.setOnClickListener(activity.frameClickListener);
        frameDailyTrackerButton.setOnClickListener(activity.frameDailyTrackerListener);

        // hide buttons
        framePm25AqiButton.setVisibility(View.INVISIBLE);
        frameOzoneAqiButton.setVisibility(View.INVISIBLE);
        frameDailyTrackerButton.setVisibility(View.INVISIBLE);
    }


    public void populate() {
        if (reading.hasReadableValue()) {
            double readableValue = reading.hasReadableValue() ? reading.getReadableValues().get(0).getValue() : 0.0;

            if (reading.getReadableType() == Readable.Type.ADDRESS) {
                addressView((SimpleAddress) reading);
            } else if (reading.getReadableType() == Readable.Type.SPECK) {
                speckView((Speck) reading);
            } else {
                Log.w(Constants.LOG_TAG, "Tried to populate LinearView in AddressShow with unimplemented Readable object.");
                defaultView();
            }
        } else {
            defaultView();
        }
    }


    private void defaultView() {
        Log.w(Constants.LOG_TAG, "warning: using default populate");
        this.textShowAddressAqiValue.setText("");
        textShowAddressAqiRange.setText("");
        textShowAddressAqiLabel.setVisibility(View.INVISIBLE);
        textShowAddressAqiTitle.setText(Constants.DefaultReading.DEFAULT_TITLE);
        textShowAddressAqiDescription.setText(Constants.DefaultReading.DEFAULT_DESCRIPTION);
        layoutShowAddress.setBackgroundColor(Color.parseColor(Constants.DefaultReading.DEFAULT_COLOR_BACKGROUND));
    }


    private void addressView(final SimpleAddress address) {
        if (address.hasReadableValue()) {
            AqiReadableValue readableValue = null;

            if (address.hasReadableOzoneValue()) {
                frameOzoneAqiButton.setVisibility(View.VISIBLE);
                // get ozone value/channel/feed
                AqiReadableValue ozone = address.getReadableOzoneValue();
                AirQualityFeed closestFeed = (AirQualityFeed) ozone.getChannel().getFeed();
                // set text for name and value
                textViewOzoneReadingName.setText(closestFeed.getName());
                textViewOzoneReadingValue.setText(String.valueOf((int)ozone.getAqiValue()));
                if (readableValue == null || ozone.getAqiValue() > readableValue.getAqiValue()) {
                    readableValue = ozone;
                }
            }

            if (address.hasReadablePm25Value()) {
                frameDailyTrackerButton.setVisibility(View.VISIBLE);
                framePm25AqiButton.setVisibility(View.VISIBLE);
                // get ozone value/channel/feed
                AqiReadableValue pm25 = address.getReadablePm25Value();
                AirQualityFeed closestFeed = (AirQualityFeed) pm25.getChannel().getFeed();
                // set text for name and value
                textViewPm25ReadingName.setText(closestFeed.getName());
                textViewPm25ReadingValue.setText(String.valueOf((int)pm25.getAqiValue()));
                if (readableValue == null || pm25.getAqiValue() > readableValue.getAqiValue()) {
                    readableValue = pm25;
                }
            }

            this.textShowAddressAqiValue.setText(String.valueOf((int)readableValue.getAqiValue()));
            AQIReading aqiReading = new AQIReading(readableValue);
            textShowAddressAqiRange.setText(aqiReading.getRangeFromIndex() + " AQI");
            textShowAddressAqiTitle.setText(aqiReading.getTitle());
            textShowAddressAqiDescription.setText(aqiReading.getDescription());
            layoutShowAddress.setBackgroundResource(aqiReading.getDrawableGradient());
            textShowAddressAqiLabel.setText(Constants.Units.RANGE_AQI);
            // request Tracker
            address.requestDailyFeedTracker(activity);
        } else {
            this.defaultView();
        }
    }


    private void speckView(Speck speck) {
        double value = speck.hasReadableValue() ? speck.getReadableValues().get(0).getValue() : 0.0;
        this.textShowAddressAqiValue.setText(String.valueOf((long)value));
        SpeckReading speckReading = new SpeckReading(value);
        if (speckReading.withinRange()) {
            textShowAddressAqiRange.setText(speckReading.getRangeFromIndex() + " Micrograms");
            textShowAddressAqiTitle.setText(speckReading.getTitle());
            textShowAddressAqiDescription.setText(speckReading.getDescription());
            layoutShowAddress.setBackgroundColor(Color.parseColor(speckReading.getColor()));
            textShowAddressAqiLabel.setText(Constants.Units.RANGE_MICROGRAMS_PER_CUBIC_METER);
        } else {
            this.defaultView();
        }
    }

}
