package org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.readables.Channel;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by mike on 6/15/15.
 */
public class EsdrJsonParser {


    // parse feeds within maxTime and that have at least 1 valid channel
    public static void populateFeedsFromJson(ArrayList<Feed> feeds, JSONObject response, double maxTime) {
        JSONArray jsonFeeds;
        int i, size;

        try {
            jsonFeeds = response.getJSONObject("data").getJSONArray("rows");
            size = jsonFeeds.length();
            for (i = 0; i < size; i++) {
                JSONObject jsonFeed = (JSONObject) jsonFeeds.get(i);
                Feed feed = EsdrJsonParser.parseFeedFromJson(jsonFeed, maxTime);
                // only consider non-null feeds with at least 1 channel
                if (feed != null && feed.getChannels().size() > 0) {
                    feeds.add(feed);
                }
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "JSON Format error (missing \"data\" or \"rows\" field).");
        }
    }


    public static void populateSpecksFromJson(ArrayList<Speck> specks, JSONObject response) {
        JSONArray jsonFeeds;
        int i, size;
        long deviceId;

        try {
            jsonFeeds = response.getJSONObject("data").getJSONArray("rows");
            size = jsonFeeds.length();
            for (i = 0; i < size; i++) {
                JSONObject jsonFeed = (JSONObject) jsonFeeds.get(i);
                Feed feed = EsdrJsonParser.parseFeedFromJson(jsonFeed, 0);
                // only consider non-null feeds with at least 1 channel
                if (feed != null && feed.getChannels().size() > 0) {
                    deviceId = jsonFeed.getLong("deviceId");
                    Speck speck = new Speck(feed, deviceId);
                    speck.setApiKeyReadOnly(jsonFeed.get("apiKeyReadOnly").toString());
                    specks.add(speck);
                }
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "JSON Format error (missing \"data\" or \"rows\" field).");
        }
    }


    // Helper function to parse a feed's JSON and create objects (also does Channels)
    public static Feed parseFeedFromJson(JSONObject row, double maxTime) {
        Feed result = new Feed();
        long feed_id;
        String name,exposure;
        boolean isMobile;
        double latitude,longitude;
        long productId;
        ArrayList<Channel> listChannels;
        JSONObject channels;
        Iterator<String> keys;

        try {
            feed_id = Long.parseLong(row.get("id").toString());
            name = row.get("name").toString();
            exposure = row.get("exposure").toString();
            isMobile = row.get("isMobile").toString().equals("1");
            latitude = Double.parseDouble(row.get("latitude").toString());
            longitude = Double.parseDouble(row.get("longitude").toString());
            productId = Long.parseLong(row.get("productId").toString());

            result.setFeed_id(feed_id);
            result.setName(name);
            result.setExposure(exposure);
            result.setIsMobile(isMobile);
            result.setLocation(new Location(latitude, longitude));
            result.setProductId(productId);

            listChannels = result.getChannels();
            try {
                channels = row.getJSONObject("channelBounds").getJSONObject("channels");
                keys = channels.keys();
                while (keys.hasNext()) {
                    // Only grab channels that we care about
                    String channelName = keys.next();
                    for (String cn : Constants.channelNames) {
                        if (channelName.equals(cn)) {
                            // NOTICE: we must also make sure that this specific channel
                            // was updated in the past 24 hours ("maxTime").
                            JSONObject channel = channels.getJSONObject(channelName);
                            if (channel.getDouble("maxTimeSecs") >= maxTime) {
                                listChannels.add(EsdrJsonParser.parseChannelFromJson(channelName, result, channel));
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.w(Constants.LOG_TAG, "Failed to grab Channels from Feed (likely null).");
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to parse Feed from JSON.");
        }

        return result;
    }


    // Helper function to create an object from a channel's JSON
    public static Channel parseChannelFromJson(String channelName, Feed feed, JSONObject entry) {
        Channel c = new Channel();
        String name;
        double minTimeSecs,maxTimeSecs,minValue,maxValue;

        try {
            name = channelName;
            minTimeSecs = Double.parseDouble(entry.get("minTimeSecs").toString());
            maxTimeSecs = Double.parseDouble(entry.get("maxTimeSecs").toString());
            minValue = Double.parseDouble(entry.get("minValue").toString());
            maxValue = Double.parseDouble(entry.get("maxValue").toString());

            c.setName(name);
            c.setFeed(feed);
            c.setMinTimeSecs(minTimeSecs);
            c.setMaxTimeSecs(maxTimeSecs);
            c.setMinValue(minValue);
            c.setMaxValue(maxValue);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to parse Channel from JSON.");
        }

        return c;
    }


    public static void parseTiles(JSONObject response, int fromTime, int toTime, HashMap<Integer, ArrayList<Double>> result) {
        int i,size;
        JSONArray dataArray,dataPoint;
        int time;
        double mean,count;
        ArrayList<Double> values;

        // grab all tiles within timestamp range (fromTime..toTime)
        try {
            dataArray = response.getJSONObject("data").getJSONArray("data");
            size = dataArray.length();
            for (i = 0; i < size; i++) {
                dataPoint = dataArray.getJSONArray(i);
                time = dataPoint.getInt(0);
                if (time > fromTime && time <= toTime) {
                    mean = dataPoint.getDouble(1);
                    count = dataPoint.getDouble(3);
                    values = new ArrayList<>();
                    values.add(mean);
                    values.add(count);
                    result.put(time, values);
                }
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "JSON Format error in parseTiles (missing \"data\" or other field).");
        }
    }

}