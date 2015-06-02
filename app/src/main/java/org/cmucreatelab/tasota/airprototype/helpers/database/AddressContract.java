package org.cmucreatelab.tasota.airprototype.helpers.database;

import android.provider.BaseColumns;

/**
 * Created by mike on 6/1/15.
 */
public final class AddressContract implements BaseColumns {
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
}
