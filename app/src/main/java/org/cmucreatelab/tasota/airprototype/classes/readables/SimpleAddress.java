package org.cmucreatelab.tasota.airprototype.classes.readables;

import android.util.Log;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.activities.readable_show.ReadableShowActivity;
import org.cmucreatelab.tasota.airprototype.classes.DailyFeedTracker;
import org.cmucreatelab.tasota.airprototype.classes.channels.OzoneChannel;
import org.cmucreatelab.tasota.airprototype.classes.channels.Pm25Channel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.OzoneReadable;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Pm25Readable;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.MapGeometry;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers.EsdrJsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONObject;
import java.lang.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mike on 6/1/15.
 */
public class SimpleAddress extends AirNowReadable implements Pm25Readable, OzoneReadable {

    // class attributes
    private long _id;
    private String name;
    private String zipcode;
//    private AirQualityFeed closestFeed = null;
    // TODO please sort this by distance
    public final ArrayList<AirQualityFeed> feeds = new ArrayList<>();
    private boolean isCurrentLocation;
    private int positionId;
    private ReadableValue readablePm25Value,readableOzoneValue;


    private DailyFeedTracker dailyFeedTracker;
    // getters/setters
    public long get_id() { return _id; }
    public void set_id(long _id) { this._id = _id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
//    public AirQualityFeed getClosestFeed() { return closestFeed; }
//    public void setClosestFeed(AirQualityFeed closestFeed) { this.closestFeed = closestFeed; }
    public boolean isCurrentLocation() { return isCurrentLocation; }
    public int getPositionId() { return positionId; }
    public void setPositionId(int positionId) { this.positionId = positionId; }
    public DailyFeedTracker getDailyFeedTracker() { return dailyFeedTracker; }
    public void setReadablePm25Value(ReadableValue readableValue) { this.readablePm25Value = readableValue; }
    public void setReadableOzoneValue(ReadableValue readableValue) { this.readableOzoneValue= readableValue; }


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
//        if (closestFeed == null) {
//            Log.e(Constants.LOG_TAG, "requestDailyFeedTracker failed (closestFeed is null)");
//            return;
//        }
        if (hasReadablePm25Value()) {
            final AirQualityFeed closestFeed = (AirQualityFeed)(getReadablePm25Value().getChannel().getFeed());

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
        } else {
            Log.e(Constants.LOG_TAG, "requestDailyFeedTracker failed (closestFeed is null)");
            return;
        }
    }


    public void requestReadablePm25Reading(final GlobalHandler globalHandler) {// the past 24 hours
        final double maxTime = (new Date().getTime() / 1000.0) - Constants.READINGS_MAX_TIME_RANGE;

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AirQualityFeed closestFeed;

                EsdrJsonParser.populateFeedsFromJson(feeds, SimpleAddress.this, response, maxTime);
                if (feeds.size() > 0) {
                    closestFeed = MapGeometry.getClosestFeedToAddress(SimpleAddress.this, feeds);
                    if (closestFeed != null) {
                        //address.setClosestFeed(closestFeed);

                        // TODO hooks in functions below; if feed is updated, we want this to be using that value

                        // Responsible for calculating the value to be displayed
                        if (Constants.DEFAULT_ADDRESS_READABLE_VALUE_TYPE == Feed.ReadableValueType.NOWCAST) {
                            closestFeed.getPm25Channels().get(0).requestNowCast(globalHandler.appContext);
                        } else if (Constants.DEFAULT_ADDRESS_READABLE_VALUE_TYPE == Feed.ReadableValueType.INSTANTCAST) {
                            // ASSERT all channels in the list of channels are usable readings
                            globalHandler.esdrFeedsHandler.requestChannelReading(null, null, closestFeed, closestFeed.getPm25Channels().get(0), (long)maxTime);
                        }
                    }
                } else {
                    Log.e(Constants.LOG_TAG, "result size is 0 in pullFeeds.");
                }
            }
        };
        globalHandler.esdrFeedsHandler.requestFeeds(getLocation(), maxTime, response);

        //AirQualityFeed closestFeed = MapGeometry.getClosestFeedToAddress(this,this.feeds);
        // request
        // store

        //
        // TODO make requests from list of readings until one exists, or set its value to null
        // for each pm25 channel
        // try to get a readable value
        // (wait for a response...)
        // if found, we're done
        // otherwise, check the next channel
    }


    public void requestReadableOzoneReading(GlobalHandler globalHandler) {
        // TODO make requests from list of readings until one exists, or set its value to null
    }


    // Readable implementation


    private ArrayList<ReadableValue> generateReadableValues() {
        ArrayList<ReadableValue> result = new ArrayList<>();
        if (hasReadablePm25Value()) {
            result.add(getReadablePm25Value());
        }
        if (hasReadableOzoneValue()) {
            result.add(getReadableOzoneValue());
        }
        return result;
    }


    private static final Type readableType = Readable.Type.ADDRESS;


    public Type getReadableType() {
        return readableType;
    }


    public boolean hasReadableValue() {
        return (generateReadableValues().size() > 0);
//        return (closestFeed != null && closestFeed.hasReadableValue());
    }


    public List<ReadableValue> getReadableValues() {
        if (hasReadableValue()) {
            return generateReadableValues();
//            return closestFeed.getReadableValues();
        }
        return null;
    }


    // Pm25Readable implementation


    @Override
    public ArrayList<Pm25Channel> getPm25Channels() {
        ArrayList<Pm25Channel> result = new ArrayList<>();
        for (AirQualityFeed feed: this.feeds) {
            result.addAll(feed.getPm25Channels());
        }
        return result;
    }


    @Override
    public boolean hasReadablePm25Value() {
        return (readablePm25Value != null);
    }


    @Override
    public ReadableValue getReadablePm25Value() {
        return readablePm25Value;
    }


    // OzoneReadable implementation


    @Override
    public ArrayList<OzoneChannel> getOzoneChannels() {
        ArrayList<OzoneChannel> result = new ArrayList<>();
        for (AirQualityFeed feed: this.feeds) {
            result.addAll(feed.getOzoneChannels());
        }
        return result;
    }


    @Override
    public boolean hasReadableOzoneValue() {
        return (readableOzoneValue != null);
    }


    @Override
    public ReadableValue getReadableOzoneValue() {
        return readableOzoneValue;
    }

}
