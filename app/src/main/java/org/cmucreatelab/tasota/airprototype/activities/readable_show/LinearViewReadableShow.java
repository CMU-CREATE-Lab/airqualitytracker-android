package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;

/**
 * Created by mike on 6/18/15.
 */
public class LinearViewReadableShow {

    private Readable readable;
    private TextView textShowAddressName;
    private TextView textShowAddressAqiValue;
    private TextView textShowAddressAqiRange;
    private TextView textShowAddressAqiTitle;
    private TextView textShowAddressAqiDescription;
    private TextView textShowAddressAqiLabel;
    private RelativeLayout layoutShowAddress;


    private void defaultView() {
        Log.w(Constants.LOG_TAG, "warning: using default populateLinearView");
        this.textShowAddressAqiValue.setText("");
        textShowAddressAqiRange.setText("");
        textShowAddressAqiLabel.setVisibility(View.INVISIBLE);
        textShowAddressAqiTitle.setText(Constants.DefaultReading.DEFAULT_TITLE);
        textShowAddressAqiDescription.setText(Constants.DefaultReading.DEFAULT_DESCRIPTION);
        layoutShowAddress.setBackgroundColor(Color.parseColor(Constants.DefaultReading.DEFAULT_COLOR_BACKGROUND));
    }


    private void addressView(double readableValue) {
        int aqi,index;

        aqi = (int) Converter.microgramsToAqi(readableValue);
        this.textShowAddressAqiValue.setText(String.valueOf(aqi));
        index = Constants.AqiReading.getIndexFromReading(aqi);
        if (index < 0) {
            this.defaultView();
        } else {
            textShowAddressAqiRange.setText(Constants.AqiReading.getRangeFromIndex(index) + " AQI");
            textShowAddressAqiTitle.setText(Constants.AqiReading.titles[index]);
            textShowAddressAqiDescription.setText(Constants.AqiReading.descriptions[index]);
            layoutShowAddress.setBackgroundResource(Constants.AqiReading.aqiDrawableGradients[index]);
            textShowAddressAqiLabel.setText(Constants.Units.RANGE_AQI);
        }
    }


    private void speckView(double readableValue) {
        int index;

        this.textShowAddressAqiValue.setText(String.valueOf((long)readableValue));
        index = Constants.SpeckReading.getIndexFromReading((long)readableValue);
        if (index < 0) {
            this.defaultView();
        } else {
            textShowAddressAqiRange.setText(Constants.SpeckReading.getRangeFromIndex(index) + " Micrograms");
            textShowAddressAqiTitle.setText(Constants.SpeckReading.titles[index]);
            // TODO descriptions for speck
//                    textShowAddressAqiDescription.setText(Constants.SpeckReading.descriptions[ugIndex]);
            textShowAddressAqiDescription.setText(Constants.AqiReading.descriptions[index]);
            layoutShowAddress.setBackgroundColor(Color.parseColor(Constants.SpeckReading.normalColors[index]));
            textShowAddressAqiLabel.setText(Constants.Units.RANGE_MICROGRAMS_PER_CUBIC_METER);
        }
    }


    public LinearViewReadableShow(ReadableShowActivity activity, Readable readable) {
        this.readable = readable;
        this.textShowAddressName = (TextView)activity.findViewById(R.id.textShowAddressName);
        this.textShowAddressAqiValue = (TextView)activity.findViewById(R.id.textShowAddressAqiValue);
        this.textShowAddressAqiRange = (TextView)activity.findViewById(R.id.textShowAddressAqiRange);
        this.textShowAddressAqiTitle = (TextView)activity.findViewById(R.id.textShowAddressAqiTitle);
        this.textShowAddressAqiDescription = (TextView)activity.findViewById(R.id.textShowAddressAqiDescription);
        this.textShowAddressAqiLabel = (TextView)activity.findViewById(R.id.textShowAddressAqiLabel);
        this.layoutShowAddress = (RelativeLayout)activity.findViewById(R.id.layoutShowAddress);

        // use custom fonts
        Typeface fontAqi = Typeface.createFromAsset(activity.getAssets(), "fonts/Dosis-Light.ttf");
        textShowAddressAqiValue.setTypeface(fontAqi);
    }


    public void populateLinearView() {
        this.textShowAddressName.setText(readable.getName());

        if (readable.hasReadableValue()) {
            double readableValue = readable.getReadableValue();

            if (readable.getReadableType() == Readable.Type.ADDRESS) {
                addressView(readableValue);
            } else if (readable.getReadableType() == Readable.Type.SPECK) {
                speckView(readableValue);
            } else {
                Log.w(Constants.LOG_TAG, "Tried to populate LinearView in AddressShow with unimplemented Readable object.");
                defaultView();
            }
        } else {
            defaultView();
        }
    }

}
