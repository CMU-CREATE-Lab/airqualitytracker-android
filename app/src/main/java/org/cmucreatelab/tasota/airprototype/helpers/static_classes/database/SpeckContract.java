package org.cmucreatelab.tasota.airprototype.helpers.static_classes.database;


/**
 * Created by mike on 6/1/15.
 */
public class SpeckContract {

    public static final String TABLE_NAME = "specks";

    public static final String COLUMN_API_KEY_READ_ONLY = "api_key_read_only";
    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_EXPOSURE = "exposure";
    public static final String COLUMN_FEED_ID = "feed_id";
    public static final String COLUMN_IS_MOBILE = "is_mobile";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_POSITION_ID = "position_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_API_KEY_READ_ONLY + " TEXT" +
            ", " + COLUMN_DEVICE_ID + " INTEGER" +
            ", " + COLUMN_EXPOSURE + " TEXT" +
            ", " + COLUMN_FEED_ID + " INTEGER" +
            ", " + COLUMN_IS_MOBILE + " INTEGER" +
            ", " + COLUMN_LATITUDE + " TEXT" +
            ", " + COLUMN_LONGITUDE + " TEXT" +
            ", " + COLUMN_NAME + " TEXT" +
            ", " + COLUMN_POSITION_ID + " INTEGER" +
            ", " + COLUMN_PRODUCT_ID + " INTEGER" +
            // ...
            ") ";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

}
