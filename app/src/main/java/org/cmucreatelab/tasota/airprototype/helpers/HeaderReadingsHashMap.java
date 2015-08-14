package org.cmucreatelab.tasota.airprototype.helpers;

import android.location.Location;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.activities.address_list.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by mike on 8/14/15.
 */
public class HeaderReadingsHashMap {

    private static final String[] HEADER_TITLES = {
            "SPECK DEVICES",
            "CITIES"
    };

    private GlobalHandler globalHandler;
    private SimpleAddress gpsAddress; // also listed in addresses
    // using ArrayList ensures an ordered list
    private final ArrayList<SimpleAddress> addresses;
    private final ArrayList<Speck> specks;
    // section headers for the table; also should match the the hashmap keys
    private final ArrayList<String> headers;
    // the hashmap containing addresses, specks, and headers
    protected HashMap<String,ArrayList> hashMap;
    // this is the data structure used by StickyGridAdapter
    public final ArrayList<StickyGridAdapter.LineItem> adapterList = new ArrayList<>();


    public HeaderReadingsHashMap(GlobalHandler globalHandler) {
        this.addresses = new ArrayList<>();
        this.specks = new ArrayList<>();
        this.headers = new ArrayList<>();
        this.hashMap = new HashMap<>();
        this.gpsAddress = new SimpleAddress("Loading Current Location...", "", 0.0, 0.0, true);
        this.globalHandler = globalHandler;

        // populate addresses from database
        ArrayList<SimpleAddress> dbAddresses = AddressDbHelper.fetchAddressesFromDatabase(this.globalHandler.appContext);
        addresses.addAll(dbAddresses);
        // TODO populate specks

        // construct hashmap
        Collections.addAll(headers, HEADER_TITLES);
        // TODO use specks instead of addresses
        this.hashMap.put(HEADER_TITLES[0], addresses);
        this.hashMap.put(HEADER_TITLES[1], addresses);
        populateAdapterList();
    }
    
    
    public void populateAdapterList() {
//        int headerCount = 0;
//        int sectionFirstPosition = 0;
//        int position = 0;
//        // header 1
//        // Insert new header view
////        sectionFirstPosition = headerCount + position;
//        headerCount += 1;
//        adapterList.add(new StickyGridAdapter.LineItem("SPECK DEVICES", true, sectionFirstPosition));
//        // grid cells
//        // TODO use real specks object
//        for (SimpleAddress simpleAddress : addresses) {
//            position += 1;
//            adapterList.add(new StickyGridAdapter.LineItem(simpleAddress.getName(), false, sectionFirstPosition));
//        }
//        // header 2
//        // Insert new header view
//        sectionFirstPosition = headerCount + position;
//        headerCount += 1;
//        adapterList.add(new StickyGridAdapter.LineItem("CITIES", true, sectionFirstPosition));
//        // grid cells
//        for (SimpleAddress simpleAddress : addresses) {
//            position += 1;
//            adapterList.add(new StickyGridAdapter.LineItem(simpleAddress.getName(), false, sectionFirstPosition));
//        }

        int headerCount = 0;
        int sectionFirstPosition = 0;
        int position = 0;
        for (String header : headers) {
            sectionFirstPosition = headerCount + position;
            headerCount += 1;
            adapterList.add(new StickyGridAdapter.LineItem(header, true, sectionFirstPosition));
            // grid cells
            for (Readable r : (ArrayList<Readable>)hashMap.get(header)) {
                position += 1;
                adapterList.add(new StickyGridAdapter.LineItem(r.toString(), false, sectionFirstPosition));
            }
        }
    }


    public void setGpsAddressLocation(Location location) {
        gpsAddress.setLatitude(location.getLatitude());
        gpsAddress.setLongitude(location.getLongitude());

        // update the gps address with the new closest feeds
        ArrayList<Feed> feeds = gpsAddress.pullFeeds(globalHandler);
    }


    public void addReading(Readable readable) {
        Readable.Type type = readable.getReadableType();
        switch(type) {
            case ADDRESS:
                this.addresses.add((SimpleAddress)readable);
                break;
            case SPECK:
                this.specks.add((Speck)readable);
                break;
            default:
                Log.e(Constants.LOG_TAG, "WARNING tried to add Readable of unknown Type in HeaderReadingsHashMap ");
        }
    }


    public void removeReading(Readable readable) {
        Readable.Type type = readable.getReadableType();
        switch(type) {
            case ADDRESS:
                this.addresses.remove((SimpleAddress) readable);
                break;
            case SPECK:
                this.specks.remove((Speck)readable);
                break;
            default:
                Log.e(Constants.LOG_TAG, "WARNING tried to add Readable of unknown Type in HeaderReadingsHashMap ");
        }
    }


    public void updateAddresses() {
        for (SimpleAddress address : this.addresses) {
            address.updateFeeds(globalHandler);
        }
    }

}
