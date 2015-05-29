package org.cmucreatelab.tasota.airprototype;

import android.provider.BaseColumns;

/**
 * Created by mike on 5/28/15.
 */
public final class ChannelContract implements BaseColumns {
    public static final String TABLE_NAME = "channels";
    public static final String COLUMN_FEED_ID = "feed_id";
    public static final String COLUMN_CHANNEL_NAME = "channel_name";
    public static final String COLUMN_MIN_TIME= "min_time_seconds";
    public static final String COLUMN_MAX_TIME= "max_time_seconds";
    public static final String COLUMN_MIN_VALUE= "min_value";
    public static final String COLUMN_MAX_VALUE= "max_value";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_FEED_ID + " INTEGER" +
            ", " + COLUMN_CHANNEL_NAME + " TEXT" +
            ", " + COLUMN_MIN_TIME + " TEXT" +
            ", " + COLUMN_MAX_TIME + " TEXT" +
            ", " + COLUMN_MIN_VALUE + " TEXT" +
            ", " + COLUMN_MAX_VALUE + " TEXT" +
            // ...
            ") ";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
}
