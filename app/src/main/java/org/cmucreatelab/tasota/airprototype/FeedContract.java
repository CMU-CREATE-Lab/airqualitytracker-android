package org.cmucreatelab.tasota.airprototype;

import android.provider.BaseColumns;

/**
 * Created by mike on 5/28/15.
 */
public final class FeedContract implements BaseColumns {
    public static final String TABLE_NAME = "feeds";
    public static final String COLUMN_FEED_ID = "feed_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EXPOSURE = "exposure";
    public static final String COLUMN_IS_MOBILE = "is_mobile";
    public static final String COLUMN_LAT= "latitude";
    public static final String COLUMN_LONG= "longitude";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_FEED_ID + " INTEGER" +
            ", " + COLUMN_NAME + " TEXT" +
            ", " + COLUMN_EXPOSURE + " TEXT" +
            ", " + COLUMN_IS_MOBILE + " INTEGER" +
            ", " + COLUMN_LAT + " TEXT" +
            ", " + COLUMN_LONG + " TEXT" +
            // ...
            ") ";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
}
