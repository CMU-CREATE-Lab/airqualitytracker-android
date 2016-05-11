package org.cmucreatelab.tasota.airprototype.classes;

import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.AqiConverter;

import java.util.ArrayList;

/**
 * Created by mike on 5/9/16.
 */
public class DailyFeedTracker {

    // class attributes
    private final Feed feed;
    private final long from,to;
    private final ArrayList<DayFeedValue> values = new ArrayList<>();
    // getters/setters
    public Feed getFeed() { return feed; }
    public ArrayList<DayFeedValue> getValues() { return values; }


    public DailyFeedTracker(Feed feed, long from, long to) {
        this.feed = feed;
        this.from = from;
        this.to = to;
    }


    public int getDirtyDaysCount() {
        int size = 0;

        for (DayFeedValue feedValue : values) {
            if (AqiConverter.microgramsToAqi(feedValue.getDirtyDaysValue()) > 50) {
                size++;
            }
        }

        return size;
    }

}
