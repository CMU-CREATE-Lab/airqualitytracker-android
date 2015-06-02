package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;

import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.classes.Feed;

import java.util.ArrayList;

/**
 * Created by mike on 6/2/15.
 */
public class GlobalHandler {

    private static GlobalHandler classInstance;
    private Context appContext;
    public ArrayList<Feed> feeds;
    public ArrayList<Address> addresses;


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        this.appContext = ctx;
        this.feeds = new ArrayList();
        this.addresses = new ArrayList();
        this.populateTemp();
    }


    // TODO these are temporary test values populated; do not keep this forever!
    private void populateTemp() {
        Address a = new Address("15235", 40.4586216, -79.8184684);
        a.set_id(1);
        this.addresses.add(a);
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }

}
