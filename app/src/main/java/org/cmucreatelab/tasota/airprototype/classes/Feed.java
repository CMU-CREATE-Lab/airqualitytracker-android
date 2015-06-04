package org.cmucreatelab.tasota.airprototype.classes;

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


    public Feed() {
        this.channels = new ArrayList<Channel>();
        this.name = new String();
        this.exposure = new String();
    }


    // Helper function to parse a feed's JSON and create objects (also does Channels)
    public static Feed parseFeedFromJson(JSONObject row) {
        Feed f = new Feed();
        try {
            long feed_id = Long.parseLong(row.get("id").toString());
            String name = row.get("name").toString();
            String exposure = row.get("exposure").toString();
            boolean isMobile = row.get("isMobile").toString().equals("1");
            double latitude = Double.parseDouble(row.get("latitude").toString());
            double longitude = Double.parseDouble(row.get("longitude").toString());
            long productId = Long.parseLong(row.get("productId").toString());

            f.setFeed_id(feed_id);
            f.setName(name);
            f.setExposure(exposure);
            f.setIsMobile(isMobile);
            f.setLatitude(latitude);
            f.setLongitude(longitude);
            f.setProductId(productId);

            ArrayList<Channel> listChannels = f.getChannels();
            JSONObject channels = row.getJSONObject("channelBounds").getJSONObject("channels");
            Iterator<String> keys = channels.keys();
            while (keys.hasNext()) {
                String channelName = keys.next();
                listChannels.add( Channel.parseChannelFromJson( channelName, feed_id, channels.getJSONObject(channelName)) );
            }
        } catch (Exception e) {
            // TODO catch exception "failed to find JSON attr"
            e.printStackTrace();
        }
        return f;
    }

    // TODO generate a proper label from the class attributes
    public String toString() {
        return "(" + this.feed_id + ")" + this.name;
    }

}
