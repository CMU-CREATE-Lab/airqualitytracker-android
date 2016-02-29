package org.cmucreatelab.tasota.airprototype.helpers.system.database;


/**
 * Created by mike on 6/1/15.
 */
public class AddressContract {

    public static final String TABLE_NAME = "addresses";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ZIPCODE = "zipcode";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_POSITION_ID = "positionId";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_NAME + " TEXT" +
            ", " + COLUMN_ZIPCODE + " TEXT" +
            ", " + COLUMN_LATITUDE + " TEXT" +
            ", " + COLUMN_LONGITUDE + " TEXT" +
            ", " + COLUMN_POSITION_ID + " INTEGER" +
            // ...
            ") ";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

}
