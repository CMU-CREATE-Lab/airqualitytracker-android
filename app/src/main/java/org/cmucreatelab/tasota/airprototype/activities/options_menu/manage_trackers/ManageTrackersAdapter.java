package org.cmucreatelab.tasota.airprototype.activities.options_menu.manage_trackers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.readables.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mike on 11/16/15.
 */
public class ManageTrackersAdapter extends ArrayAdapter<ManageTrackersAdapter.TrackerListItem> {

    public static class TrackerListItem {
        public boolean isHeader;
        protected String name;
        public Readable readable;
        public boolean hidden = false;
        public View view;

        public TrackerListItem(String name) {
            isHeader = true;
            this.name = name;
        }
        public TrackerListItem(Readable readable) {
            isHeader = false;
            this.readable = readable;
        }
    }

    private final ManageTrackersActivity context;
    final int INVALID_ID = -1;
    HashMap<ManageTrackersAdapter.TrackerListItem, Integer> mIdMap = new HashMap<ManageTrackersAdapter.TrackerListItem, Integer>();


    public ManageTrackersAdapter(ManageTrackersActivity context, ArrayList<ManageTrackersAdapter.TrackerListItem> values) {
        super(context, R.layout.__options_menu____manage_trackers__table_item, values);
        this.context = context;
        for (int i = 0; i < values.size(); ++i) {
            mIdMap.put(values.get(i), i);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final TrackerListItem item = getItem(position);
        View rowView;

        if (item.isHeader) {
            rowView = inflater.inflate(R.layout.__options_menu____manage_trackers__table_header, parent, false);

            TextView textViewTrackerHeader = (TextView)rowView.findViewById(R.id.textViewTrackerHeader);
            textViewTrackerHeader.setText(item.name);
        } else {
            rowView = inflater.inflate(R.layout.__options_menu____manage_trackers__table_item, parent, false);

            TextView textViewReadingName = (TextView)rowView.findViewById(R.id.textViewReadingName);
            textViewReadingName.setText(item.readable.getName());

            FrameLayout frameTrackerMove = (FrameLayout)rowView.findViewById(R.id.frameTrackerMove);
            frameTrackerMove.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.i(Constants.LOG_TAG, "Touched Frame!");
                    // this is why we need context to be specific to ManageTrackersActivity
                    context.uiElements.listViewTrackers.startListMovementFromItem(item);
                    return false;
                }
            });

            FrameLayout frameTrackerDelete = (FrameLayout)rowView.findViewById(R.id.frameTrackerDelete);
            frameTrackerDelete.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.i(Constants.LOG_TAG, "Touched (delete) Frame!");
                    context.showDeleteDialog(item);
                    return false;
                }
            });

            FrameLayout frameTrackerEdit = (FrameLayout)rowView.findViewById(R.id.frameTrackerEdit);
            frameTrackerEdit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.i(Constants.LOG_TAG, "Touched (edit) Frame!");
                    context.showEditDialog(item);
                    return false;
                }
            });
        }
        if (item.hidden) {
            rowView.setVisibility(View.INVISIBLE);
        }

        item.view = rowView;
        return rowView;
    }


    public void unhideAllListItems() {
        for (TrackerListItem item: mIdMap.keySet()) {
            item.hidden = false;
        }
    }


    public ManageTrackersAdapter(ManageTrackersActivity context, int textViewResourceId, List<ManageTrackersAdapter.TrackerListItem> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }


    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        ManageTrackersAdapter.TrackerListItem item = getItem(position);
        return mIdMap.get(item);
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

}
