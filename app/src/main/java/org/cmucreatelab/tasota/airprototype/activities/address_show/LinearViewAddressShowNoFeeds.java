package org.cmucreatelab.tasota.airprototype.activities.address_show;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;

/**
 * Created by mike on 6/18/15.
 */
public class LinearViewAddressShowNoFeeds {

    private SimpleAddress address;

    private TextView textShowAddressName;
    private TextView textShowAddressAqiValue;
    private TextView textShowAddressAqiRange;
    private TextView textShowAddressAqiTitle;
    private TextView textShowAddressAqiDescription;
    private RelativeLayout layoutShowAddress;

    //private FrameLayout frameShowAddressWeatherValue, frameShowAddressWeatherIcon;


    public LinearViewAddressShowNoFeeds(AddressShowActivity activity, SimpleAddress address) {
        this.address = address;
        this.textShowAddressName = (TextView)activity.findViewById(R.id.textShowAddressName);
        this.textShowAddressAqiValue = (TextView)activity.findViewById(R.id.textShowAddressAqiValue);
        this.textShowAddressAqiRange = (TextView)activity.findViewById(R.id.textShowAddressAqiRange);
        this.textShowAddressAqiTitle = (TextView)activity.findViewById(R.id.textShowAddressAqiTitle);
        this.textShowAddressAqiDescription = (TextView)activity.findViewById(R.id.textShowAddressAqiDescription);
        this.layoutShowAddress = (RelativeLayout)activity.findViewById(R.id.layoutShowAddress);

        // use custom fonts
        Typeface fontAqi = Typeface.createFromAsset(activity.getAssets(), "fonts/Dosis-Light.ttf");
        textShowAddressAqiValue.setTypeface(fontAqi);

        // TODO testing only
        this.layoutShowAddress.setBackgroundColor(Color.CYAN);
    }


    public void populateLinearView() {
        this.textShowAddressName.setText(this.address.getName());

        if (address.getClosestFeed() != null) {
            int aqiIndex;
            long aqi;

            aqi = (long)Converter.microgramsToAqi(address.getClosestFeed().getFeedValue());
            this.textShowAddressAqiValue.setText(String.valueOf(aqi));
            aqiIndex = Constants.AqiReading.getIndexFromReading(aqi);
            if (aqiIndex < 0) {
                // TODO error readings (default message?)
            } else {
                textShowAddressAqiRange.setText(Constants.AqiReading.getRangeFromIndex(aqiIndex) + " AQI");
                textShowAddressAqiTitle.setText( Constants.AqiReading.titles[aqiIndex] );
                textShowAddressAqiDescription.setText(Constants.AqiReading.descriptions[aqiIndex]);
//                layoutShowAddress.setBackgroundColor(Color.parseColor(Constants.AqiReading.aqiColors[aqiIndex]));
                layoutShowAddress.setBackgroundResource(Constants.AqiReading.aqiDrawableGradients[aqiIndex]);
            }
        } else {
            // TODO error readings (default message?)
            this.textShowAddressAqiValue.setText("N/A");
            textShowAddressAqiRange.setText("");
            textShowAddressAqiTitle.setText("");
            textShowAddressAqiDescription.setText("");
        }
    }

}
