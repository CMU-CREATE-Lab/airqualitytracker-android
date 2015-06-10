package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressContract;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressDbHelper;
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

    private static GlobalHandler classInstance;
    private Context appContext;
    public ArrayList<SimpleAddress> addresses;
    public HashMap<SimpleAddress,ArrayList<Feed>> addressFeedHash;
    public HttpRequestHandler httpRequestHandler;
    public GoogleApiClientHandler googleApiClientHandler;
    public LocationUpdateHandler locationUpdateHandler;

    // Keep track of ALL your array adapters for notifyGlobalDataSetChanged()
    public ArrayAdapterAddressList listAdapter;


    private void addCurrentLocationToAddresses() {
        SimpleAddress gps;
        ArrayList<Feed> gFeed;

        // TODO this will be your GPS location, eventually
        gps = new SimpleAddress("Loading Current Location...", 0.0, 0.0);
        gps.set_id(-1);
        gFeed = getFeedsForAddress(gps);
        this.addresses.add(gps);
        this.addressFeedHash.put(gps, gFeed);
    }


    private void addDatabaseEntriesToAddresses() {
        String[] projection = {
                "_id",
                AddressContract.COLUMN_NAME,
                AddressContract.COLUMN_LATITUDE,
                AddressContract.COLUMN_LONGITUDE
        };
        AddressDbHelper mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;

        mDbHelper = new AddressDbHelper(appContext);
        db = mDbHelper.getWritableDatabase();
        cursor = db.query(AddressContract.TABLE_NAME, projection,
                null, null, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                null // sort order
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SimpleAddress simpleAddress;
            ArrayList<Feed> feed;
            int id;
            String name;
            double latd,longd;

            try {
                // read record
                id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                latd = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("latitude")));
                longd = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("longitude")));
                Log.i("addToDatabase", "READ RECORD _id=" + id);

                // add to data structure
                simpleAddress = new SimpleAddress(name, latd, longd);
                simpleAddress.set_id(id);
                feed = getFeedsForAddress(simpleAddress);
                this.addresses.add(simpleAddress);
                this.addressFeedHash.put(simpleAddress, feed);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // iterate
            cursor.moveToNext();
        }
    }


    protected void updateCurrentLocation(Location lastLocation) {
        SimpleAddress a = addresses.get(0);
        a.setName(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        a.setLatitude(lastLocation.getLatitude());
        a.setLongitude(lastLocation.getLongitude());
        notifyGlobalDataSetChanged();
    }


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        this.appContext = ctx;
        this.addresses = new ArrayList();
        this.addressFeedHash = new HashMap();
        this.httpRequestHandler = HttpRequestHandler.getInstance(ctx);
        this.googleApiClientHandler = GoogleApiClientHandler.getInstance(ctx,this);
        this.locationUpdateHandler = LocationUpdateHandler.getInstance(ctx,this.googleApiClientHandler);
        this.addCurrentLocationToAddresses();
        this.addDatabaseEntriesToAddresses();
    }


    private void notifyGlobalDataSetChanged() {
        // TODO this function provides a mechanism for notifying all (active) list adapters in the app when the dataset gets updated.
        this.listAdapter.notifyDataSetChanged();
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }


    public void removeAddress(SimpleAddress simpleAddress) {
        addressFeedHash.remove(simpleAddress);
        addresses.remove(simpleAddress);
    }


    public void addAddress(SimpleAddress simpleAddress) {
        addresses.add(simpleAddress);
        ArrayList<Feed> feed = getFeedsForAddress(simpleAddress);
        addressFeedHash.put(simpleAddress,feed);
    }


    public ArrayList<Feed> getFeedsForAddress(SimpleAddress addr) {
        final ArrayList<Feed> result = new ArrayList();

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

}
