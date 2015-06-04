package org.cmucreatelab.tasota.airprototype.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressContract;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressDbHelper;

/**
 * Created by mike on 6/1/15.
 */
public class Address {

    private long _id;
    private String name;
    private double latitude,longitude;

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


    public Address(String name, double latitude, double longitude) {
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
            Log.i("Address", "DELETED ADDRESS RECORD id=" + this._id);

            return true;
        }
    }


    public static Address createAddressInDatabase(Context ctx, String name, double latitude, double longitude) {
        AddressDbHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;
        Address address;

        mDbHelper = new AddressDbHelper(ctx);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(AddressContract.COLUMN_NAME, name);
        values.put(AddressContract.COLUMN_LATITUDE, String.valueOf(latitude));
        values.put(AddressContract.COLUMN_LONGITUDE, String.valueOf(longitude));
        newId = db.insert(AddressContract.TABLE_NAME, "null", values);
        Log.i("Address", "INSERTED RECORD INTO DATABASE id=" + newId);
        address = new Address(name,latitude,longitude);
        address.set_id(newId);

        return address;
    }

}
