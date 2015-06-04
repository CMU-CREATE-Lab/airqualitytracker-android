package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressContract;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressDbHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mike on 6/2/15.
 */
public class GlobalHandler {

    private static GlobalHandler classInstance;
    private Context appContext;
    public ArrayList<Address> addresses;
    public HashMap<Address,ArrayList<Feed>> addressFeedHash;


    private void addCurrentLocationToAddresses() {
        Address gps;
        ArrayList<Feed> gFeed;

        // TODO this will be your GPS location, eventually
        gps = new Address("15235", 40.4586216, -79.8184684);
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
            Address address;
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
                address = new Address(name, latd, longd);
                address.set_id(id);
                feed = getFeedsForAddress(address);
                this.addresses.add(address);
                this.addressFeedHash.put(address, feed);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // iterate
            cursor.moveToNext();
        }
    }


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        this.appContext = ctx;
        this.addresses = new ArrayList();
        this.addressFeedHash = new HashMap();
        this.addCurrentLocationToAddresses();
        this.addDatabaseEntriesToAddresses();
    }


    private void notifyGlobalDataSetChanged() {
        // TODO this function provides a mechanism for notifying all (active) list adapters in the app when the dataset gets updated.
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }


    public void removeAddress(Address address) {
        addressFeedHash.remove(address);
        addresses.remove(address);
    }


    public void addAddress(Address address) {
        addresses.add(address);
        ArrayList<Feed> feed = getFeedsForAddress(address);
        addressFeedHash.put(address,feed);
    }


    public ArrayList<Feed> getFeedsForAddress(Address addr) {
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
        HttpRequestHandler.getInstance(appContext).requestFeeds(addr.getLatitude(), addr.getLongitude(), response, error);

        return result;
    }

}
