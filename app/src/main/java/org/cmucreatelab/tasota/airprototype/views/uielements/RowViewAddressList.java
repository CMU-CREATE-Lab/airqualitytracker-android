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

    private SimpleAddress row;
    private View rowView;


    public RowViewAddressList(SimpleAddress row, View rowView) {
        this.row = row;
        this.rowView = rowView;
    }


    public void populateRowView() {
        TextView textView;

        // Address or Zipcode info
        textView = (TextView)rowView.findViewById(R.id.textAddressItemName);
        textView.setText(row.getName());

        // Reported value for closest Feed
        textView = (TextView)rowView.findViewById(R.id.textAddressItemValue);
        if (row.getClosestFeed() == null) {
            textView.setText("N/A");
        } else {
            textView.setText(row.getClosestFeed().getFeedValue() + " µg/m³");
        }

        // Description info based on closest feed reading
        textView = (TextView)rowView.findViewById(R.id.textAddressItemDescription);
        if (row.getClosestFeed() == null) {
            textView.setText( "" );
        } else {
            int index = Constants.SpeckReading.getIndexFromReading(row.getClosestFeed().getFeedValue());
            if (index >= 0) {
                textView.setText(Constants.SpeckReading.descriptions[index]);
                try {
                    // TODO colorblind check?
                    textView.setTextColor(Color.parseColor(Constants.SpeckReading.normalColors[index]));
                } catch (Exception e) {
                    // Has to catch failure to parse (0x doesn't work but # does because Java is trash)
                    Log.w(Constants.LOG_TAG, "Failed to parse color " + Constants.SpeckReading.normalColors[index].toString());
                }
            }
        }
    }

}
