package org.cmucreatelab.tasota.airprototype.activities.address_show;

import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;

/**
 * Created by mike on 6/18/15.
 */
public class LinearViewAddressShow {

//    private SimpleAddress address;
    private Readable readable;

    private TextView textShowAddressName;
    private TextView textShowAddressAqiValue;
    private TextView textShowAddressAqiRange;
    private TextView textShowAddressAqiTitle;
    private TextView textShowAddressAqiDescription;
    private TextView textShowAddressAqiLabel;
    private RelativeLayout layoutShowAddress;

    //private FrameLayout frameShowAddressWeatherValue, frameShowAddressWeatherIcon;


    private void defaultView() {
        // TODO change default message?
        Log.w(Constants.LOG_TAG, "warning: using default populateLinearView");
        this.textShowAddressAqiValue.setText("");
        textShowAddressAqiRange.setText("");
        textShowAddressAqiLabel.setVisibility(View.INVISIBLE);
        textShowAddressAqiTitle.setText("Unavailable");
        textShowAddressAqiDescription.setText("The current AQI for this region is unavailable.");
//        layoutShowAddress.setBackgroundColor(Color.parseColor("#f1f1f2"));
//        layoutShowAddress.setBackgroundResource(R.drawable.gradient_0);
        layoutShowAddress.setBackgroundColor(Color.parseColor("#404041"));
    }


    public LinearViewAddressShow(AddressShowActivity activity, Readable readable) {
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

        this.layoutShowAddress.setBackgroundColor(Color.parseColor("#f1f1f2"));
    }


    public void populateLinearView() {
        this.textShowAddressName.setText(this.readable.getName());
        double readableValue = readable.getReadableValue();

        if (readable.getReadableType() == Readable.Type.ADDRESS) {
            if (readable.hasReadableValue()) {
                int aqiIndex;
                long aqi;

                aqi = (long) Converter.microgramsToAqi(readableValue);
                this.textShowAddressAqiValue.setText(String.valueOf(aqi));
                aqiIndex = Constants.AqiReading.getIndexFromReading(aqi);
                if (aqiIndex < 0) {
                    this.defaultView();
                } else {
                    textShowAddressAqiRange.setText(Constants.AqiReading.getRangeFromIndex(aqiIndex) + " AQI");
                    textShowAddressAqiTitle.setText(Constants.AqiReading.titles[aqiIndex]);
                    textShowAddressAqiDescription.setText(Constants.AqiReading.descriptions[aqiIndex]);
                    //                layoutShowAddress.setBackgroundColor(Color.parseColor(Constants.AqiReading.aqiColors[aqiIndex]));
                    layoutShowAddress.setBackgroundResource(Constants.AqiReading.aqiDrawableGradients[aqiIndex]);
                }
            } else {
                this.defaultView();
            }
        } else if (readable.getReadableType() == Readable.Type.SPECK) {
            if (readable.hasReadableValue()) {
                int ugIndex;

                this.textShowAddressAqiValue.setText(String.valueOf((long)readableValue));
                ugIndex = Constants.AqiReading.getIndexFromReading((long)readableValue);
                if (ugIndex < 0) {
                    this.defaultView();
                } else {
                    textShowAddressAqiRange.setText(Constants.SpeckReading.getRangeFromIndex(ugIndex) + " Micrograms");
                    textShowAddressAqiTitle.setText(Constants.SpeckReading.titles[ugIndex]);
                    // TODO descriptions for speck
//                    textShowAddressAqiDescription.setText(Constants.SpeckReading.descriptions[ugIndex]);
                    textShowAddressAqiDescription.setText(Constants.AqiReading.descriptions[ugIndex]);
                    layoutShowAddress.setBackgroundColor(Color.parseColor(Constants.SpeckReading.normalColors[ugIndex]));
                }
            } else {
                this.defaultView();
            }
        } else {
            Log.w(Constants.LOG_TAG, "Tried to populate LinearView in AddressShow with unimplemented Readable object.");
            this.defaultView();
        }
    }

}
