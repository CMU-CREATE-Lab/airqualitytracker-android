package org.cmucreatelab.tasota.airprototype.activities.address_list.old_list_code;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.address_list.AddressListActivity;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;

/**
 * Created by mike on 6/15/15.
 */
public class RowViewAddressList {

    private final AddressListActivity context;
    private SimpleAddress address;
    private View rowView;
    private TextView textAddressItemCurrentLocation;
    private TextView textAddressItemLocationName;
    private TextView textAddressItemLocationZip;
    private TextView textAddressItemLocationValue;
    private TextView textAddressAqiLabel;
    private FrameLayout frameAddressItemWeatherValue;
    private FrameLayout frameAddressItemWeatherIcon;
    private boolean isCurrentLocation;


    public RowViewAddressList(AddressListActivity context, SimpleAddress address, View rowView) {
        this.context = context;
        this.address = address;
        this.rowView = rowView;
        this.textAddressItemCurrentLocation = (TextView)rowView.findViewById(R.id.textAddressItemCurrentLocation);
        this.textAddressItemLocationName = (TextView)rowView.findViewById(R.id.textAddressItemLocationName);
        this.textAddressItemLocationZip = (TextView)rowView.findViewById(R.id.textAddressItemLocationZip);
        this.textAddressItemLocationValue = (TextView)rowView.findViewById(R.id.textAddressItemLocationValue);
        this.textAddressAqiLabel = (TextView)rowView.findViewById(R.id.textAddressAqiLabel);
        this.frameAddressItemWeatherValue = (FrameLayout)rowView.findViewById(R.id.frameAddressItemWeatherValue);
        this.frameAddressItemWeatherIcon = (FrameLayout)rowView.findViewById(R.id.frameAddressItemWeatherIcon);

        this.isCurrentLocation = address.isCurrentLocation();

        // use custom fonts
        Typeface fontAqi = Typeface.createFromAsset(context.getAssets(), "fonts/Dosis-Light.ttf");
        textAddressItemLocationValue.setTypeface(fontAqi);
    }


    public void populateRowView() {
        // Address or Zipcode info
        textAddressItemLocationName.setText(address.getName());
        textAddressItemLocationZip.setText(address.getZipcode());

        // Display based on whether you are the current location
        if (isCurrentLocation) {
            textAddressItemLocationZip.setVisibility(View.GONE);
            textAddressItemCurrentLocation.setVisibility(View.VISIBLE);
        } else {
            textAddressItemLocationZip.setVisibility(View.VISIBLE);
            textAddressItemCurrentLocation.setVisibility(View.GONE);
        }

        // TODO hides weather (for now)
        frameAddressItemWeatherValue.setVisibility(View.GONE);
        frameAddressItemWeatherIcon.setVisibility(View.GONE);


        // Reported value for closest Feed
        if (address.getClosestFeed() == null) {
            textAddressItemLocationValue.setText("N/A");
        } else {
            double label = Converter.microgramsToAqi(address.getClosestFeed().getFeedValue());
            // TODO looks like we are showing whole numbers?
            textAddressItemLocationValue.setText(String.valueOf((int)label));
        }

        // Description info based on closest feed reading
        if (address.getClosestFeed() == null) {
            textAddressAqiLabel.setVisibility(View.GONE);
            rowView.setBackgroundColor(Color.parseColor("#404041"));
        } else {
            double aqi = Converter.microgramsToAqi(address.getClosestFeed().getFeedValue());
            textAddressAqiLabel.setVisibility(View.VISIBLE);
            int index = Constants.AqiReading.getIndexFromReading(aqi);
            if (index >= 0) {
                try {
                    rowView.setBackgroundColor(Color.parseColor(Constants.AqiReading.aqiColors[index]));
                } catch (Exception e) {
                    // Has to catch failure to parse (0x doesn't work but # does because Java is trash)
                    Log.w(Constants.LOG_TAG, "Failed to parse color " + Constants.SpeckReading.normalColors[index]);
                }
            }
        }
    }

}