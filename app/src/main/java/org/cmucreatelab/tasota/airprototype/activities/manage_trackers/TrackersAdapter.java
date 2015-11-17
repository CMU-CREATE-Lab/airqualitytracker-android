package org.cmucreatelab.tasota.airprototype.activities.manage_trackers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Readable;

import java.util.ArrayList;

/**
 * Created by mike on 11/16/15.
 */
public class TrackersAdapter extends ArrayAdapter<TrackersAdapter.TrackerListItem> {
    public static class TrackerListItem {
        protected boolean isHeader;
        protected String name;
        protected Readable readable;

        public TrackerListItem(String name) {
            isHeader = true;
            this.name = name;
        }
        public TrackerListItem(Readable readable) {
            isHeader = false;
            this.readable = readable;
        }
    }

    private final Context context;
    private final ArrayList<TrackersAdapter.TrackerListItem> values;

    public TrackersAdapter(Context context, ArrayList<TrackersAdapter.TrackerListItem> values) {
        super(context, R.layout.__trackers__manage_trackers_table_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TrackerListItem item = values.get(position);
        View rowView;

        if (item.isHeader) {
            rowView = inflater.inflate(R.layout.__trackers__manage_trackers_table_header, parent, false);

            TextView textViewTrackerHeader = (TextView)rowView.findViewById(R.id.textViewTrackerHeader);
            textViewTrackerHeader.setText(item.name);
        } else {
            rowView = inflater.inflate(R.layout.__trackers__manage_trackers_table_item, parent, false);

            TextView textViewReadingName = (TextView)rowView.findViewById(R.id.textViewReadingName);
            textViewReadingName.setText(item.readable.getName());
        }

        return rowView;
    }

}
