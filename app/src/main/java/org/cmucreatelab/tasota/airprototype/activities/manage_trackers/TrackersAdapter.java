package org.cmucreatelab.tasota.airprototype.activities.manage_trackers;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mike on 11/16/15.
 */
public class TrackersAdapter extends ArrayAdapter<TrackersAdapter.TrackerListItem> {
    public static class TrackerListItem {
        public boolean isHeader;
        protected String name;
        protected Readable readable;
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
//    public final ArrayList<TrackersAdapter.TrackerListItem> values;

    public TrackersAdapter(ManageTrackersActivity context, ArrayList<TrackersAdapter.TrackerListItem> values) {
        super(context, R.layout.__trackers__manage_trackers_table_item, values);
        this.context = context;
//        this.values = values;
        for (int i = 0; i < values.size(); ++i) {
            mIdMap.put(values.get(i), i);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        TrackerListItem item = values.get(position);
        final TrackerListItem item = getItem(position);
        View rowView;

        if (item.isHeader) {
            rowView = inflater.inflate(R.layout.__trackers__manage_trackers_table_header, parent, false);

            TextView textViewTrackerHeader = (TextView)rowView.findViewById(R.id.textViewTrackerHeader);
            textViewTrackerHeader.setText(item.name);
        } else {
            rowView = inflater.inflate(R.layout.__trackers__manage_trackers_table_item, parent, false);

            TextView textViewReadingName = (TextView)rowView.findViewById(R.id.textViewReadingName);
            textViewReadingName.setText(item.readable.getName());

            FrameLayout frameTrackerMove = (FrameLayout)rowView.findViewById(R.id.frameTrackerMove);
            frameTrackerMove.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.i(Constants.LOG_TAG, "Touched Frame!");
                    // this is why we need context to be specific to ManageTrackersActivity
                    context.listViewTrackers.startListMovementFromItem(item);
                    return false;
                }
            });

//            rowView.setTag("icon bitmap");
//            FrameLayout frameTrackerMove = (FrameLayout)rowView.findViewById(R.id.frameTrackerMove);
//
//            frameTrackerMove.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    View parentView = (View)view.getParent();
//                    Log.i(Constants.LOG_TAG, "TOUCH EVENT");
//                    ClipData.Item item = new ClipData.Item((String)parentView.getTag());
//                    String[] mimeTypes = {
//                            ClipDescription.MIMETYPE_TEXT_PLAIN
//                    };
//                    ClipData dragData = new ClipData((String)parentView.getTag(),mimeTypes,item);
//                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(parentView);
//
//                    parentView.startDrag(dragData,myShadow,null,0);
//                    return false;
//                }
//            });
//            rowView.setOnDragListener(new View.OnDragListener() {
//                @Override
//                public boolean onDrag(View view, DragEvent dragEvent) {
//                    Log.i(Constants.LOG_TAG, "DRAG: " + dragEvent.toString());
//                    return false;
//                }
//            });
        }
        if (item.hidden) {
            rowView.setVisibility(View.INVISIBLE);
//            item.hidden = false;
        }

        item.view = rowView;
        return rowView;
    }


    final int INVALID_ID = -1;

    HashMap<TrackersAdapter.TrackerListItem, Integer> mIdMap = new HashMap<TrackersAdapter.TrackerListItem, Integer>();

    // TODO helper to unhide all views
    public void unhideAllListItems() {
        for (TrackerListItem item: mIdMap.keySet()) {
            item.hidden = false;
        }
    }

    public TrackersAdapter(ManageTrackersActivity context, int textViewResourceId, List<TrackersAdapter.TrackerListItem> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    // TODO check how this method works
    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        TrackersAdapter.TrackerListItem item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
