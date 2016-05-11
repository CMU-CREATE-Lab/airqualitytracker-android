package org.cmucreatelab.tasota.airprototype.classes;

import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
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

}
