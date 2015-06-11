package org.cmucreatelab.tasota.airprototype.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressContract;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressDbHelper;
import java.util.ArrayList;

/**
 * Created by mike on 6/1/15.
 */
public class SimpleAddress {

    private long _id;
    private String name;
    private double latitude,longitude;
    private Feed closestFeed = null;

    public long get_id() {
        return _id;
    }
    public void set_id(long _id) {
        this._id = _id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public Feed getClosestFeed() {
        return closestFeed;
    }
    public void setClosestFeed(Feed closestFeed) {
        this.closestFeed = closestFeed;
    }


    public SimpleAddress(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    // destroy address in database
    public boolean destroy(Context ctx) {
        if (this._id < 0) {
            return false;
        } else {
            AddressDbHelper mDbHelper;
            SQLiteDatabase db;
            String selection;
            String[] selectionArgs = { String.valueOf(this._id) };

            mDbHelper = new AddressDbHelper(ctx);
            db = mDbHelper.getWritableDatabase();
            selection = "_id LIKE ?";
            db.delete(AddressContract.TABLE_NAME, selection, selectionArgs);
            Log.i("SimpleAddress", "DELETED ADDRESS RECORD id=" + this._id);

            return true;
        }
    }


    public static SimpleAddress createAddressInDatabase(Context ctx, String name, double latitude, double longitude) {
        AddressDbHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;
        SimpleAddress simpleAddress;

        mDbHelper = new AddressDbHelper(ctx);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(AddressContract.COLUMN_NAME, name);
        values.put(AddressContract.COLUMN_LATITUDE, String.valueOf(latitude));
        values.put(AddressContract.COLUMN_LONGITUDE, String.valueOf(longitude));
        newId = db.insert(AddressContract.TABLE_NAME, "null", values);
        Log.i("SimpleAddress", "INSERTED RECORD INTO DATABASE id=" + newId);
        simpleAddress = new SimpleAddress(name,latitude,longitude);
        simpleAddress.set_id(newId);

        return simpleAddress;
    }


    public static SimpleAddress generateAddressFromRecord(Cursor cursor) throws IllegalArgumentException {
        SimpleAddress simpleAddress;
        ArrayList<Feed> feed;
        int id;
        String name;
        double latd,longd;

        // read record
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        latd = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("latitude")));
        longd = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("longitude")));
        Log.i("addToDatabase", "READ RECORD _id=" + id);

        // add to data structure
        simpleAddress = new SimpleAddress(name, latd, longd);
        simpleAddress.set_id(id);
        return simpleAddress;
    }


    public static ArrayList<SimpleAddress> fetchAddressesFromDatabase(Context ctx) {
        String[] projection = {
                "_id",
                AddressContract.COLUMN_NAME,
                AddressContract.COLUMN_LATITUDE,
                AddressContract.COLUMN_LONGITUDE
        };
        AddressDbHelper mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;
        ArrayList<SimpleAddress> result = new ArrayList<>();

        mDbHelper = new AddressDbHelper(ctx);
        db = mDbHelper.getWritableDatabase();
        cursor = db.query(AddressContract.TABLE_NAME, projection,
                null, null, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                null // sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                result.add(generateAddressFromRecord(cursor));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // iterate
            cursor.moveToNext();
        }

        return result;
    }

}
