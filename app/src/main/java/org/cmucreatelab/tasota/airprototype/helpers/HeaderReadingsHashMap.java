package org.cmucreatelab.tasota.airprototype.helpers;

import android.location.Location;
import android.util.Log;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.activities.manage_trackers.TrackersAdapter;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.activities.secret_menu.ListFeedsAdapter;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.JsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.SpeckDbHelper;
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
    // used by tracker manager
    public final ArrayList<TrackersAdapter.TrackerListItem> trackerList = new ArrayList<>();
    public final ArrayList<ListFeedsAdapter.ListFeedsItem> debugFeedsList = new ArrayList<>();


    public HeaderReadingsHashMap(GlobalHandler globalHandler) {
        this.gpsAddress = new SimpleAddress("Loading Current Location...", "", 0.0, 0.0, true);
        this.globalHandler = globalHandler;
        // headers
        Collections.addAll(headers, Constants.HEADER_TITLES);
        // addresses from database
        ArrayList<SimpleAddress> dbAddresses = AddressDbHelper.fetchAddressesFromDatabase(this.globalHandler.appContext);
        addresses.addAll(dbAddresses);
        // specks from login info
        ArrayList<Speck> dbSpecks = SpeckDbHelper.fetchSpecksFromDatabase(this.globalHandler.appContext);
        for (Speck speck : dbSpecks) {
            addReading(speck);
            globalHandler.esdrSpecksHandler.requestChannelsForSpeck(speck);
        }
        populateSpecks();
    }


    // helper function to determine if a speck with deviceId exists in specks list
    // returns -1 if speck doesn't exist, returns index otherwise
    private int findIndexOfSpeckWithDeviceId(long deviceId) {
        int index=0;
        for (Speck speck: specks) {
            if (speck.getDeviceId() == deviceId) {
                return index;
            }
            index++;
        }
        return -1;
    }


    public void populateAdapterList() {
        // sectionFirstPosition marks the position of the header in each section
        int headerCount=0,sectionFirstPosition,position=0;
        adapterList.clear();
        trackerList.clear();
        debugFeedsList.clear();

        // adapterList
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
        // trackerList
        for (String header : headers) {
            ArrayList<Readable> items = (ArrayList<Readable>)hashMap.get(header);
            // only display headers with non-empty contents
            if (items.size() > 0) {
                trackerList.add( new TrackersAdapter.TrackerListItem(header) );

                // grid cells
                for (Readable r : items) {
                    if (r.getReadableType() == Readable.Type.ADDRESS && ((SimpleAddress)r).isCurrentLocation()) {
                        continue;
                    }
                    trackerList.add( new TrackersAdapter.TrackerListItem(r) );
                }
            }
        }
        // debugFeedsList
        // TODO this doesn't get updated enough (doesn't list all the feeds until you refresh)
        int index=0;
        if (globalHandler.settingsHandler.appUsesLocation()) {
            debugFeedsList.add( new ListFeedsAdapter.ListFeedsItem(gpsAddress, index++) );
            for (Feed feed : gpsAddress.feeds) {
                debugFeedsList.add( new ListFeedsAdapter.ListFeedsItem(gpsAddress, feed) );
            }
        }
        for (SimpleAddress address : addresses) {
            debugFeedsList.add( new ListFeedsAdapter.ListFeedsItem(address, index++) );
            for (Feed feed : address.feeds) {
                debugFeedsList.add( new ListFeedsAdapter.ListFeedsItem(address, feed) );
            }
        }
    }


    public void setGpsAddressLocation(Location location) {
        gpsAddress.setLatitude(location.getLatitude());
        gpsAddress.setLongitude(location.getLongitude());
        globalHandler.esdrFeedsHandler.requestUpdateFeeds(gpsAddress);
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


    public void renameReading(Readable readable, String name) {
        Readable.Type type = readable.getReadableType();
        switch(type) {
            case ADDRESS:
                SimpleAddress address = (SimpleAddress)readable;
                address.setName(name);
                AddressDbHelper.updateAddressInDatabase(globalHandler.appContext,address);
                break;
            case SPECK:
                Speck speck = (Speck)readable;
                speck.setName(name);
                SpeckDbHelper.updateSpeckInDatabase(globalHandler.appContext, speck);
                break;
            default:
                Log.e(Constants.LOG_TAG, "Tried to rename Readable of unknown Type in HeaderReadingsHashMap ");
        }
    }


    // NOTICE: the operation performed via android code does NOT match
    // the operation performed by iOS code. In iOS, we only handle after
    // the user has finished their swapping. In Android, we handle each
    // neighbor's swap (since it had to be implemented from scratch).
    // Regardless, the function logic still works in Java.
    public void reorderReading(Readable reading, Readable destination) {
        if (reading.getReadableType() == destination.getReadableType()) {
            int to,from;
            Readable.Type type = reading.getReadableType();
            switch (type) {
                case ADDRESS:
                    to = addresses.indexOf(destination);
                    addresses.remove(reading);
                    addresses.add(to,(SimpleAddress)reading);
                    break;
                case SPECK:
                    to = specks.indexOf(destination);
                    specks.remove(reading);
                    specks.add(to,(Speck)reading);
                    break;
                default:
                    Log.e(Constants.LOG_TAG, "Tried to reorder Readables of unknown (matching) Type in HeaderReadingsHashMap ");
            }
            globalHandler.positionIdHelper.reorderAddressPositions(addresses);
            globalHandler.positionIdHelper.reorderSpeckPositions(specks);
            // TODO this crashes in the TrackersAdapter code (TrackersAdapter.getItemId) but would be nice (for completeness sake) if it could be called in this method
//            refreshHash();
        }
    }


    public void updateAddresses() {
        for (SimpleAddress address : this.addresses) {
            globalHandler.esdrFeedsHandler.requestUpdateFeeds(address);
        }
    }


    public void updateSpecks() {
        for (Speck speck : specks) {
            globalHandler.esdrFeedsHandler.requestUpdate(speck);
        }
    }


    public void clearSpecks() {
        // TODO make this clear the table instead of iterating?
        for (Speck speck : specks) {
            SpeckDbHelper.destroy(speck, globalHandler.appContext);
        }
        specks.clear();
        refreshHash();
    }


    public void populateSpecks() {
//        specks.clear();
        if (globalHandler.esdrLoginHandler.isUserLoggedIn()) {
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
                                if (speck.getDeviceId() == deviceId && speck.get_id() <= 0) {
                                    speck.setName(prettyName);
                                    break;
                                }
                            }
                        }
                        // add all specks into the database which arent in there already
                        for (Speck speck: specks) {
                            if (speck.get_id() <= 0) {
                                SpeckDbHelper.addSpeckToDatabase(globalHandler.appContext,speck);
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
                        // only add what isnt in the DB already
                        if (findIndexOfSpeckWithDeviceId(speck.getDeviceId()) < 0) {
                            HeaderReadingsHashMap.this.specks.add(speck);
                            globalHandler.esdrFeedsHandler.requestUpdate(speck);
                        }
                    }
                    globalHandler.esdrSpecksHandler.requestSpeckDevices(globalHandler.esdrLoginHandler.getAccessToken(), globalHandler.esdrLoginHandler.getUserId(), devicesResponse);
                }
            };

            globalHandler.esdrSpecksHandler.requestSpeckFeeds(globalHandler.esdrLoginHandler.getAccessToken(), globalHandler.esdrLoginHandler.getUserId(), feedsResponse);
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
