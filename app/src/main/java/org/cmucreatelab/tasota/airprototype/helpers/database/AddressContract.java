package org.cmucreatelab.tasota.airprototype.helpers.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mike on 6/1/15.
 */
public class AddressContract extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "addresses";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE= "latitude";
    public static final String COLUMN_LONGITUDE= "longitude";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_NAME + " TEXT" +
            ", " + COLUMN_LATITUDE + " TEXT" +
            ", " + COLUMN_LONGITUDE + " TEXT" +
            // ...
            ") ";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "AddressReader.db";

    public AddressContract(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE );
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
