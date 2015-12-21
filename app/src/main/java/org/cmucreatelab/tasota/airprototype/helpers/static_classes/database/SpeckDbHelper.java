package org.cmucreatelab.tasota.airprototype.helpers.static_classes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.HttpRequestHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 6/12/15.
 */
public class SpeckDbHelper {


    // destroy speck in database
    public static boolean destroy(Speck speck, Context context) {
        if (speck.get_id() < 0) {
            return false;
        } else {
            SpeckSensorSQLiteOpenHelper mDbHelper;
            SQLiteDatabase db;
            String selection = "_id LIKE ?";
            String[] selectionArgs = { String.valueOf(speck.get_id()) };
            int result;

            mDbHelper = new SpeckSensorSQLiteOpenHelper(context);
            db = mDbHelper.getWritableDatabase();
            result = db.delete(SpeckContract.TABLE_NAME, selection, selectionArgs);
            if (result == 1) {
                Log.i(Constants.LOG_TAG, "deleted speck _id=" + speck.get_id());
            } else {
                Log.w(Constants.LOG_TAG, "Attempted to delete speck _id=" +
                        speck.get_id() + " but deleted " + result + " items.");
            }
            return (result > 0);
        }
    }

    public static void addSpeckToDatabase(Context ctx, Speck speck) {
        SpeckSensorSQLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;

        if (speck.getPositionId() <= 0) {
            speck.setPositionId(GlobalHandler.getInstance(ctx).positionIdHelper.getSpeckLastPosition());
        }

        mDbHelper = new SpeckSensorSQLiteOpenHelper(ctx);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(SpeckContract.COLUMN_API_KEY_READ_ONLY, speck.getApiKeyReadOnly());
        values.put(SpeckContract.COLUMN_DEVICE_ID, speck.getDeviceId());
        values.put(SpeckContract.COLUMN_EXPOSURE, speck.getExposure());
        values.put(SpeckContract.COLUMN_FEED_ID, speck.getFeed_id());
        values.put(SpeckContract.COLUMN_IS_MOBILE, speck.isMobile());
        values.put(SpeckContract.COLUMN_LATITUDE, String.valueOf(speck.getLatitude()));
        values.put(SpeckContract.COLUMN_LONGITUDE, String.valueOf(speck.getLongitude()));
        values.put(SpeckContract.COLUMN_NAME, speck.getName());
        values.put(SpeckContract.COLUMN_POSITION_ID, speck.getPositionId());
        values.put(SpeckContract.COLUMN_PRODUCT_ID, speck.getProductId());
        newId = db.insert(SpeckContract.TABLE_NAME, "null", values);

        speck.set_id(newId);
        Log.i(Constants.LOG_TAG, "inserted new speck _id=" + newId);
    }


    public static void updateSpeckInDatabase(Context context, Speck speck) {
        if (speck.get_id() >= 0) {
            SpeckSensorSQLiteOpenHelper mDbHelper;
            SQLiteDatabase db;
            String selection = "_id LIKE ?";
            String[] selectionArgs = { String.valueOf(speck.get_id()) };
            int result;
            ContentValues contentValues;

            mDbHelper = new SpeckSensorSQLiteOpenHelper(context);
            db = mDbHelper.getWritableDatabase();

            // find values to be updated
            contentValues = new ContentValues();
            contentValues.put(SpeckContract.COLUMN_NAME, speck.getName());
            contentValues.put(SpeckContract.COLUMN_LATITUDE, speck.getLatitude());
            contentValues.put(SpeckContract.COLUMN_LONGITUDE, speck.getLongitude());
            contentValues.put(SpeckContract.COLUMN_POSITION_ID, speck.getPositionId());
            contentValues.put(SpeckContract.COLUMN_DEVICE_ID, speck.getDeviceId());
            contentValues.put(SpeckContract.COLUMN_EXPOSURE, speck.getExposure());
            contentValues.put(SpeckContract.COLUMN_FEED_ID, speck.getFeed_id());
            contentValues.put(SpeckContract.COLUMN_IS_MOBILE, speck.isMobile());
            contentValues.put(SpeckContract.COLUMN_PRODUCT_ID, speck.getProductId());

            // perform update
            result = db.update(SpeckContract.TABLE_NAME, contentValues, selection, selectionArgs);
            if (result == 1) {
                Log.i(Constants.LOG_TAG, "updated speck _id=" + speck.get_id());
            } else {
                Log.w(Constants.LOG_TAG, "Attempted to update speck _id=" +
                        speck.get_id() + " but updated " + result + " items.");
            }
        }
    }

    public static Speck createSpeckInDatabase(
            Context ctx,
            String apiKeyReadOnly,
            long deviceId,
            String exposure,
            long feedId,
            boolean isMobile,
            double latitude,
            double longitude,
            String name,
            int positionId,
            long productId) {
        SpeckSensorSQLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;
        Speck speck = new Speck(apiKeyReadOnly,deviceId,exposure,feedId,isMobile,latitude,longitude,name,positionId,productId);

        mDbHelper = new SpeckSensorSQLiteOpenHelper(ctx);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(SpeckContract.COLUMN_API_KEY_READ_ONLY, speck.getApiKeyReadOnly());
        values.put(SpeckContract.COLUMN_DEVICE_ID, speck.getDeviceId());
        values.put(SpeckContract.COLUMN_EXPOSURE, speck.getExposure());
        values.put(SpeckContract.COLUMN_FEED_ID, speck.getFeed_id());
        values.put(SpeckContract.COLUMN_IS_MOBILE, speck.isMobile());
        values.put(SpeckContract.COLUMN_LATITUDE, String.valueOf(speck.getLatitude()));
        values.put(SpeckContract.COLUMN_LONGITUDE, String.valueOf(speck.getLongitude()));
        values.put(SpeckContract.COLUMN_NAME, speck.getName());
        values.put(SpeckContract.COLUMN_POSITION_ID, speck.getPositionId());
        values.put(SpeckContract.COLUMN_PRODUCT_ID, speck.getProductId());

        newId = db.insert(SpeckContract.TABLE_NAME, "null", values);
        speck.set_id(newId);
        Log.i(Constants.LOG_TAG, "inserted new speck _id=" + newId);

        return speck;
    }


    public static Speck generateSpeckFromRecord(Cursor cursor, Context context) throws IllegalArgumentException {
        Speck speck;
        int positionId;
        String apiKeyReadOnly,exposure,name;
        long id,feedId,productId,deviceId;
        boolean isMobile;
        double latitude,longitude;

        try {
            // read record
            id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            apiKeyReadOnly = cursor.getString(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_API_KEY_READ_ONLY));
            deviceId = cursor.getLong(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_DEVICE_ID));
            exposure = cursor.getString(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_EXPOSURE));
            feedId = cursor.getLong(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_FEED_ID));
            isMobile = cursor.getInt(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_IS_MOBILE)) == 1;
            latitude= Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_LATITUDE)));
            longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_LATITUDE)));
            name = cursor.getString(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_NAME));
            positionId = cursor.getInt(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_POSITION_ID));
            productId = cursor.getLong(cursor.getColumnIndexOrThrow(SpeckContract.COLUMN_PRODUCT_ID));

            Log.v(Constants.LOG_TAG, "Read Speck record _id=" + id);

            // add to data structure
            speck = new Speck(apiKeyReadOnly,deviceId,exposure,feedId,isMobile,latitude,longitude,name,positionId,productId);
            speck.set_id(id);
//            GlobalHandler.getInstance(context).httpRequestHandler.requestChannelsForSpeck(speck);
            return speck;
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to read from cursor! cursor.toString()=" + cursor.toString());
            throw e;
        }
    }


    public static ArrayList<Speck> fetchSpecksFromDatabase(Context ctx) {
        String[] projection = {
                "_id",
                SpeckContract.COLUMN_API_KEY_READ_ONLY, SpeckContract.COLUMN_DEVICE_ID,
                SpeckContract.COLUMN_EXPOSURE, SpeckContract.COLUMN_FEED_ID,
                SpeckContract.COLUMN_IS_MOBILE, SpeckContract.COLUMN_LATITUDE,
                SpeckContract.COLUMN_LONGITUDE, SpeckContract.COLUMN_NAME,
                SpeckContract.COLUMN_POSITION_ID, SpeckContract.COLUMN_PRODUCT_ID
        };
        SpeckSensorSQLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;
        ArrayList<Speck> result = new ArrayList<>();

        mDbHelper = new SpeckSensorSQLiteOpenHelper(ctx);
        db = mDbHelper.getWritableDatabase();
        cursor = db.query(SpeckContract.TABLE_NAME, projection,
                null, null, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                SpeckContract.COLUMN_POSITION_ID + " ASC" // sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                result.add(generateSpeckFromRecord(cursor,ctx));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // iterate
            cursor.moveToNext();
        }

        if (result.size() == 0) {
            Log.w(Constants.LOG_TAG, "fetchSpecksFromDatabase is returning an empty list.");
        }
        return result;
    }

}
