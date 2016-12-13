package org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.DailyFeedTracker;
import org.cmucreatelab.tasota.airprototype.classes.DayFeedValue;
import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.classes.channels.HumidityChannel;
import org.cmucreatelab.tasota.airprototype.classes.channels.OzoneChannel;
import org.cmucreatelab.tasota.airprototype.classes.channels.Pm25Channel;
import org.cmucreatelab.tasota.airprototype.classes.channels.TemperatureChannel;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirQualityFeed;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by mike on 6/15/15.
 */
public class EsdrJsonParser {


    // parse feeds within maxTime and that have at least 1 valid channel
    public static void populateFeedsFromJson(ArrayList<AirQualityFeed> feeds, SimpleAddress simpleAddress, JSONObject response, double maxTime) {
        JSONArray jsonFeeds;
        int i, size;

        try {
            jsonFeeds = response.getJSONObject("data").getJSONArray("rows");
            size = jsonFeeds.length();
            for (i = 0; i < size; i++) {
                JSONObject jsonFeed = (JSONObject) jsonFeeds.get(i);
                // TODO setAddress
                AirQualityFeed feed = EsdrJsonParser.parseAQFeedFromJson(jsonFeed, maxTime, simpleAddress);
                // only consider non-null feeds with at least 1 channel
                if (feed != null) {
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
                Speck speck = EsdrJsonParser.parseSpeckFromJson(jsonFeed, 0);
                // only consider non-null feeds with at least 1 channel
                if (speck != null && speck.getPm25Channels().size() > 0) {
                    deviceId = jsonFeed.getLong("deviceId");
                    speck.setDeviceId(deviceId);
                    speck.setApiKeyReadOnly(jsonFeed.get("apiKeyReadOnly").toString());
                    specks.add(speck);
                }
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "JSON Format error (missing \"data\" or \"rows\" field).");
        }
    }


    // Helper function to parse a feed's JSON and create objects (also does Channels)
    public static AirQualityFeed parseAQFeedFromJson(JSONObject row, double maxTime, SimpleAddress simpleAddress) {
        AirQualityFeed result = new AirQualityFeed(simpleAddress);
        long feed_id;
        String name,exposure;
        boolean isMobile;
        double latitude,longitude;
        long productId;
        JSONObject channels;
        Iterator<String> keys;

        try {
            feed_id = Long.parseLong(row.get("id").toString());
            name = row.get("name").toString();
            exposure = row.get("exposure").toString();
            isMobile = row.get("isMobile").toString().equals("1");
            try {
                latitude = Double.parseDouble(row.get("latitude").toString());
            } catch (Exception e) {
                Log.w(Constants.LOG_TAG, "Error parsing latitude; will use 0 instead.");
                latitude = 0;
            }
            try {
                longitude = Double.parseDouble(row.get("longitude").toString());
            } catch (Exception e) {
                Log.w(Constants.LOG_TAG, "Error parsing longitude; will use 0 instead.");
                longitude = 0;
            }
            productId = Long.parseLong(row.get("productId").toString());

            result.setFeed_id(feed_id);
            result.setName(name);
            result.setExposure(exposure);
            result.setIsMobile(isMobile);
            result.setLocation(new Location(latitude, longitude));
            result.setProductId(productId);

            try {
                channels = row.getJSONObject("channelBounds").getJSONObject("channels");
                keys = channels.keys();
                while (keys.hasNext()) {
                    String channelName = keys.next();
                    // NOTICE: we must also make sure that this specific channel
                    // was updated in the past 24 hours ("maxTime").
                    JSONObject channel = channels.getJSONObject(channelName);
                    if (channel.getDouble("maxTimeSecs") >= maxTime) {
                        result.addChannel(EsdrJsonParser.parseChannelFromJson(channelName, result, channel));
                        break;
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
    // same as above but for specks
    public static Speck parseSpeckFromJson(JSONObject row, double maxTime) {
        Speck result = null;
        long feed_id;
        String name,exposure;
        boolean isMobile;
        double latitude,longitude;
        long productId;
        JSONObject channels;
        Iterator<String> keys;

        try {
            feed_id = Long.parseLong(row.get("id").toString());
            name = row.get("name").toString();
            exposure = row.get("exposure").toString();
            isMobile = row.get("isMobile").toString().equals("1");
            try {
                latitude = Double.parseDouble(row.get("latitude").toString());
            } catch (Exception e) {
                Log.w(Constants.LOG_TAG, "Error parsing latitude; will use 0 instead.");
                latitude = 0;
            }
            try {
                longitude = Double.parseDouble(row.get("longitude").toString());
            } catch (Exception e) {
                Log.w(Constants.LOG_TAG, "Error parsing longitude; will use 0 instead.");
                longitude = 0;
            }
            productId = Long.parseLong(row.get("productId").toString());

            result = new Speck("",0,exposure,feed_id,isMobile,new Location(latitude, longitude),name,0,productId);

            try {
                channels = row.getJSONObject("channelBounds").getJSONObject("channels");
                keys = channels.keys();
                while (keys.hasNext()) {
                    String channelName = keys.next();
                    // NOTICE: we must also make sure that this specific channel
                    // was updated in the past 24 hours ("maxTime").
                    JSONObject channel = channels.getJSONObject(channelName);
                    if (channel.getDouble("maxTimeSecs") >= maxTime) {
                        result.addChannel(EsdrJsonParser.parseChannelFromJson(channelName, result, channel));
                        break;
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
        Log.i(Constants.LOG_TAG,"new Channel \""+channelName+"\"");
        String name;
        double minTimeSecs,maxTimeSecs,minValue,maxValue;

        try {
            // check for known channel types
            if (Arrays.asList(Constants.channelNamesPm).contains(channelName)) {
                c = new Pm25Channel();
                Log.i(Constants.LOG_TAG,"new Pm25Channel \""+channelName+"\"");
            } else if (Arrays.asList(Constants.channelNamesOzone).contains(channelName)) {
                c = new OzoneChannel();
                Log.i(Constants.LOG_TAG,"new OzoneChannel \""+channelName+"\"");
            } else if (Arrays.asList(Constants.channelNamesHumidity).contains(channelName)) {
                c = new HumidityChannel();
                Log.i(Constants.LOG_TAG,"new HumidityChannel \""+channelName+"\"");
            } else if (Arrays.asList(Constants.channelNamesTemperature).contains(channelName)) {
                c = new TemperatureChannel();
                Log.i(Constants.LOG_TAG,"new TemperatureChannel \""+channelName+"\"");
            } else {
                Log.w(Constants.LOG_TAG,"General Channel created from channelName=\""+channelName+"\"");
            }
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


    public static DailyFeedTracker parseDailyFeedTracker(Feed feed, long from, long to, JSONObject entry) {
        DailyFeedTracker result = new DailyFeedTracker(feed, from, to);
        ArrayList<DayFeedValue> values = result.getValues();

        try {
            JSONArray jsonValues = entry.getJSONArray("data");
            int size = jsonValues.length();
            for (int i=0; i<size; i++) {
                JSONArray row = jsonValues.getJSONArray(i);

                // ASSERT: request was done in the order: mean, median, max
                long time = row.getLong(0);
                double mean = row.getDouble(1);
                double median = row.getDouble(2);
                double max = row.getDouble(3);

                values.add( new DayFeedValue(time,mean,median,max) );
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "JSON Format error in parseDailyFeedTracker: " + e.getLocalizedMessage());
        }

        return result;
    }

}
