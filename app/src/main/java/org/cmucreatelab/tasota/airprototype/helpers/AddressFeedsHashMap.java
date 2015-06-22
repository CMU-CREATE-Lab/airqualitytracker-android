package org.cmucreatelab.tasota.airprototype.helpers;

import android.location.Location;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressDbHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mike on 6/11/15.
 */
public class AddressFeedsHashMap {

    private GlobalHandler globalHandler;
    public SimpleAddress gpsAddress; // listed in addresses
    // this ArrayList ensures an ordered list of addresses
    // (required to react to AddressListActivity events and displaying on AddressShowActivity)
    public ArrayList<SimpleAddress> addresses;
    public HashMap<SimpleAddress,ArrayList<Feed>> hashMap;


    public AddressFeedsHashMap(GlobalHandler globalHandler) {
        this.addresses = new ArrayList<>();
        this.hashMap = new HashMap<>();
        this.gpsAddress = new SimpleAddress("Loading Current Location...", 0.0, 0.0);
        gpsAddress.setIconType(SimpleAddress.IconType.GPS);
        // we don't want gpsAddress to be in addresses
//        this.put(gpsAddress, new ArrayList<Feed>());
        hashMap.put(gpsAddress, new ArrayList<Feed>());
        this.globalHandler = globalHandler;
        // populate addresses from database
        ArrayList<SimpleAddress> dbAddresses = AddressDbHelper.fetchAddressesFromDatabase(this.globalHandler.appContext);
        for (SimpleAddress simpleAddress : dbAddresses) {
            this.addAddress(simpleAddress);
        }
    }


    public void setGpsAddressLocation(Location location) {
        gpsAddress.setLatitude(location.getLatitude());
        gpsAddress.setLongitude(location.getLongitude());

        // update the gps address with the new closest feeds
        ArrayList<Feed> feeds = pullFeedsForAddress(gpsAddress);
        // we don't want gpsAddress to be in addresses
//        this.put(gpsAddress, feeds);
        hashMap.put(gpsAddress, feeds);
    }


    public void removeAddress(SimpleAddress simpleAddress) {
        this.hashMap.remove(simpleAddress);
        addresses.remove(simpleAddress);
    }


    public void addAddress(SimpleAddress simpleAddress) {
        ArrayList<Feed> feed = pullFeedsForAddress(simpleAddress);
        this.put(simpleAddress, feed);
    }


    public void put(SimpleAddress simpleAddress, ArrayList<Feed> feeds) {
        if (addresses.indexOf(simpleAddress) < 0){
            addresses.add(simpleAddress);
        }
        this.hashMap.put(simpleAddress, feeds);
    }


    // Updates the feeds for all current addresses ("refresh")
    public void updateAddresses() {
        for (SimpleAddress address : this.addresses) {
            this.put(address, pullFeedsForAddress(address));
        }
    }


    public ArrayList<Feed> pullFeedsForAddress(final SimpleAddress addr) {
        final ArrayList<Feed> result = new ArrayList<>();
        // the past 24 hours
        final double maxTime = new Date().getTime() / 1000.0 - 86400;

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Feed closestFeed;

                try {
                    JSONArray jsonFeeds;
                    int i,size;

                    jsonFeeds = response.getJSONObject("data").getJSONArray("rows");
                    size = jsonFeeds .length();
                    for (i=0;i<size;i++) {
                        JSONObject jsonFeed = (JSONObject)jsonFeeds.get(i);
                        Feed feed = JsonParser.parseFeedFromJson(jsonFeed,maxTime);
                        // only consider non-null feeds
                        if (feed != null) {
                            result.add(feed);
                        }
                    }
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG, "JSON Format error (missing \"data\" or \"rows\" field).");
                }
                if (result.size() > 0) {
                    closestFeed = MapGeometry.getClosestFeedToAddress(addr, result);
                    if (closestFeed != null) {
                        addr.setClosestFeed(closestFeed);
                        // ASSERT all channels in the list of channels are usable readings
                        // TODO we use the first channel listed; handle when we do not have all channels as PM25
                        globalHandler.httpRequestHandler.requestChannelReading(closestFeed, closestFeed.getChannels().get(0));
                        globalHandler.notifyGlobalDataSetChanged();
                    }
                } else {
                    Log.e(Constants.LOG_TAG,"result size is 0 in pullFeedsForAddress.");
                }
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.LOG_TAG, "Received error from Volley: " + error.getLocalizedMessage());
            }
        };
        globalHandler.httpRequestHandler.requestFeeds(addr.getLatitude(), addr.getLongitude(), maxTime, response, error);

        return result;
    }

}
