package org.cmucreatelab.tasota.airprototype.activities.manage_trackers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cmucreatelab.tasota.airprototype.R;

import java.util.ArrayList;

/**
 * Created by mike on 11/16/15.
 */
public class TrackersAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public TrackersAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.__trackers__manage_trackers_table_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.__trackers__manage_trackers_table_item, parent, false);

        TextView textViewReadingName = (TextView)rowView.findViewById(R.id.textViewReadingName);
        textViewReadingName.setText("Tracked Name Here");

        return rowView;
    }

}
