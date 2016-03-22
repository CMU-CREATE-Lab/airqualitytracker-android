package org.cmucreatelab.tasota.airprototype.helpers.application;

import android.util.Log;
import com.android.volley.Response;

import org.cmucreatelab.tasota.airprototype.activities.options_menu.manage_trackers.ManageTrackersAdapter;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.sticky_grid.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.activities.secret_menu.SecretMenuListFeedsAdapter;
import org.cmucreatelab.tasota.airprototype.classes.readables.Readable;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers.EsdrJsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.system.database.SpeckDbHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mike on 12/22/15.
 */
public abstract class ReadingsHandlerCore {

    protected GlobalHandler globalHandler;
    // using ArrayList ensures an ordered list
    protected final ArrayList<SimpleAddress> addresses = new ArrayList<>();
    protected final ArrayList<Speck> specks = new ArrayList<>();
    // section headers for the table; also should match the the hashmap keys
    protected final ArrayList<String> headers = new ArrayList<>();
    // the hashmap containing addresses, specks, and headers
    protected final HashMap<String,ArrayList> hashMap = new HashMap<>();
    // this is the data structure used by StickyGridAdapter
    public final ArrayList<StickyGridAdapter.LineItem> adapterList = new ArrayList<>();
    // used by tracker manager
    public final ArrayList<ManageTrackersAdapter.TrackerListItem> trackerList = new ArrayList<>();
    public final ArrayList<SecretMenuListFeedsAdapter.ListFeedsItem> debugFeedsList = new ArrayList<>();


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
            SpeckDbHelper.destroy(speck, globalHandler.getAppContext());
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
                            for (Speck speck : ReadingsHandlerCore.this.specks) {
                                if (speck.getDeviceId() == deviceId && speck.get_id() <= 0) {
                                    speck.setName(prettyName);
                                    break;
                                }
                            }
                        }
                        // add all specks into the database which arent in there already
                        for (Speck speck: specks) {
                            if (speck.get_id() <= 0) {
                                SpeckDbHelper.addSpeckToDatabase(globalHandler.getAppContext(),speck);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(Constants.LOG_TAG, "JSON Format error (missing \"data\" or \"rows\" field).");
                    }
                    ReadingsHandlerCore.this.refreshHash();
                }
            };

            feedsResponse = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ArrayList<Speck> specks = new ArrayList<>();
                    Log.v(Constants.LOG_TAG, "updateSpecks handling response=" + response.toString());
                    EsdrJsonParser.populateSpecksFromJson(specks, response);

                    for (Speck speck : specks) {
                        // only add what isnt in the DB already
                        if (findIndexOfSpeckWithDeviceId(speck.getDeviceId()) < 0) {
                            ReadingsHandlerCore.this.specks.add(speck);
                            globalHandler.esdrFeedsHandler.requestUpdate(speck);
                        }
                    }
                    globalHandler.esdrSpecksHandler.requestSpeckDevices(globalHandler.esdrAccount.getAccessToken(), globalHandler.esdrAccount.getUserId(), devicesResponse);
                }
            };

            globalHandler.esdrSpecksHandler.requestSpeckFeeds(globalHandler.esdrAccount.getAccessToken(), globalHandler.esdrAccount.getUserId(), feedsResponse);
        }
        refreshHash();
    }


    public void refreshHash() {
        this.hashMap.clear();
        this.hashMap.put(Constants.HEADER_TITLES[0], specks);
        this.hashMap.put(Constants.HEADER_TITLES[1], addresses);
    }

}
