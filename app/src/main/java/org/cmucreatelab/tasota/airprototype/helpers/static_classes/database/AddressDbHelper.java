package org.cmucreatelab.tasota.airprototype.helpers.static_classes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 6/12/15.
 */
public class AddressDbHelper {


    // destroy address in database
    public static boolean destroy(SimpleAddress simpleAddress, Context context) {
        if (simpleAddress.get_id() < 0) {
            return false;
        } else {
            AddressContract mDbHelper;
            SQLiteDatabase db;
            String selection = "_id LIKE ?";
            String[] selectionArgs = { String.valueOf(simpleAddress.get_id()) };
            int result;

            mDbHelper = new AddressContract(context);
            db = mDbHelper.getWritableDatabase();
            result = db.delete(AddressContract.TABLE_NAME, selection, selectionArgs);
            if (result == 1) {
                Log.i(Constants.LOG_TAG, "deleted address _id=" + simpleAddress.get_id());
            } else {
                Log.w(Constants.LOG_TAG, "Attempted to delete address _id=" +
                        simpleAddress.get_id() + " but deleted " + result + " items.");
            }
            return (result > 0);
        }
    }

    public static void addAddressToDatabase(Context ctx, SimpleAddress address) {
        AddressContract mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;

        mDbHelper = new AddressContract(ctx);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(AddressContract.COLUMN_NAME, address.getName());
        values.put(AddressContract.COLUMN_ZIPCODE, address.getZipcode());
        values.put(AddressContract.COLUMN_LATITUDE, String.valueOf(address.getLatitude()));
        values.put(AddressContract.COLUMN_LONGITUDE, String.valueOf(address.getLongitude()));
        newId = db.insert(AddressContract.TABLE_NAME, "null", values);

        address.set_id(newId);
        Log.i(Constants.LOG_TAG, "inserted new address _id=" + newId);
    }


    public static SimpleAddress createAddressInDatabase(Context ctx, String name, String zipcode, double latitude, double longitude) {
        AddressContract mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;
        SimpleAddress simpleAddress;

        mDbHelper = new AddressContract(ctx);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(AddressContract.COLUMN_NAME, name);
        values.put(AddressContract.COLUMN_ZIPCODE, zipcode);
        values.put(AddressContract.COLUMN_LATITUDE, String.valueOf(latitude));
        values.put(AddressContract.COLUMN_LONGITUDE, String.valueOf(longitude));
        newId = db.insert(AddressContract.TABLE_NAME, "null", values);
        simpleAddress = new SimpleAddress(name,zipcode,latitude,longitude);
        simpleAddress.set_id(newId);
        Log.i(Constants.LOG_TAG, "inserted new address _id=" + newId);

        return simpleAddress;
    }


    public static SimpleAddress generateAddressFromRecord(Cursor cursor) throws IllegalArgumentException {
        SimpleAddress simpleAddress;
        int id;
        String name,zipcode;
        double latd,longd;

        try {
            // read record
            id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            zipcode = cursor.getString(cursor.getColumnIndexOrThrow("zipcode"));
            latd = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("latitude")));
            longd = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("longitude")));
            Log.v(Constants.LOG_TAG, "Read address record _id=" + id);

            // add to data structure
            simpleAddress = new SimpleAddress(name, zipcode, latd, longd);
            simpleAddress.set_id(id);
            return simpleAddress;
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to read from cursor! cursor.toString()=" + cursor.toString());
            throw e;
        }
    }


    public static ArrayList<SimpleAddress> fetchAddressesFromDatabase(Context ctx) {
        String[] projection = {
                "_id",
                AddressContract.COLUMN_NAME, AddressContract.COLUMN_ZIPCODE,
                AddressContract.COLUMN_LATITUDE, AddressContract.COLUMN_LONGITUDE
        };
        AddressContract mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;
        ArrayList<SimpleAddress> result = new ArrayList<>();

        mDbHelper = new AddressContract(ctx);
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

        if (result.size() == 0) {
            Log.w(Constants.LOG_TAG, "fetchAddressesFromDatabase is returning an empty list.");
        }
        return result;
    }

}
