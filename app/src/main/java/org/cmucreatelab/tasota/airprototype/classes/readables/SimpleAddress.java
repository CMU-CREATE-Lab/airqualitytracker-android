package org.cmucreatelab.tasota.airprototype.classes.readables;

import android.util.Log;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.activities.BaseActivity;
import org.cmucreatelab.tasota.airprototype.classes.DailyFeedTracker;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers.EsdrJsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONObject;
import java.lang.*;
import java.util.ArrayList;

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
    // TODO dailyFeedTracker when closestFeed exists
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


    // TODO replace with specific activity which will call this upon request
    public void requestDailyFeedTracker(BaseActivity activity) {
        if (closestFeed == null) {
            Log.e(Constants.LOG_TAG, "requestDailyFeedTracker failed (closestFeed is null)");
            return;
        }
        if (dailyFeedTracker != null) {
            // TODO call method inside Activity to denote request is finished
            return;
        }

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SimpleAddress.this.dailyFeedTracker = EsdrJsonParser.parseDailyFeedTracker(closestFeed,response);
            }
        };

        GlobalHandler.getInstance(activity.getApplicationContext()).esdrTilesHandler.requestFeedAverages(closestFeed, response);
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


    public double getReadableValue() {
        if (hasReadableValue()) {
            return getClosestFeed().getReadableValue();
        }
        return 0;
    }

}
