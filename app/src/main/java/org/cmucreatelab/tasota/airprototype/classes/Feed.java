package org.cmucreatelab.tasota.airprototype.classes;

import android.util.Log;

import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mike on 6/1/15.
 */
public class Feed {

    // NOTE: if you want more attributes, be sure that they are included in the json response (for parsing)
    private long feed_id;
    private String name;
    // (FROM DOCS): an enum and must be one of indoor, outdoor, or virtual
    private String exposure;
    private boolean isMobile;
    private double latitude;
    private double longitude;
    private long productId;
    private ArrayList<Channel> channels;

    // what we want to display to the user (ug/m^3)
    private String feedDisplay;

    public long getProductId() {
        return productId;
    }
    public void setProductId(long productId) {
        this.productId = productId;
    }
    public long getFeed_id() {
        return feed_id;
    }
    public void setFeed_id(long feed_id) {
        this.feed_id = feed_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getExposure() {
        return exposure;
    }
    public void setExposure(String exposure) {
        this.exposure = exposure;
    }
    public boolean isMobile() {
        return isMobile;
    }
    public void setIsMobile(boolean isMobile) {
        this.isMobile = isMobile;
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
    public ArrayList<Channel> getChannels() {
        return channels;
    }
    public String getFeedDisplay() {
        if (feedDisplay == null) {
            updateChannelReadings();
        }
        return feedDisplay;
    }


    public Feed() {
        this.channels = new ArrayList<Channel>();
        this.name = new String();
        this.exposure = new String();
    }


    public void updateChannelReadings() {
        // TODO grab most recent readings from the Feed's channels
        // TODO this is where Chris' API call will come in handy
        feedDisplay = "feed="+String.valueOf(this.feed_id);
    }


    @Override
    public String toString() {
        // TODO generate a proper label from the class attributes
        return "(" + this.feed_id + ")" + this.name;
    }


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
                String channelName = keys.next();
                listChannels.add( Channel.parseChannelFromJson( channelName, feed_id, channels.getJSONObject(channelName)) );
            }

            result.updateChannelReadings();
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to parse Feed from JSON.");
        }

        return result;
    }

}
