package org.cmucreatelab.tasota.airprototype.classes;

import android.util.Log;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.JsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.MapGeometry;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mike on 6/1/15.
 */
public class SimpleAddress implements Readable {

    private static final Type readableType = Readable.Type.ADDRESS;
    public Type getReadableType() {
        return readableType;
    }
    public boolean hasReadableValue() {
        return this.getClosestFeed() != null;
    }
    public double getReadableValue() {
        if (hasReadableValue())
            return this.getClosestFeed().getFeedValue();
        Log.w(Constants.LOG_TAG,"Tried getReadableValue on SimpleAddress with hasReadableValue=false; returning 0");
        return 0.0;
    }
    public String getName() {
        return name;
    }

    private long _id;
    private String name;
    private String zipcode;
    private double latitude,longitude;
    private Feed closestFeed = null;
    public final ArrayList<Feed> feeds = new ArrayList<>();
    private boolean isCurrentLocation;
    public long get_id() {
        return _id;
    }
    public boolean isCurrentLocation() {
        return isCurrentLocation;
    }
    public void set_id(long _id) {
        this._id = _id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public Feed getClosestFeed() {
        return closestFeed;
    }
    public void setClosestFeed(Feed closestFeed) {
        this.closestFeed = closestFeed;
    }
    public String getZipcode() {
        return zipcode;
    }
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    public SimpleAddress(String name, String zipcode, double latitude, double longitude) {
        this.name = name;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isCurrentLocation = false;
    }


    public SimpleAddress(String name, String zipcode, double latitude, double longitude, boolean isCurrentLocation) {
        this(name, zipcode, latitude, longitude);
        this.isCurrentLocation = isCurrentLocation;
    }


    /*
     * Handles updating feeds asynchronously
     */
    public void requestUpdateFeeds(final GlobalHandler globalHandler) {
        this.feeds.clear();
        // the past 24 hours
        final double maxTime = (new Date().getTime() / 1000.0) - Constants.READINGS_MAX_TIME_RANGE;

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Feed closestFeed;

                JsonParser.populateFeedsFromJson(feeds, response, maxTime);
                if (feeds.size() > 0) {
                    closestFeed = MapGeometry.getClosestFeedToAddress(SimpleAddress.this, feeds);
                    if (closestFeed != null) {
                        SimpleAddress.this.setClosestFeed(closestFeed);
                        // ASSERT all channels in the list of channels are usable readings
                        globalHandler.httpRequestHandler.requestChannelReading(closestFeed, closestFeed.getChannels().get(0));
                    }
                } else {
                    Log.e(Constants.LOG_TAG, "result size is 0 in pullFeeds.");
                }
            }
        };
        globalHandler.httpRequestHandler.requestFeeds(this.latitude, this.longitude, maxTime, response);
    }

}
