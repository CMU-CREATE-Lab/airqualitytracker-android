package org.cmucreatelab.tasota.airprototype.activities.address_show;

import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.MapGeometry;
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
    private TextView textShowAddressAqi;
    private TextView textShowAddressLastUpdatedAt;


    public LinearViewAddressShow(AddressShowActivity activity, SimpleAddress address) {
        this.address = address;
        this.textShowAddressName = (TextView)activity.findViewById(R.id.textShowAddressName);
        this.textShowAddressLat = (TextView)activity.findViewById(R.id.textShowAddressLat);
        this.textShowAddressLong = (TextView)activity.findViewById(R.id.textShowAddressLong);
        this.textShowAddressClosestFeed = (TextView)activity.findViewById(R.id.textShowAddressClosestFeed);
        this.textShowAddressDistance = (TextView)activity.findViewById(R.id.textShowAddressDistance);
        this.textShowAddressMeasurement = (TextView)activity.findViewById(R.id.textShowAddressMeasurement);
        this.textShowAddressAqi = (TextView)activity.findViewById(R.id.textShowAddressAqi);
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
            double distance,micrograms,aqi;
            Date date = new Date();

            distance = MapGeometry.getDistanceFromFeedToAddress(address, address.getClosestFeed());
            distance = (int)(100*distance)/100.0;
            date.setTime((long)(1000*address.getClosestFeed().getLastTime()));
            micrograms = address.getClosestFeed().getFeedValue();
            aqi = (long)(100*Converter.microgramsToAqi(micrograms)) / 100.0;

            this.textShowAddressDistance.setText(String.valueOf(distance)+" km");
            this.textShowAddressMeasurement.setText(String.valueOf(micrograms));
            this.textShowAddressAqi.setText(String.valueOf(aqi));
            this.textShowAddressLastUpdatedAt.setText(date.toString());
        } else {
            this.textShowAddressDistance.setText("N/A");
            this.textShowAddressMeasurement.setText("N/A");
            this.textShowAddressLastUpdatedAt.setText("N/A");
        }
    }

}
