package org.cmucreatelab.tasota.airprototype.helpers;

import org.cmucreatelab.tasota.airprototype.activities.manage_trackers.TrackersAdapter;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.activities.secret_menu.ListFeedsAdapter;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.abstract_classes.ReadingsHandlerEditable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.GpsReadingHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.SpeckDbHelper;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mike on 12/22/15.
 */
public class ReadingsHandler extends ReadingsHandlerEditable {

    public GpsReadingHandler gpsReadingHandler;


    public ReadingsHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.gpsReadingHandler = new GpsReadingHandler(globalHandler);
        // headers
        Collections.addAll(headers, Constants.HEADER_TITLES);
    }


    public void populateAdapterList() {
        // sectionFirstPosition marks the position of the header in each section
        int headerCount=0,sectionFirstPosition,position=0;
        adapterList.clear();
        trackerList.clear();
        debugFeedsList.clear();

        // adapterList
        for (String header : headers) {
            ArrayList<org.cmucreatelab.tasota.airprototype.classes.Readable> items = new ArrayList<>();
            items.addAll((ArrayList<Readable>)hashMap.get(header));
            // TODO had to add Null pointer check
            if (globalHandler.settingsHandler.appUsesLocation() && header == Constants.HEADER_TITLES[1]) {
                items.add(0,gpsReadingHandler.gpsAddress);
            }
            // only display headers with non-empty contents
            if (items.size() > 0) {
                sectionFirstPosition = headerCount + position;
                headerCount += 1;
                adapterList.add(new StickyGridAdapter.LineItem(header, true, sectionFirstPosition));

                // grid cells
                for (Readable r : items) {
                    position += 1;
                    adapterList.add(new StickyGridAdapter.LineItem(false, sectionFirstPosition, r));
                }
            }
        }
        // trackerList
        for (String header : headers) {
            ArrayList<Readable> items = (ArrayList<Readable>)hashMap.get(header);
            // only display headers with non-empty contents
            if (items.size() > 0) {
                trackerList.add( new TrackersAdapter.TrackerListItem(header) );

                // grid cells
                for (Readable r : items) {
                    trackerList.add( new TrackersAdapter.TrackerListItem(r) );
                }
            }
        }
        // debugFeedsList
        // TODO this doesn't get updated enough (doesn't list all the feeds until you refresh)
        int index=0;
        // TODO had to add Null pointer check
        if (gpsReadingHandler != null && globalHandler.settingsHandler.appUsesLocation()) {
            debugFeedsList.add( new ListFeedsAdapter.ListFeedsItem(gpsReadingHandler.gpsAddress, index++) );
            for (Feed feed : gpsReadingHandler.gpsAddress.feeds) {
                debugFeedsList.add( new ListFeedsAdapter.ListFeedsItem(gpsReadingHandler.gpsAddress, feed) );
            }
        }
        for (SimpleAddress address : addresses) {
            debugFeedsList.add( new ListFeedsAdapter.ListFeedsItem(address, index++) );
            for (Feed feed : address.feeds) {
                debugFeedsList.add( new ListFeedsAdapter.ListFeedsItem(address, feed) );
            }
        }
    }


    @Override
    public void refreshHash() {
        super.refreshHash();

        this.populateAdapterList();
        globalHandler.notifyGlobalDataSetChanged();
    }

}
