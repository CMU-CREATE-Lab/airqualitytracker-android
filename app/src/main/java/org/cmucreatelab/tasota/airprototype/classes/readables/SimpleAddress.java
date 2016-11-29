package org.cmucreatelab.tasota.airprototype.classes.readables;

import android.util.Log;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.activities.readable_show.ReadableShowActivity;
import org.cmucreatelab.tasota.airprototype.classes.DailyFeedTracker;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers.EsdrJsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONObject;
import java.lang.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mike on 6/1/15.
 */
public class SimpleAddress extends AirNowReadable {

    // class attributes
    private long _id;
    private String name;
    private String zipcode;
    private Feed closestFeed = null;
    public final ArrayList<Feed> feeds = new ArrayList<>();
    private boolean isCurrentLocation;
    private int positionId;


    private DailyFeedTracker dailyFeedTracker;
    // getters/setters
    public long get_id() { return _id; }
    public void set_id(long _id) { this._id = _id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public Feed getClosestFeed() { return closestFeed; }
    public void setClosestFeed(Feed closestFeed) { this.closestFeed = closestFeed; }
    public boolean isCurrentLocation() { return isCurrentLocation; }
    public int getPositionId() { return positionId; }
    public void setPositionId(int positionId) { this.positionId = positionId; }
    public DailyFeedTracker getDailyFeedTracker() { return dailyFeedTracker; }


    // class constructor
    public SimpleAddress(String name, String zipcode, Location location) {
        this.name = name;
        this.zipcode = zipcode;
        this.location = location;
        this.isCurrentLocation = false;
    }


    // class constructor
    public SimpleAddress(String name, String zipcode, Location location, boolean isCurrentLocation) {
        this(name, zipcode, location);
        this.isCurrentLocation = isCurrentLocation;
    }


    public void requestDailyFeedTracker(final ReadableShowActivity activity) {
        final long to = new Date().getTime() / 1000;
        final long from = to - (long)(86400 * 365);
        if (closestFeed == null) {
            Log.e(Constants.LOG_TAG, "requestDailyFeedTracker failed (closestFeed is null)");
            return;
        }
        if (dailyFeedTracker != null) {
            // check that this tracker is at least within the last 24 hours (otherwise it needs updated)
            if (to - dailyFeedTracker.getStartTime() <= Constants.TWENTY_FOUR_HOURS) {
                int numberDirtyDays = dailyFeedTracker.getDirtyDaysCount();
                String text = String.valueOf(numberDirtyDays) + ((numberDirtyDays == 1) ? " dirty day " : " dirty days ")
                        + "in the past year";
                activity.feedTrackerResponse(text);
                return;
            }
        }

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SimpleAddress.this.dailyFeedTracker = EsdrJsonParser.parseDailyFeedTracker(closestFeed, from, to, response);
                int numberDirtyDays = dailyFeedTracker.getDirtyDaysCount();
                String text = String.valueOf(numberDirtyDays) + ((numberDirtyDays == 1) ? " dirty day " : " dirty days ")
                        + "in the past year";
                activity.feedTrackerResponse(text);
            }
        };

        GlobalHandler.getInstance(activity.getApplicationContext()).esdrTilesHandler.requestFeedAverages(closestFeed, from, to, response);
    }


    // Readable implementation


    private static final Type readableType = Readable.Type.ADDRESS;


    public Type getReadableType() {
        return readableType;
    }


    public boolean hasReadableValue() {
        Feed feed = getClosestFeed();
        return (feed != null && feed.hasReadableValue());
    }


    public ReadableValue getReadableValue() {
        if (hasReadableValue()) {
            return getClosestFeed().getReadableValue();
        }
        return null;
    }

}
