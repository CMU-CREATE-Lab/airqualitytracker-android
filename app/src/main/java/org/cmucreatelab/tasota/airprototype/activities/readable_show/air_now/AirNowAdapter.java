package org.cmucreatelab.tasota.airprototype.activities.readable_show.air_now;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.AirNowObservation;
import org.cmucreatelab.tasota.airprototype.classes.aqi_scales.AQIReading;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.AqiConverter;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 3/11/16.
 */
public class AirNowAdapter extends ArrayAdapter<AirNowAdapter.AirNowItem> {

    public static class AirNowItem {
        public boolean isHeader;
        public AirNowObservation observation;
        public String title; // for headers only
        int index; // for headers only

        public AirNowItem(String title, int index) {
            this.isHeader = true;
            this.title = title;
            this.index = index;
        }
        public AirNowItem(AirNowObservation observation) {
            this.isHeader = false;
            this.observation = observation;
        }
    }

    private AirNowActivity context;


    public AirNowAdapter(AirNowActivity context, ArrayList<AirNowItem> values) {
        super(context, R.layout.__readable_show____air_now__activity, values);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final AirNowItem item = getItem(position);
        View rowView;

        if (item.isHeader) {
            rowView = inflater.inflate(R.layout.__readable_show____air_now__list_header, parent, false);

            // header title
            TextView textViewAirNowListHeader = (TextView)rowView.findViewById(R.id.textViewAirNowListHeader);
            textViewAirNowListHeader.setText(item.title);
        } else {
            rowView = inflater.inflate(R.layout.__readable_show____air_now__list_item, parent, false);

            double aqi = item.observation.getAqi();

            // AQI
            TextView textViewAqi = (TextView)rowView.findViewById(R.id.textViewAqi);
            textViewAqi.setText(String.valueOf(aqi));

            // Parameter Name
            TextView textViewParamName = (TextView)rowView.findViewById(R.id.textViewParamName);
            textViewParamName.setText(item.observation.getParameterName());

            // AQI Color
            FrameLayout frameColored = (FrameLayout)rowView.findViewById(R.id.frameColored);
            frameColored.setBackgroundColor(Color.parseColor(new AQIReading(AqiConverter.aqiToMicrograms(aqi)).getColor()));
        }

        return rowView;
    }

}
