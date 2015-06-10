package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressContract;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressDbHelper;
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
    public ArrayList<Address> addresses;
    public HashMap<Address,ArrayList<Feed>> addressFeedHash;

    private GoogleApiClient googleApiClient;
    private com.google.android.gms.location.LocationListener locationListener;
    private Location lastLocation;


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


    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(appContext)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        updateCurrentLocation();
                        Log.i("DEBUG", "last known location is " + lastLocation.toString());
//                        startLocationUpdates();
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                        // TODO handle suspended connection
                        Log.i("DEBUG","onConnectionSuspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        // TODO handle failed connection
                        Log.i("DEBUG","onConnectionFailed");
                    }
                })
                .addApi(LocationServices.API)
                .build();
        locationListener = new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location;
                updateCurrentLocation();
                Log.i("DEBUG", "LOCATION WAS UPDATED TO " + lastLocation.toString());
//                stopLocationUpdates();
            }
        };
        // make sure you actually CONNECT the api client for it to do anything (so much hatred)
        googleApiClient.connect();
    }


    private void updateCurrentLocation() {
        Address a = addresses.get(0);
        a.setName(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        a.setLatitude(lastLocation.getLatitude());
        a.setLongitude(lastLocation.getLongitude());
//        listAdapter.notifyDataSetChanged();
    }


    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, locationListener);
    }


    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
    }


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        this.appContext = ctx;
        this.addresses = new ArrayList();
        this.addressFeedHash = new HashMap();
        this.addCurrentLocationToAddresses();
        this.addDatabaseEntriesToAddresses();
        this.initGoogleApiClient();
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
