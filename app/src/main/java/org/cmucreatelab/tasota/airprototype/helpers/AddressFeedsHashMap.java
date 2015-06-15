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
        this.put(gpsAddress, new ArrayList<Feed>());
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
        this.put(gpsAddress, feeds);
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


    public ArrayList<Feed> pullFeedsForAddress(final SimpleAddress addr) {
        final ArrayList<Feed> result = new ArrayList<>();

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonFeeds;
                    int i,size;

                    jsonFeeds = response.getJSONObject("data").getJSONArray("rows");
                    size = jsonFeeds .length();
                    for (i=0;i<size;i++) {
                        JSONObject jsonFeed = (JSONObject)jsonFeeds.get(i);
                        result.add( JsonParser.parseFeedFromJson(jsonFeed) );
                    }
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG, "JSON Format error (missing \"data\" or \"rows\" field).");
                }
                addr.setClosestFeed( MapGeometry.getClosestFeedToAddress(addr,result) );
                globalHandler.notifyGlobalDataSetChanged();
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.LOG_TAG, "Received error from Volley: " + error.getLocalizedMessage());
            }
        };
        globalHandler.httpRequestHandler.requestFeeds(addr.getLatitude(), addr.getLongitude(), response, error);

        return result;
    }

}
