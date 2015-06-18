package org.cmucreatelab.tasota.airprototype.views.uielements;

import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressShowActivity;

/**
 * Created by mike on 6/18/15.
 */
public class LinearViewAddressShow {

    private SimpleAddress address;
    private TextView textShowAddressName;
    private TextView textShowAddressLat;
    private TextView textShowAddressLong;
    private TextView textShowAddressClosestFeed;


    public LinearViewAddressShow(AddressShowActivity activity, SimpleAddress address) {
        this.address = address;
        this.textShowAddressName = (TextView)activity.findViewById(R.id.textShowAddressName);
        this.textShowAddressLat = (TextView)activity.findViewById(R.id.textShowAddressLat);
        this.textShowAddressLong = (TextView)activity.findViewById(R.id.textShowAddressLong);
        this.textShowAddressClosestFeed = (TextView)activity.findViewById(R.id.textShowAddressClosestFeed);
    }


    public void populateLinearView() {
        this.textShowAddressName.setText(this.address.getName());
        this.textShowAddressLat.setText(String.valueOf(this.address.getLatitude()));
        this.textShowAddressLong.setText(String.valueOf(this.address.getLongitude()));
        if (this.address.getClosestFeed() == null) {
            this.textShowAddressClosestFeed.setText("null");
        } else {
            this.textShowAddressClosestFeed.setText(String.valueOf(this.address.getClosestFeed().getName()));
        }
    }

}
