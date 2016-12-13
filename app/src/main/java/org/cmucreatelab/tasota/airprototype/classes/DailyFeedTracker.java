package org.cmucreatelab.tasota.airprototype.classes;

import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Pm25AqiConverter;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
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
    public long getStartTime() { return from; }


    public DailyFeedTracker(Feed feed, long from, long to) {
        this.feed = feed;
        this.from = from;
        this.to = to;
    }


    public int getDirtyDaysCount() {
        return getDaysCount(Constants.DirtyDays.DIRTY_DAYS_VALUE_TYPE);
    }


    public int getDaysCount(DayFeedValue.DaysValueType type) {
        int size = 0;

        for (DayFeedValue feedValue : values) {
            if (Pm25AqiConverter.microgramsToAqi(feedValue.getCount(type)) > Constants.DirtyDays.DIRTY_DAYS_AQI_THRESHOLD) {
                size++;
            }
        }

        return size;
    }

}
