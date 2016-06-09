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
import org.cmucreatelab.tasota.airprototype.classes.readables.Readable;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.AqiConverter;

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
    private FrameLayout frameAqiButton;
    private FrameLayout frameDailyTrackerButton;
    private TextView textViewReadingName;


    public ReadableShowUIElements(ReadableShowActivity activity, Readable reading) {
        super(activity);
        this.reading = reading;
        this.textShowAddressAqiValue = (TextView)activity.findViewById(R.id.textShowAddressAqiValue);
        this.textShowAddressAqiRange = (TextView)activity.findViewById(R.id.textShowAddressAqiRange);
        this.textShowAddressAqiTitle = (TextView)activity.findViewById(R.id.textShowAddressAqiTitle);
        this.textShowAddressAqiDescription = (TextView)activity.findViewById(R.id.textShowAddressAqiDescription);
        this.textShowAddressAqiLabel = (TextView)activity.findViewById(R.id.textShowAddressAqiLabel);
        this.layoutShowAddress = (RelativeLayout)activity.findViewById(R.id.layoutShowAddress);
        this.frameAqiButton = (FrameLayout)activity.findViewById(R.id.frameAqiButton);
        this.frameDailyTrackerButton = (FrameLayout)activity.findViewById(R.id.frameDailyTracker);
        this.textViewReadingName = (TextView)activity.findViewById(R.id.textViewReadingName);

        // use custom fonts
        Typeface fontAqi = Typeface.createFromAsset(activity.getAssets(), "fonts/Dosis-Light.ttf");
        textShowAddressAqiValue.setTypeface(fontAqi);

        frameAqiButton.setOnClickListener(activity.frameClickListener);
        frameDailyTrackerButton.setOnClickListener(activity.frameDailyTrackerListener);
    }


    public void populate() {
        if (reading.hasReadableValue()) {
            double readableValue = reading.getReadableValue();

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

        // hide buttons
        frameAqiButton.setVisibility(View.INVISIBLE);
        frameDailyTrackerButton.setVisibility(View.INVISIBLE);
    }


    private void addressView(final SimpleAddress address) {
        int aqi,index;
        double micrograms;

        micrograms = address.getReadableValue();
        aqi = (int) AqiConverter.microgramsToAqi(micrograms);
        this.textShowAddressAqiValue.setText(String.valueOf(aqi));
        AQIReading aqiReading = new AQIReading(micrograms);
        if (aqiReading.withinRange()) {
            textShowAddressAqiRange.setText(aqiReading.getRangeFromIndex() + " AQI");
            textShowAddressAqiTitle.setText(aqiReading.getTitle());
            textShowAddressAqiDescription.setText(aqiReading.getDescription());
            layoutShowAddress.setBackgroundResource(aqiReading.getDrawableGradient());
            textShowAddressAqiLabel.setText(Constants.Units.RANGE_AQI);
            this.textViewReadingName.setText(address.getClosestFeed().getName());
            // request Tracker
            address.requestDailyFeedTracker(activity);
        } else {
            this.defaultView();
        }
    }


    private void speckView(Speck speck) {
//        int index;

        this.textShowAddressAqiValue.setText(String.valueOf((long) speck.getReadableValue()));
//        index = Constants.SpeckReading.getIndexFromReading((long)speck.getReadableValue());
        SpeckReading speckReading = new SpeckReading(speck.getReadableValue());
        if (speckReading.withinRange()) {
            textShowAddressAqiRange.setText(speckReading.getRangeFromIndex() + " Micrograms");
            textShowAddressAqiTitle.setText(speckReading.getTitle());
            textShowAddressAqiDescription.setText(speckReading.getDescription());
            layoutShowAddress.setBackgroundColor(Color.parseColor(speckReading.getColor()));
            textShowAddressAqiLabel.setText(Constants.Units.RANGE_MICROGRAMS_PER_CUBIC_METER);

            // hide buttons
            frameAqiButton.setVisibility(View.INVISIBLE);
            frameDailyTrackerButton.setVisibility(View.INVISIBLE);
        } else {
            this.defaultView();
        }
    }

}
