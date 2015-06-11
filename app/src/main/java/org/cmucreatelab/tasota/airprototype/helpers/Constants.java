package org.cmucreatelab.tasota.airprototype.helpers;

/**
 * Created by mike on 6/11/15.
 */
public final class Constants {

    public final class AddressIntent {
        public static final int MAX_RESULTS = 1;
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME = "org.cmucreatelab.tasota.airprototype";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".addressName";
        public static final String RECEIVER = PACKAGE_NAME + ".receiver";
    }

    public final class MapGeometry {
        // Distance from central point, in kilometers (box dimension will be 2x larger)
        public static final double BOUNDBOX_HEIGHT = 20.0;
        // Distance from central point, in kilometers (box dimension will be 2x larger)
        public static final double BOUNDBOX_LENGTH = 20.0;
        // radius of Earth
        public static final double RADIUS_EARTH = 6371.0;
        // ASSERT these values will be less than 90.0
        public static final double BOUNDBOX_LAT = BOUNDBOX_HEIGHT / ( RADIUS_EARTH * 2 * Math.PI ) * 360.0;
        public static final double BOUNDBOX_LONG = BOUNDBOX_LENGTH / ( RADIUS_EARTH * 2 * Math.PI ) * 360.0;
    }

}
