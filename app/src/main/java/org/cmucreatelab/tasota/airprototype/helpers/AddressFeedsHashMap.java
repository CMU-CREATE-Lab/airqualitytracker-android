package org.cmucreatelab.tasota.airprototype.helpers;

import android.location.Location;
import org.cmucreatelab.tasota.airprototype.classes.Speck;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mike on 6/11/15.
 */
public class AddressFeedsHashMap {

    private GlobalHandler globalHandler;
    protected SimpleAddress gpsAddress; // listed in addresses
    // this ArrayList ensures an ordered list of addresses
    // (required to react to AddressListActivity events and displaying on AddressShowActivity)
    protected ArrayList<SimpleAddress> addresses;
    protected ArrayList<Speck> specks; // TODO store specks
    protected HashMap<SimpleAddress,ArrayList<Feed>> hashMap;
    public SimpleAddress getGpsAddress() {
        return gpsAddress;
    }
    public ArrayList<SimpleAddress> getAddresses() {
        return addresses;
    }


    public AddressFeedsHashMap(GlobalHandler globalHandler) {
        this.addresses = new ArrayList<>();
        this.hashMap = new HashMap<>();
        this.gpsAddress = new SimpleAddress("Loading Current Location...", "", 0.0, 0.0, true);
        hashMap.put(gpsAddress, new ArrayList<Feed>());
        this.globalHandler = globalHandler;
        // populate addresses from database
        ArrayList<SimpleAddress> dbAddresses = AddressDbHelper.fetchAddressesFromDatabase(this.globalHandler.appContext);
        for (SimpleAddress simpleAddress : dbAddresses) {
            // prevents making http requests until we actually want to display something
//            ArrayList<Feed> feed = pullFeeds(simpleAddress);
            ArrayList<Feed> feed = new ArrayList<>();
            this.put(simpleAddress, feed);
        }
    }


    public void setGpsAddressLocation(Location location) {
        gpsAddress.setLatitude(location.getLatitude());
        gpsAddress.setLongitude(location.getLongitude());

        // update the gps address with the new closest feeds
        ArrayList<Feed> feeds = gpsAddress.pullFeeds(globalHandler);
        hashMap.put(gpsAddress, feeds);
    }


    public void removeAddress(SimpleAddress simpleAddress) {
        this.hashMap.remove(simpleAddress);
        addresses.remove(simpleAddress);
//        globalHandler.requestAddressesForDisplay();
    }


    public void addAddress(SimpleAddress simpleAddress) {
        ArrayList<Feed> feed = simpleAddress.pullFeeds(globalHandler);
        this.put(simpleAddress, feed);
//        globalHandler.requestAddressesForDisplay();
    }


    public void put(SimpleAddress simpleAddress, ArrayList<Feed> feeds) {
        if (addresses.indexOf(simpleAddress) < 0){
            addresses.add(simpleAddress);
        }
        this.hashMap.put(simpleAddress, feeds);
    }


    public ArrayList<Feed> getFeedsFromAddressInHashMap(SimpleAddress simpleAddress) {
        return this.hashMap.get(simpleAddress);
    }


    // Updates the feeds for all current addresses ("refresh")
    protected void updateAddresses() {
        for (SimpleAddress address : this.addresses) {
            this.put(address, address.pullFeeds(globalHandler));
        }
    }

}
