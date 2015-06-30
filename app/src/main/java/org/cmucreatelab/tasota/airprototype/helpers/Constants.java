package org.cmucreatelab.tasota.airprototype.helpers;

/**
 * Created by mike on 6/11/15.
 */
public final class Constants {

    public static final String LOG_TAG = "AirPrototype";
    // these are the channel names that we want our feeds to report
    public static final String[] channelNames = {
            "pm2_5", "PM2_5", "pm2_5_1hr",
            "pm2_5_24hr", "PM25B_UG_M3", "PM25_UG_M3"
    };
    public static final String APP_PACKAGE_NAME = "org.cmucreatelab.tasota.airprototype";
    public static final String BROADCAST_INTENT_FILTER = APP_PACKAGE_NAME + ".broadcastintentfilter";

    public static final class SpeckReading {
        public static final String[] normalColors = {
                "#1a9850", "#91cf60", "#d9ef8b",
                "#FEE08B", "#FC8D59", "#D73027"
        };
        public static final String[] colorblindColors = {
                "#4575b4", "#91bfdb", "#e0f3f8",
                "#fee090", "#fc8d59", "#d73027"
        };
        public static final String[] descriptions = {
                "Good", "Moderate", "Slightly Elevated",
                "Elevated", "High", "Very High"
        };
        // ranges measured in ug/m^3
        private static final int[] ranges = {
            21, 41, 81,
            161, 321
        };

        public static int getIndexFromReading(double reading) {
            if (reading < 0) {
                return -1;
            }
            int i;
            for (i=0;i<ranges.length;i++) {
                if (reading < ranges[i]) {
                    break;
                }
            }
            return i;
        }
    }

    public final class AddressIntent {
        public static final int MAX_RESULTS = 1;
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String RESULT_DATA_KEY = APP_PACKAGE_NAME + ".addressName";
        public static final String RECEIVER = APP_PACKAGE_NAME + ".receiver";
    }

    public final class AddressList {
        public final static String ADDRESS_INDEX = APP_PACKAGE_NAME + ".addressindex";
    }

    public final class Esdr {
        public static final String API_URL = "https://esdr.cmucreatelab.org";
        public static final String GRANT_TYPE_TOKEN = "password";
        public static final String GRANT_TYPE_REFRESH = "refresh_token";
        // TODO placeholder for actual Client information (don't push to git)
        public static final String CLIENT_ID = "client_id";
        public static final String CLIENT_SECRET = "this should never work";
    }

    public final class MapGeometry {
        // Distance from central point, in kilometers (box dimension will be 2x larger)
        public static final double BOUNDBOX_HEIGHT = 20.0;
        // Distance from central point, in kilometers (box dimension will be 2x larger)
        public static final double BOUNDBOX_LENGTH = 20.0;
        // radius of Earth (in kilometers)
        public static final double RADIUS_EARTH = 6371.0;
        // ASSERT these values will be less than 90.0
        public static final double BOUNDBOX_LAT = BOUNDBOX_HEIGHT / ( RADIUS_EARTH * 2 * Math.PI ) * 360.0;
        public static final double BOUNDBOX_LONG = BOUNDBOX_LENGTH / ( RADIUS_EARTH * 2 * Math.PI ) * 360.0;
    }

}
