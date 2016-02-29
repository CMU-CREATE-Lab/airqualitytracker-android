package org.cmucreatelab.tasota.airprototype.helpers.system.services.gps;

import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;

/**
 * Created by mike on 12/22/15.
 */
public class GpsReadingHandler {

    public SimpleAddress gpsAddress;
    private GlobalHandler globalHandler;


    public GpsReadingHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.gpsAddress = new SimpleAddress("Loading Current Location...", "", new Location(0.0, 0.0), true);
    }


    public void setGpsAddressLocation(android.location.Location location) {
        gpsAddress.setLocation( new Location(location.getLatitude(), location.getLongitude()) );
        globalHandler.esdrFeedsHandler.requestUpdateFeeds(gpsAddress);
    }

}
