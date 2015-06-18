package org.cmucreatelab.tasota.airprototype.helpers;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.Channel;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mike on 6/15/15.
 */
public class JsonParser {

    // Helper function to parse a feed's JSON and create objects (also does Channels)
    public static Feed parseFeedFromJson(JSONObject row) {
        Feed result = new Feed();

        try {
            long feed_id;
            String name,exposure;
            boolean isMobile;
            double latitude,longitude;
            long productId;
            ArrayList<Channel> listChannels;
            JSONObject channels;
            Iterator<String> keys;

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
            result.setLatitude(latitude);
            result.setLongitude(longitude);
            result.setProductId(productId);

            listChannels = result.getChannels();
            channels = row.getJSONObject("channelBounds").getJSONObject("channels");
            keys = channels.keys();
            while (keys.hasNext()) {
                // Only grab channels that we care about
                String channelName = keys.next();
                for (String cn : Constants.channelNames) {
                    if (channelName.equals(cn)) {
                        listChannels.add( JsonParser.parseChannelFromJson( channelName, feed_id, channels.getJSONObject(channelName)) );
                        break;
                    }
                }
            }
            if (listChannels.size() == 0) {
                return null;
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to parse Feed from JSON.");
        }

        return result;
    }


    // Helper function to create an object from a channel's JSON
    public static Channel parseChannelFromJson(String channelName, long feedId, JSONObject entry) {
        Channel c = new Channel();

        try {
            String name;
            long feed_id;
            double minTimeSecs,maxTimeSecs,minValue,maxValue;

            name = channelName;
            feed_id = feedId;
            minTimeSecs = Double.parseDouble(entry.get("minTimeSecs").toString());
            maxTimeSecs = Double.parseDouble(entry.get("maxTimeSecs").toString());;
            minValue = Double.parseDouble(entry.get("minValue").toString());
            maxValue = Double.parseDouble(entry.get("maxValue").toString());

            c.setName(name);
            c.setFeed_id(feed_id);
            c.setMinTimeSecs(minTimeSecs);
            c.setMaxTimeSecs(maxTimeSecs);
            c.setMinValue(minValue);
            c.setMaxValue(maxValue);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to parse Channel from JSON.");
        }

        return c;
    }

}
