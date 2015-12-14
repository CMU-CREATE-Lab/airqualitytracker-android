package org.cmucreatelab.tasota.airprototype.activities.secret_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.manage_trackers.ManageTrackersActivity;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.MapGeometry;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by mike on 12/10/15.
 */
public class ListFeedsAdapter extends ArrayAdapter<ListFeedsAdapter.ListFeedsItem> {
    public static class ListFeedsItem {
        public boolean isHeader;
        public SimpleAddress address;
        int index; // for headers only
        public Feed feed;

        public ListFeedsItem(SimpleAddress address, int index) {
            this.isHeader = true;
            this.address = address;
            this.index = index;
        }
        public ListFeedsItem(SimpleAddress address, Feed feed) {
            this.isHeader = false;
            this.address = address;
            this.feed = feed;
        }
    }

    private DebugActivity context;

//    // header
//    private TextView textViewListFeedsHeader;
//    // non-header
//    private TextView textListFeedsLatitude;
//    private TextView textListFeedsLongitude;
//    private TextView textListFeedsFeedId;
//    private TextView textListFeedsFeedName;
//    private TextView textListFeedsDistance;
//    private TextView textListFeedsFeedValue;


    public ListFeedsAdapter(DebugActivity context, ArrayList<ListFeedsAdapter.ListFeedsItem> values) {
        super(context, R.layout.__secret_menu__debug_activity, values);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ListFeedsItem item = getItem(position);
        View rowView;

        if (item.isHeader) {
            rowView = inflater.inflate(R.layout.__secret_menu__debug_activity_list_header, parent, false);

            // header title
            TextView textViewListFeedsHeader = (TextView)rowView.findViewById(R.id.textViewListFeedsHeader);
            textViewListFeedsHeader.setText(String.valueOf(item.index)+" ("+item.address.getLatitude()+","+item.address.getLongitude()+")");
        } else {
            rowView = inflater.inflate(R.layout.__secret_menu__debug_activity_list_item, parent, false);

            // feed lat
            TextView textListFeedsLatitude = (TextView)rowView.findViewById(R.id.textListFeedsLatitude);
            textListFeedsLatitude.setText(String.valueOf(item.feed.getLatitude()));
            // feed long
            TextView textListFeedsLongitude = (TextView)rowView.findViewById(R.id.textListFeedsLongitude);
            textListFeedsLongitude.setText(String.valueOf(item.feed.getLongitude()));
            // feed ID
            TextView textListFeedsFeedId = (TextView)rowView.findViewById(R.id.textListFeedsFeedId);
            textListFeedsFeedId.setText("id="+String.valueOf(item.feed.getFeed_id()));
            // feed name
            TextView textListFeedsFeedName = (TextView)rowView.findViewById(R.id.textListFeedsFeedName);
            textListFeedsFeedName.setText(String.valueOf(item.feed.getName()));
            // textListFeedsDistance;
            double distance = ((int)(100 * MapGeometry.getDistanceFromFeedToAddress(item.address, item.feed)))/100.0;
            TextView textListFeedsDistance = (TextView)rowView.findViewById(R.id.textListFeedsDistance);
            textListFeedsDistance.setText(String.valueOf(distance)+" mi");
            // feed value
            TextView textListFeedsFeedValue = (TextView)rowView.findViewById(R.id.textListFeedsFeedValue);
            textListFeedsFeedValue.setText(String.valueOf(item.feed.getFeedValue()));
        }

        return rowView;
    }

}