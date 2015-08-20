package org.cmucreatelab.tasota.airprototype.helpers;

import android.location.Location;
import android.util.Log;

import com.android.volley.Response;

import org.cmucreatelab.tasota.airprototype.activities.address_list.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.JsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.json.JSONObject;

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
    public SimpleAddress gpsAddress; // also listed in addresses
    // using ArrayList ensures an ordered list
    public final ArrayList<SimpleAddress> addresses;
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

        // construct hashmap
        Collections.addAll(headers, HEADER_TITLES);

        // populate addresses from database and specks from login info
        ArrayList<SimpleAddress> dbAddresses = AddressDbHelper.fetchAddressesFromDatabase(this.globalHandler.appContext);
        addresses.addAll(dbAddresses);
        populateSpecks();
    }
    
    
    public void populateAdapterList() {
        // Tracks the number of headers displayed
        int headerCount = 0;
        // Tracks current position in the list; used to label section headers and their contents
        int sectionFirstPosition;
        int position = 0;
        adapterList.clear();

        for (String header : headers) {
            ArrayList<Readable> items = (ArrayList<Readable>)hashMap.get(header);
            if (items.size() > 0) {
                sectionFirstPosition = headerCount + position;
                headerCount += 1;
                adapterList.add(new StickyGridAdapter.LineItem(header, true, sectionFirstPosition));
                // grid cells
                for (Readable r : items) {
                    position += 1;
                    adapterList.add(new StickyGridAdapter.LineItem(false, sectionFirstPosition, r));
                }
            }
        }
    }


    public void setGpsAddressLocation(Location location) {
        gpsAddress.setLatitude(location.getLatitude());
        gpsAddress.setLongitude(location.getLongitude());
        gpsAddress.requestUpdateFeeds(globalHandler);
    }


    public void addReading(Readable readable) {
        Readable.Type type = readable.getReadableType();
        switch(type) {
            case ADDRESS:
                this.addresses.add((SimpleAddress) readable);
                break;
            case SPECK:
                this.specks.add((Speck) readable);
                break;
            default:
                Log.e(Constants.LOG_TAG, "WARNING tried to add Readable of unknown Type in HeaderReadingsHashMap ");
        }
        refreshHash();
    }


    public void removeReading(Readable readable) {
        Readable.Type type = readable.getReadableType();
        switch(type) {
            case ADDRESS:
                this.addresses.remove((SimpleAddress) readable);
                break;
            case SPECK:
                this.specks.remove((Speck) readable);
                break;
            default:
                Log.e(Constants.LOG_TAG, "WARNING tried to add Readable of unknown Type in HeaderReadingsHashMap ");
        }
        refreshHash();
    }


    public void updateAddresses() {
        for (SimpleAddress address : this.addresses) {
            address.requestUpdateFeeds(globalHandler);
        }
    }


    public void updateSpecks() {
        for (Speck speck : specks) {
            if (speck.getChannels().size() > 0) {
                // ASSERT all channels in the list of channels are usable readings
                // TODO we use the first channel listed; handle when we do not have all channels as PM25
                globalHandler.httpRequestHandler.requestPrivateChannelReading(globalHandler.settingsHandler.getAccessToken(),speck, speck.getChannels().get(0));
            } else {
                Log.e(Constants.LOG_TAG,"No channels found from speck id="+speck.getFeed_id());
            }
        }
    }


    public void populateSpecks() {
        specks.clear();
        if (globalHandler.settingsHandler.isUserLoggedIn()) {
            Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ArrayList<Feed> feeds = new ArrayList<>();
                    Log.v(Constants.LOG_TAG, "updateSpecks handling response=" + response.toString());
                    JsonParser.populateAllFeedsFromJson(feeds, response);
                    globalHandler.settingsHandler.userFeedsNeedsUpdated = false;

                    for (Feed feed : feeds) {
                        Speck speck = new Speck(feed);
                        specks.add(speck);
                        if (speck.getChannels().size() > 0) {
                            // ASSERT all channels in the list of channels are usable readings
                            // TODO we use the first channel listed; handle when we do not have all channels as PM25
                            globalHandler.httpRequestHandler.requestPrivateChannelReading(globalHandler.settingsHandler.getAccessToken(),speck, speck.getChannels().get(0));
                        } else {
                            Log.e(Constants.LOG_TAG,"No channels found from speck id="+speck.getFeed_id());
                        }
                    }
                    HeaderReadingsHashMap.this.refreshHash();
                }
            };
            globalHandler.httpRequestHandler.requestPrivateFeeds(globalHandler.settingsHandler.getAccessToken(), response);
        }
        refreshHash();
    }


    public void refreshHash() {
        this.hashMap.clear();

        this.hashMap.put(HEADER_TITLES[0], specks);
        if (globalHandler.settingsHandler.appUsesLocation()) {
            final ArrayList<Readable> tempAddresses = new ArrayList<>();
            tempAddresses.add(this.gpsAddress);
            tempAddresses.addAll(this.addresses);
            this.hashMap.put(HEADER_TITLES[1], tempAddresses);
        } else {
            this.hashMap.put(HEADER_TITLES[1], addresses);
        }

        this.populateAdapterList();
        globalHandler.notifyGlobalDataSetChanged();
    }

}
