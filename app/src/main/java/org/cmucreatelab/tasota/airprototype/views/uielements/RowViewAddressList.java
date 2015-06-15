package org.cmucreatelab.tasota.airprototype.views.uielements;

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
            textView.setText(row.getClosestFeed().getFeedDisplay());
        }

        // Description info based on closest feed reading
        textView = (TextView)rowView.findViewById(R.id.textAddressItemDescription);
        if (row.getClosestFeed() == null) {
            textView.setText( "" );
        } else {
            int reading = row.getClosestFeed().getFeedValue();
            textView.setText( Constants.SpeckReading.descriptions[Constants.SpeckReading.getIndexFromReading(reading)] );
            // TODO set display color
        }
    }

}
