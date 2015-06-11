package org.cmucreatelab.tasota.airprototype.helpers;

import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mike on 6/11/15.
 */
public class AddressFeedsHashMap {

    public SimpleAddress gpsAddress; // listed in addresses
    // this ArrayList ensures an ordered list of addresses
    // (required to react to AddressListActivity events and displaying on AddressShowActivity)
    public ArrayList<SimpleAddress> addresses;
    public HashMap<SimpleAddress,ArrayList<Feed>> hashMap;


    public AddressFeedsHashMap() {
        this.addresses = new ArrayList<>();
        this.hashMap = new HashMap<>();
        this.gpsAddress = new SimpleAddress("Loading Current Location...", 0.0, 0.0);
        this.put(gpsAddress, new ArrayList<Feed>());
    }


    public void removeAddress(SimpleAddress simpleAddress) {
        this.hashMap.remove(simpleAddress);
        addresses.remove(simpleAddress);
    }


    public void put(SimpleAddress simpleAddress, ArrayList<Feed> feeds) {
        if (addresses.indexOf(simpleAddress) < 0){
            addresses.add(simpleAddress);
        }
        this.hashMap.put(simpleAddress, feeds);
    }

}
