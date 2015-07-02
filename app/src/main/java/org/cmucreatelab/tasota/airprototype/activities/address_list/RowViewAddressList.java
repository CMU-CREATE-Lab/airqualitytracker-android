package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

/**
 * Created by mike on 6/15/15.
 */
public class RowViewAddressList {

    private final AddressListActivity context;
    private SimpleAddress address;
    private View rowView;
    private TextView textAddressItemName;
    private TextView textAddressItemValue;
    private TextView textAddressItemDescription;
    private ImageView textAddressItemIcon;


    public RowViewAddressList(AddressListActivity context, SimpleAddress address, View rowView) {
        this.context = context;
        this.address = address;
        this.rowView = rowView;
        this.textAddressItemName = (TextView)rowView.findViewById(R.id.textAddressItemName);
        this.textAddressItemValue = (TextView)rowView.findViewById(R.id.textAddressItemValue);
        this.textAddressItemDescription = (TextView)rowView.findViewById(R.id.textAddressItemDescription);
        this.textAddressItemIcon = (ImageView)rowView.findViewById(R.id.textAddressItemIcon);
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

        // set icon
        if (address.getIconType() == SimpleAddress.IconType.GPS) {
            textAddressItemIcon.setImageResource(R.drawable.ic_track_changes);
        } else if (address.getIconType() == SimpleAddress.IconType.SPECK) {
            textAddressItemIcon.setImageResource(R.drawable.speck_icon_black);
        } else if (address.getIconType() == SimpleAddress.IconType.DEFAULT) {
            textAddressItemIcon.setImageResource(R.drawable.speck_icon_black);
        } else {
            Log.e(Constants.LOG_TAG,"Unknown IconType!");
        }

        // Description info based on closest feed reading
        if (address.getClosestFeed() == null) {
            textAddressItemDescription.setText( "" );
            textAddressItemDescription.setBackgroundColor(Color.TRANSPARENT);
        } else {
            int index = Constants.SpeckReading.getIndexFromReading(address.getClosestFeed().getFeedValue());
            if (index >= 0) {
                textAddressItemDescription.setText(Constants.SpeckReading.descriptions[index]);
                try {
                    if (GlobalHandler.getInstance(context.getApplicationContext()).settingsHandler.isColorblindMode()) {
                        textAddressItemDescription.setBackgroundColor(Color.parseColor(Constants.SpeckReading.colorblindColors[index]));
                        textAddressItemDescription.setTextColor(Color.WHITE);
//                        textAddressItemDescription.setTextColor(Color.parseColor(Constants.SpeckReading.colorblindColors[index]));
                    } else {
                        textAddressItemDescription.setBackgroundColor(Color.parseColor(Constants.SpeckReading.normalColors[index]));
                        textAddressItemDescription.setTextColor(Color.WHITE);
//                        textAddressItemDescription.setTextColor(Color.parseColor(Constants.SpeckReading.normalColors[index]));
                    }
                } catch (Exception e) {
                    // Has to catch failure to parse (0x doesn't work but # does because Java is trash)
                    Log.w(Constants.LOG_TAG, "Failed to parse color " + Constants.SpeckReading.normalColors[index]);
                }
            }
        }
    }

}
