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
    // TODO feeds will need to be associated with addresses
//    public ArrayList<Feed> feeds;
    public ArrayList<Address> addresses;
    public HashMap<Address,ArrayList<Feed>> addressFeedHash;


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        this.appContext = ctx;
//        this.feeds = new ArrayList();
        this.addresses = new ArrayList();
        this.addressFeedHash = new HashMap();
        this.populateTemp();
    }


    // TODO these are temporary test values populated; do not keep this forever!
    private void populateTemp() {
//        for (int i=0;i<10;i++) {
//            Address a = new Address("15235", 40.4586216, -79.8184684);
//            a.set_id(i);
//            this.addresses.add(a);
//        }
        // TODO this will be your GPS location, eventually
        Address gps = new Address("15235", 40.4586216, -79.8184684);
        //a.set_id(1);
        ArrayList<Feed> gFeed = getFeedsForAddress(gps);
        this.addresses.add(gps);
        this.addressFeedHash.put(gps, gFeed);

        // TODO get database entries and add to hash
        String[] projection = {
                "_id",
                AddressContract.COLUMN_NAME,
                AddressContract.COLUMN_LATITUDE,
                AddressContract.COLUMN_LONGITUDE
        };
        AddressDbHelper mDbHelper = new AddressDbHelper(appContext);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor c = db.query(AddressContract.TABLE_NAME, projection,
                null, null, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                null // sort order
        );
        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                // read record
                int id = c.getInt(c.getColumnIndexOrThrow("_id"));
                String name = c.getString(c.getColumnIndexOrThrow("name"));
                double latd = Double.parseDouble(c.getString(c.getColumnIndexOrThrow("_id")));
                double longd = Double.parseDouble(c.getString(c.getColumnIndexOrThrow("_id")));
                Log.i("addToDatabase", "READ RECORD _id=" + id);

                // add to data structure
                Address a = new Address(name, latd, longd);
                a.set_id(id);
                ArrayList<Feed> f = getFeedsForAddress(a);
                this.addresses.add(a);
                this.addressFeedHash.put(a, f);

                // iterate
                c.moveToNext();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
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
                    JSONArray jsonFeeds = response.getJSONObject("data").getJSONArray("rows");
                    int size = jsonFeeds .length();
                    for (int i=0;i<size;i++) {
                        JSONObject feed = (JSONObject)jsonFeeds.get(i);
                        Feed f = Feed.parseFeedFromJson(feed);
                        result.add(f);
                    }
                } catch (Exception e) {
                    // TODO catch exception "failed to find JSON attr"
                    e.printStackTrace();
                }


//                feedsListAdapter.notifyDataSetChanged();
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
