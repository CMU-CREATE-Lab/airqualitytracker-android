package org.cmucreatelab.tasota.airprototype.views.uielements;

import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.MapGeometry;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressShowActivity;
import java.util.Date;

/**
 * Created by mike on 6/18/15.
 */
public class LinearViewAddressShow {

    private SimpleAddress address;
    private TextView textShowAddressName;
    private TextView textShowAddressLat;
    private TextView textShowAddressLong;
    private TextView textShowAddressClosestFeed;
    private TextView textShowAddressDistance;
    private TextView textShowAddressMeasurement;
    private TextView textShowAddressLastUpdatedAt;


    public LinearViewAddressShow(AddressShowActivity activity, SimpleAddress address) {
        this.address = address;
        this.textShowAddressName = (TextView)activity.findViewById(R.id.textShowAddressName);
        this.textShowAddressLat = (TextView)activity.findViewById(R.id.textShowAddressLat);
        this.textShowAddressLong = (TextView)activity.findViewById(R.id.textShowAddressLong);
        this.textShowAddressClosestFeed = (TextView)activity.findViewById(R.id.textShowAddressClosestFeed);
        this.textShowAddressDistance = (TextView)activity.findViewById(R.id.textShowAddressDistance);
        this.textShowAddressMeasurement = (TextView)activity.findViewById(R.id.textShowAddressMeasurement);
        this.textShowAddressLastUpdatedAt = (TextView)activity.findViewById(R.id.textShowAddressLastUpdatedAt);
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
        if (address.getClosestFeed() != null) {
            double distance = MapGeometry.getDistanceFromFeedToAddress(address, address.getClosestFeed());
            distance = (int)(100*distance)/100.0;
            Date date = new Date();
            date.setTime((long)(1000*address.getClosestFeed().getLastTime()));

            this.textShowAddressDistance.setText(String.valueOf(distance)+" km");
            this.textShowAddressMeasurement.setText(String.valueOf(address.getClosestFeed().getFeedValue()));
            this.textShowAddressLastUpdatedAt.setText(date.toString());
        } else {
            this.textShowAddressDistance.setText("N/A");
            this.textShowAddressMeasurement.setText("N/A");
            this.textShowAddressLastUpdatedAt.setText("N/A");
        }
    }

}
