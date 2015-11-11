package org.cmucreatelab.tasota.airprototype.helpers.static_classes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 11/6/15.
 */
public class SpeckSensorSQLiteOpenHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "AddressReader.db";


    public SpeckSensorSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        Log.i(Constants.LOG_TAG, "(SQL COMMAND): " + AddressContract.SQL_CREATE_TABLE);
        db.execSQL(AddressContract.SQL_CREATE_TABLE);
        Log.i(Constants.LOG_TAG, "(SQL COMMAND): " + SpeckContract.SQL_CREATE_TABLE);
        db.execSQL(SpeckContract.SQL_CREATE_TABLE );
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(AddressContract.SQL_DROP_TABLE);
        db.execSQL(SpeckContract.SQL_DROP_TABLE);
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
