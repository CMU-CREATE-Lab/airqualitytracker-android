package org.cmucreatelab.tasota.airprototype.helpers.static_classes;


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
    public static final boolean USES_BACKGROUND_SERVICES = true;

    public static final class Location {
        public static final long LOCATION_REQUEST_INTERVAL = 600000;
        public static final long LOCATION_REQUEST_FASTEST_INTERVAL = 60000;
    }

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

    public static final class AqiReading {
        public static final String[] descriptions = {
                "Air quality is considered satisfactory, and air pollution poses little or no risk.",
                "Air quality is acceptable; however, for some pollutants there may be a moderate " +
                        "health concern for a very small number of people. For example, people " +
                        "who are unusually sensitive to ozone may experience respiratory symptoms.",
                "Although general public is not likely to be affected at this AQI range, people " +
                        "with lung disease, older adults and children are at a greater risk from " +
                        "exposure to ozone, whereas persons with heart and lung disease, older " +
                        "adults and children are at greater risk from the presence of particles " +
                        "in the air.",
                "Everyone may begin to experience some adverse health effects, and members of the " +
                        "sensitive groups may experience more serious effects.",
                "This would trigger a health alert signifying that everyone may experience more " +
                        "serious health effects.",
                "This would trigger a health warnings of emergency conditions. The entire " +
                        "population is more likely to be affected."
        };
        public static final String[] titles = {
                "Good", "Moderate", "Unhealthy for Sensitive Groups",
                "Unhealthy", "Very Unhealthy", "Hazardous"
        };
        private static final int[] ranges = {
                50, 100, 150,
                200, 300
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
        public static final String RECEIVER = APP_PACKAGE_NAME + ".addressintent.receiver";
    }

    public final class EsdrRefreshIntent {
        public static final String RECEIVER = APP_PACKAGE_NAME + ".esdrrefreshintent.receiver";
        public static final String ALARM_RECEIVER = RECEIVER + ".alarmmanager";
        // TODO this interval should be much larger (days, even 1 week); shortened for testing only
        public static final long ALARM_INTERVAL_MILLISECONDS = 60000;
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
