package org.cmucreatelab.tasota.airprototype.views.uielements;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;

/**
 * Created by mike on 6/15/15.
 */
public class RowViewAddressList {

    private SimpleAddress address;
    private View rowView;
    private TextView textAddressItemName;
    private TextView textAddressItemValue;
    private TextView textAddressItemDescription;


    public RowViewAddressList(SimpleAddress address, View rowView) {
        this.address = address;
        this.rowView = rowView;
        this.textAddressItemName = (TextView)rowView.findViewById(R.id.textAddressItemName);
        this.textAddressItemValue = (TextView)rowView.findViewById(R.id.textAddressItemValue);
        this.textAddressItemDescription = (TextView)rowView.findViewById(R.id.textAddressItemDescription);
    }


    public void populateRowView() {
        // Address or Zipcode info
        textAddressItemName.setText(address.getName());

        // Reported value for closest Feed
        if (address.getClosestFeed() == null) {
            textAddressItemValue.setText("N/A");
        } else {
            textAddressItemValue.setText(address.getClosestFeed().getFeedValue() + " µg/m³");
        }

        // Description info based on closest feed reading
        if (address.getClosestFeed() == null) {
            textAddressItemDescription.setText( "" );
        } else {
            int index = Constants.SpeckReading.getIndexFromReading(address.getClosestFeed().getFeedValue());
            if (index >= 0) {
                textAddressItemDescription.setText(Constants.SpeckReading.descriptions[index]);
                try {
                    // TODO colorblind check?
                    textAddressItemDescription.setTextColor(Color.parseColor(Constants.SpeckReading.normalColors[index]));
                } catch (Exception e) {
                    // Has to catch failure to parse (0x doesn't work but # does because Java is trash)
                    Log.w(Constants.LOG_TAG, "Failed to parse color " + Constants.SpeckReading.normalColors[index]);
                }
            }
        }
    }

}
