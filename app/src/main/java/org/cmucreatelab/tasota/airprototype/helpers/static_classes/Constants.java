package org.cmucreatelab.tasota.airprototype.helpers.static_classes;

import org.cmucreatelab.tasota.airprototype.classes.DayFeedValue;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import java.util.HashMap;

/**
 * Created by mike on 6/11/15.
 */
public final class Constants {

    public static final String LOG_TAG = "AirPrototype";

    public static final String APP_PACKAGE_NAME = "org.cmucreatelab.tasota.airprototype";

    public static final String APP_VERSION = "2.7";

    public static final boolean USES_BACKGROUND_SERVICES = true;

    // This should be either INSTANTCAST or NOWCAST
    public static final Feed.ReadableValueType DEFAULT_ADDRESS_READABLE_VALUE_TYPE = Feed.ReadableValueType.NOWCAST;

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

    public static final long TWENTY_FOUR_HOURS = 86400;

    public static final long READINGS_MAX_TIME_RANGE = TWENTY_FOUR_HOURS;

    public static final long SPECKS_MAX_TIME_RANGE = 1800; // 30 minutes

    public static final long ESDR_TOKEN_TIME_TO_UPDATE_ON_REFRESH = TWENTY_FOUR_HOURS;

    // controls whether or not the code will perform esdr token refresh requests
    public static final boolean REFRESHES_ESDR_TOKEN = true;

    // determines what value we want to iterate over to determine number of dirty days
    public static final DayFeedValue.DaysValueType DIRTY_DAYS_VALUE_TYPE = DayFeedValue.DaysValueType.MAX;

    // any day exceeding this AQI value is defined as dirty
    public static final int DIRTY_DAYS_AQI_THRESHOLD = 50;

    public static final class ManualOverrides {
        // strongly encouraged to also set REFRESHES_ESDR_TOKEN = false when using this option
        public static final boolean MANUAL_ESDR_LOGIN = false;
        public static final String username = "";
        public static final String accessToken = "";
        public static final String refreshToken = "";
        public static final long userId = 0;
    }

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
        public static final double BOUNDBOX_HEIGHT = 40.0;
        // Distance from central point, in kilometers (box dimension will be 2x larger)
        public static final double BOUNDBOX_LENGTH = 40.0;
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
        public static final long ALARM_INTERVAL_MILLISECONDS = TWENTY_FOUR_HOURS * 1000;
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

}
