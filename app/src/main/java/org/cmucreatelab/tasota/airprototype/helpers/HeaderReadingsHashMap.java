package org.cmucreatelab.tasota.airprototype.helpers;

import android.location.Location;
import android.preference.PreferenceActivity;
import android.util.Log;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.JsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by mike on 8/14/15.
 */
public class HeaderReadingsHashMap {

    private GlobalHandler globalHandler;
    public SimpleAddress gpsAddress;
    // using ArrayList ensures an ordered list
    private final ArrayList<SimpleAddress> addresses = new ArrayList<>();
    private final ArrayList<Speck> specks = new ArrayList<>();
    // section headers for the table; also should match the the hashmap keys
    private final ArrayList<String> headers = new ArrayList<>();
    // the hashmap containing addresses, specks, and headers
    private final HashMap<String,ArrayList> hashMap = new HashMap<>();
    // this is the data structure used by StickyGridAdapter
    public final ArrayList<StickyGridAdapter.LineItem> adapterList = new ArrayList<>();


    public HeaderReadingsHashMap(GlobalHandler globalHandler) {
        this.gpsAddress = new SimpleAddress("Loading Current Location...", "", 0.0, 0.0, true);
        this.globalHandler = globalHandler;
        // headers
        Collections.addAll(headers, Constants.HEADER_TITLES);
        // addresses from database
        ArrayList<SimpleAddress> dbAddresses = AddressDbHelper.fetchAddressesFromDatabase(this.globalHandler.appContext);
        addresses.addAll(dbAddresses);
        // specks from login info
        populateSpecks();
    }
    
    
    public void populateAdapterList() {
        // sectionFirstPosition marks the position of the header in each section
        int headerCount=0,sectionFirstPosition,position=0;
        adapterList.clear();

        for (String header : headers) {
            ArrayList<Readable> items = (ArrayList<Readable>)hashMap.get(header);
            // only display headers with non-empty contents
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
                Log.e(Constants.LOG_TAG, "Tried to add Readable of unknown Type in HeaderReadingsHashMap ");
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
                Log.e(Constants.LOG_TAG, "Tried to remove Readable of unknown Type in HeaderReadingsHashMap ");
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
            speck.requestUpdate(globalHandler);
        }
    }


    public void populateSpecks() {
        specks.clear();
        if (globalHandler.settingsHandler.isUserLoggedIn()) {
            final Response.Listener<JSONObject> feedsResponse,devicesResponse;

            // bad variable ordering is brought to you by: Java
            devicesResponse = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonFeeds = response.getJSONObject("data").getJSONArray("rows");
                        int size = jsonFeeds.length();
                        for (int i = 0; i < size; i++) {
                            JSONObject jsonFeed = (JSONObject) jsonFeeds.get(i);
                            int deviceId = jsonFeed.getInt("id");
                            String prettyName = jsonFeed.getString("name");
                            for (Speck speck : HeaderReadingsHashMap.this.specks) {
                                if (speck.getDeviceId() == deviceId) {
                                    speck.setName(prettyName);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(Constants.LOG_TAG, "JSON Format error (missing \"data\" or \"rows\" field).");
                    }
                    HeaderReadingsHashMap.this.refreshHash();
                }
            };

            feedsResponse = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ArrayList<Speck> specks = new ArrayList<>();
                    Log.v(Constants.LOG_TAG, "updateSpecks handling response=" + response.toString());
                    JsonParser.populateSpecksFromJson(specks, response);
                    // TODO not sure if needed anymore?
                    globalHandler.settingsHandler.userFeedsNeedsUpdated = false;

                    for (Speck speck : specks) {
                        HeaderReadingsHashMap.this.specks.add(speck);
                        speck.requestUpdate(globalHandler);
                    }
                    globalHandler.httpRequestHandler.requestSpeckDevices(globalHandler.settingsHandler.getAccessToken(), globalHandler.settingsHandler.getUserId(), devicesResponse);
                }
            };

            globalHandler.httpRequestHandler.requestSpeckFeeds(globalHandler.settingsHandler.getAccessToken(), globalHandler.settingsHandler.getUserId(), feedsResponse);
        }
        refreshHash();
    }


    public void refreshHash() {
        this.hashMap.clear();
        this.hashMap.put(Constants.HEADER_TITLES[0], specks);
        if (globalHandler.settingsHandler.appUsesLocation()) {
            final ArrayList<Readable> tempAddresses = new ArrayList<>();
            tempAddresses.add(this.gpsAddress);
            tempAddresses.addAll(this.addresses);
            this.hashMap.put(Constants.HEADER_TITLES[1], tempAddresses);
        } else {
            this.hashMap.put(Constants.HEADER_TITLES[1], addresses);
        }
        this.populateAdapterList();
        globalHandler.notifyGlobalDataSetChanged();
    }

}
