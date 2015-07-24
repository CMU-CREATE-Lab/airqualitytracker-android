package org.cmucreatelab.tasota.airprototype.helpers;

import android.location.Location;
import android.util.Log;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.JsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.MapGeometry;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mike on 6/11/15.
 */
public class AddressFeedsHashMap {

    private GlobalHandler globalHandler;
    protected SimpleAddress gpsAddress; // listed in addresses
    // this ArrayList ensures an ordered list of addresses
    // (required to react to AddressListActivity events and displaying on AddressShowActivity)
    protected ArrayList<SimpleAddress> addresses;
    protected HashMap<SimpleAddress,ArrayList<Feed>> hashMap;
    public SimpleAddress getGpsAddress() {
        return gpsAddress;
    }
    public ArrayList<SimpleAddress> getAddresses() {
        return addresses;
    }


    public AddressFeedsHashMap(GlobalHandler globalHandler) {
        this.addresses = new ArrayList<>();
        this.hashMap = new HashMap<>();
        this.gpsAddress = new SimpleAddress("Loading Current Location...", 0.0, 0.0, true);
        gpsAddress.setIconType(SimpleAddress.IconType.GPS);
        hashMap.put(gpsAddress, new ArrayList<Feed>());
        this.globalHandler = globalHandler;
        // populate addresses from database
        ArrayList<SimpleAddress> dbAddresses = AddressDbHelper.fetchAddressesFromDatabase(this.globalHandler.appContext);
        for (SimpleAddress simpleAddress : dbAddresses) {
            // prevents making http requests until we actually want to display something
//            ArrayList<Feed> feed = pullFeedsForAddress(simpleAddress);
            ArrayList<Feed> feed = new ArrayList<>();
            this.put(simpleAddress, feed);
        }
    }


    public void setGpsAddressLocation(Location location) {
        gpsAddress.setLatitude(location.getLatitude());
        gpsAddress.setLongitude(location.getLongitude());

        // update the gps address with the new closest feeds
        ArrayList<Feed> feeds = pullFeedsForAddress(gpsAddress);
        hashMap.put(gpsAddress, feeds);
    }


    public void removeAddress(SimpleAddress simpleAddress) {
        this.hashMap.remove(simpleAddress);
        addresses.remove(simpleAddress);
        globalHandler.requestAddressesForDisplay();
    }


    public void addAddress(SimpleAddress simpleAddress) {
        ArrayList<Feed> feed = pullFeedsForAddress(simpleAddress);
        this.put(simpleAddress, feed);
        globalHandler.requestAddressesForDisplay();
    }


    public void put(SimpleAddress simpleAddress, ArrayList<Feed> feeds) {
        if (addresses.indexOf(simpleAddress) < 0){
            addresses.add(simpleAddress);
        }
        this.hashMap.put(simpleAddress, feeds);
    }


    public ArrayList<Feed> getFeedsFromAddressInHashMap(SimpleAddress simpleAddress) {
        return this.hashMap.get(simpleAddress);
    }


    // Updates the feeds for all current addresses ("refresh")
    protected void updateAddresses() {
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

                JsonParser.populateFeedsFromJson(result,response,maxTime);
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
        globalHandler.httpRequestHandler.requestFeeds(addr.getLatitude(), addr.getLongitude(), maxTime, response);

        return result;
    }

}
