package org.cmucreatelab.tasota.airprototype.helpers.static_classes;

import android.location.Location;

import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

/**
 * Created by mike on 12/22/15.
 */
public class GpsReadingHandler {

    public SimpleAddress gpsAddress;
    private GlobalHandler globalHandler;


    public GpsReadingHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.gpsAddress = new SimpleAddress("Loading Current Location...", "", 0.0, 0.0, true);
    }


    public void setGpsAddressLocation(Location location) {
        gpsAddress.setLatitude(location.getLatitude());
        gpsAddress.setLongitude(location.getLongitude());
        globalHandler.esdrFeedsHandler.requestUpdateFeeds(gpsAddress);
    }

}
