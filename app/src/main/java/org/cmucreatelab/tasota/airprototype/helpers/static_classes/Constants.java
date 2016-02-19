package org.cmucreatelab.tasota.airprototype.helpers.static_classes;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.R;
import java.util.HashMap;

/**
 * Created by mike on 6/11/15.
 */
public final class Constants {

    public static final String LOG_TAG = "AirPrototype";

    public static final String APP_PACKAGE_NAME = "org.cmucreatelab.tasota.airprototype";

    public static final String APP_VERSION = "1.8";

    public static final boolean USES_BACKGROUND_SERVICES = true;

    public static final class AppSecrets {
        // TODO placeholder for actual Client information (don't push to git)
        public static final String ESDR_CLIENT_ID = "client_id";
        public static final String ESDR_CLIENT_SECRET = "this should never work";
        public static final String AIR_NOW_API_KEY = "this-is-your-airnow-key-dont-push";
    }

    // TODO settings key-values & default
    public static final HashMap<String, Object> DEFAULT_SETTINGS = new HashMap(){{
        put(SettingsKeys.appUsesLocation, true);
        put(SettingsKeys.userLoggedIn, false);
        put(SettingsKeys.username, "");
        put(SettingsKeys.accessToken, "");
        put(SettingsKeys.refreshToken, "");
        put(SettingsKeys.userId, new Long(-1));
        put(SettingsKeys.expiresAt, new Long(0));
        put(SettingsKeys.addressLastPosition, 1);
        put(SettingsKeys.speckLastPosition, 1);
        put(SettingsKeys.blacklistedDevices, "");
    }};

    // these are the channel names that we want our feeds to report
    public static final String[] channelNames = {
            "pm2_5", "PM2_5", "pm2_5_1hr",
            "pm2_5_24hr", "PM25B_UG_M3", "PM25_UG_M3",
            "particle_concentration"
    };

    public static final long READINGS_MAX_TIME_RANGE = 86400; // 24 hours

    public static final long SPECKS_MAX_TIME_RANGE = 1800; // 30 minutes

    public static final long ESDR_TOKEN_TIME_TO_UPDATE_ON_REFRESH = 86400; // 24 hours

    public static final class SettingsKeys {
        public static final String appUsesLocation = "app_uses_location";
        public static final String userLoggedIn = "user_logged_in";
        public static final String username = "username";
        public static final String accessToken = "access_token";
        public static final String refreshToken = "refresh_token";
        public static final String userId = "user_id";
        public static final String expiresAt = "expires_at";
        public static final String addressLastPosition = "address_last_position";
        public static final String speckLastPosition = "speck_last_position";
        public static final String blacklistedDevices = "blacklisted_devices";
    }

    public static final class Location {
        public static final long LOCATION_REQUEST_INTERVAL = 600000; // 10 minutes
        public static final long LOCATION_REQUEST_FASTEST_INTERVAL = 60000; // 1 minute
    }

    public static final class Units {
        public static final String MICROGRAMS_PER_CUBIC_METER = "µg/m³";
        public static final String AQI = "AQI";
        public static final String RANGE_MICROGRAMS_PER_CUBIC_METER = "µg/m³    /500";
        public static final String RANGE_AQI = "AQI    /500";
    }

    public final class Esdr {
        public static final String API_URL = "https://esdr.cmucreatelab.org";
        public static final String GRANT_TYPE_TOKEN = "password";
        public static final String GRANT_TYPE_REFRESH = "refresh_token";
    }

    public static final class AirNow {
        public static final String API_URL = "http://www.airnowapi.org";
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

    // Interface-related tags

    public static final String[] HEADER_TITLES = {
            "SPECK DEVICES",
            "CITIES"
    };

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
        public static final long ALARM_INTERVAL_MILLISECONDS = 86400000; // 24 hours
    }

    public final class AddressList {
        public static final String ADDRESS_INDEX = APP_PACKAGE_NAME + ".addressindex";
    }

    public final class StickyGrid {
        public static final String GRID_TAG = "tag_grid_fragment";
        public static final String KEY_HEADER_POSITIONING = "key_header_mode";
        public static final String KEY_MARGINS_FIXED = "key_margins_fixed";
        public static final boolean MARGINS_ARE_FIXED = true;
        public static final int HEADER_DISPLAY = 1;
        public static final int VIEW_TYPE_HEADER = 0x09;
        public static final int VIEW_TYPE_CONTENT = 0x00;
    }

    // Content for Readable

    public static final class DefaultReading {
        public static final String DEFAULT_LOCATION = "N/A";
        public static final String DEFAULT_COLOR_BACKGROUND = "#404041";
        public static final String DEFAULT_TITLE = "Unavailable";
        public static final String DEFAULT_DESCRIPTION = "The current AQI for this region is unavailable.";
    }

    public static final class SpeckReading {
        // TODO replace with speck descriptions?
        public static final String[] descriptions = {
                "Air quality is considered Good.",
                "Air quality is considered Moderate.",
                "Air quality is considered Slightly Elevated.",
                "Air quality is considered Elevated.",
                "Air quality is considered High.",
                "Air quality is considered Very High."
        };
        public static final String[] normalColors = {
                "#1a9850", "#91cf60", "#d9ef8b",
                "#FEE08B", "#FC8D59", "#D73027"
        };
        public static final String[] colorblindColors = {
                "#4575b4", "#91bfdb", "#e0f3f8",
                "#fee090", "#fc8d59", "#d73027"
        };
        public static final String[] titles = {
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
                    return i;
                }
            }
            return -1;
        }

        public static String getRangeFromIndex(int index) {
            String result;
            if (index < 0) {
                Log.e(LOG_TAG, "getRangeFromIndex received index < 0.");
                result = "";
            } else if (index == 0) {
                result = "0-" + ranges[0];
            } else if (index == 5) {
                result = ranges[4] + "+";
            } else {
                result = ranges[index-1] + "-" + ranges[index];
            }
            return result;
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
                "This would trigger a health warning of emergency conditions. The entire " +
                        "population is more likely to be affected."
        };
        public static final String[] titles = {
                "Good", "Moderate", "Unhealthy for Sensitive Groups",
                "Unhealthy", "Very Unhealthy", "Hazardous"
        };
        public static final String[] aqiColors = {
                "#a3ba5c", "#e9b642", "#e98c37",
                "#e24f36", "#b54382", "#b22651"
        };
        public static final String[] aqiFontColors = {
                "#192015", "#2a1e11", "#261705",
                "#330004", "#2d0d18", "#28060b"
        };
        public static final int[] aqiDrawableGradients = {
                R.drawable.gradient_0, R.drawable.gradient_1, R.drawable.gradient_2,
                R.drawable.gradient_3, R.drawable.gradient_4, R.drawable.gradient_5
        };
        // these are currently hardcoded into the xml files and are just placeholders in the code for the time being.
        private static final String[] aqiGradientColorStart = {
                "#a3ba5c", "#e9b642", "#e98c37",
                "#e24f36", "#b54382", "#b22651"
        };
        // these are currently hardcoded into the xml files and are just placeholders in the code for the time being.
        private static final String[] aqiGradientColorEnd = {
                "#7a9055", "#c18f35", "#b44c26",
                "#ad2227", "#992a68", "#8c1739"
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
                    return i;
                }
            }
            return -1;
        }

        public static String getRangeFromIndex(int index) {
            String result;
            if (index < 0) {
                Log.e(LOG_TAG, "getRangeFromIndex received index < 0.");
                result = "";
            } else if (index == 0) {
                result = "0-" + ranges[0];
            } else if (index == 5) {
                result = ranges[4] + "+";
            } else {
                result = ranges[index-1] + "-" + ranges[index];
            }
            return result;
        }
    }
}
