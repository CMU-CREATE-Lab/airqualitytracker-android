package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.views.services.AddressResultReceiver;
import org.cmucreatelab.tasota.airprototype.views.services.FetchAddressIntentService;
import org.cmucreatelab.tasota.airprototype.views.uielements.ArrayAdapterAddressList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by mike on 6/2/15.
 */
public class GlobalHandler {

    private Context appContext;
    private static GlobalHandler classInstance;
    public ArrayList<SimpleAddress> addresses;
    public SimpleAddress gpsAddress; // listed in addresses
    public HashMap<SimpleAddress,ArrayList<Feed>> addressFeedHash;
    public HttpRequestHandler httpRequestHandler;
    public GoogleApiClientHandler googleApiClientHandler;
    public LocationUpdateHandler locationUpdateHandler;

    // Keep track of ALL your array adapters for notifyGlobalDataSetChanged()
    public ArrayAdapterAddressList listAdapter;


    private void addDatabaseEntriesToAddresses() {
        ArrayList<SimpleAddress> dbAddresses;
        ArrayList<Feed> feed;

        dbAddresses = SimpleAddress.fetchAddressesFromDatabase(this.appContext);
        for (SimpleAddress simpleAddress : dbAddresses) {
                feed = getFeedsForAddress(simpleAddress);
                this.addresses.add(simpleAddress);
                this.addressFeedHash.put(simpleAddress, feed);
        }
    }


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        // context and handlers
        this.appContext = ctx;
        this.httpRequestHandler = HttpRequestHandler.getInstance(ctx);
        this.googleApiClientHandler = GoogleApiClientHandler.getInstance(ctx,this);
        this.locationUpdateHandler = LocationUpdateHandler.getInstance(ctx, this.googleApiClientHandler);

        // data structures
        this.addresses = new ArrayList<>();
        this.addressFeedHash = new HashMap<>();
        gpsAddress = new SimpleAddress("Loading Current Location...", 0.0, 0.0);
        this.addresses.add(gpsAddress);

        // populate stuff
        this.addDatabaseEntriesToAddresses();
    }


    private void notifyGlobalDataSetChanged() {
        // TODO this function provides a mechanism for notifying all (active) list adapters in the app when the dataset gets updated.
        this.listAdapter.notifyDataSetChanged();
    }


    protected void updateCurrentLocation(Location lastLocation) {
        gpsAddress.setLatitude(lastLocation.getLatitude());
        gpsAddress.setLongitude(lastLocation.getLongitude());

        // update the gps address with the new closest feeds
        ArrayList<Feed> feeds = getFeedsForAddress(gpsAddress);
        this.addressFeedHash.put(gpsAddress, feeds);

        notifyGlobalDataSetChanged();
        // TODO consider this when more than one update can occur.
        startFetchAddressIntentService(lastLocation);
    }


    protected void startFetchAddressIntentService(Location lastLocation) {
        if (Geocoder.isPresent()) {
            Intent intent = new Intent(this.appContext, FetchAddressIntentService.class);
            AddressResultReceiver resultReceiver = new AddressResultReceiver(new Handler(),this);

            intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, resultReceiver);
            intent.putExtra("latitude",lastLocation.getLatitude());
            intent.putExtra("longitude",lastLocation.getLongitude());
            this.appContext.startService(intent);
        } else {
            Log.i("ERROR","Geocoder is not present");
        }
    }


    public void removeAddress(SimpleAddress simpleAddress) {
        addressFeedHash.remove(simpleAddress);
        addresses.remove(simpleAddress);
    }


    public void addAddress(SimpleAddress simpleAddress) {
        addresses.add(simpleAddress);
        ArrayList<Feed> feed = getFeedsForAddress(simpleAddress);
        addressFeedHash.put(simpleAddress, feed);
    }


    public ArrayList<Feed> getFeedsForAddress(final SimpleAddress addr) {
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
                        result.add( Feed.parseFeedFromJson(jsonFeed) );
                    }
                    addr.setClosestFeed( MapGeometry.getClosestFeedToAddress(addr,result) );
                    GlobalHandler.this.notifyGlobalDataSetChanged();
                } catch (Exception e) {
                    // TODO catch exception "failed to find JSON attr"
                    e.printStackTrace();
                }
                notifyGlobalDataSetChanged();
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle errors
            }
        };
        this.httpRequestHandler.requestFeeds(addr.getLatitude(), addr.getLongitude(), response, error);

        return result;
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }

}
