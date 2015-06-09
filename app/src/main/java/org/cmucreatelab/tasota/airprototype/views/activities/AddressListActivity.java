package org.cmucreatelab.tasota.airprototype.views.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.cmucreatelab.tasota.airprototype.views.uielements.AlertDialogAddressListDelete;
import org.cmucreatelab.tasota.airprototype.views.uielements.AlertDialogAddressListNew;
import org.cmucreatelab.tasota.airprototype.views.uielements.ArrayAdapterAddressList;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import java.util.ArrayList;

public class AddressListActivity extends ActionBarActivity {

    public ArrayList<Address> addresses;
    public ArrayAdapterAddressList listAdapter;
    public final static String ADDRESS_INDEX = "org.cmucreatelab.tasota.airprototype.addressindex";
    public AlertDialogAddressListNew dialogNew;
    public AlertDialogAddressListDelete dialogDelete;

    private GoogleApiClient googleApiClient;
    private com.google.android.gms.location.LocationListener locationListener;
    private Location lastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        addresses = GlobalHandler.getInstance(this.getApplicationContext()).addresses;
        listAdapter = new ArrayAdapterAddressList(this, addresses);

        listView = (ListView)findViewById(R.id.listViewAddresses);
        listView.setAdapter(listAdapter);
        listView.setLongClickable(true);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switchToMainActivity(i);
                    }
                }
        );
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("onItemLongClick", "DID LONG CLICK");
                        Address address = addresses.get(i);
                        if (address.get_id() < 0) {
                            Log.i("onItemLongClick", "WARNING - the long-clicked address has negative id=" + address.get_id());
                        } else {
                            showDeleteDialog(address);
                        }
                        return true;
                    }
                }
        );

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        Log.i("DEBUG", "last known location is " + lastLocation.toString());
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                googleApiClient, locationRequest, locationListener);
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                        // TODO handle suspended connection
                        Log.i("DEBUG","onConnectionSuspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        // TODO handle failed connection
                        Log.i("DEBUG","onConnectionFailed");
                    }
                })
                .addApi(LocationServices.API)
                .build();
        if (locationListener == null) {
            locationListener = new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lastLocation = location;
                    Log.i("DEBUG", "LOCATION WAS UPDATED TO " + lastLocation.toString());
                }
            };
        }
        // make sure you actually CONNECT the api client for it to do anything (so much hatred)
        googleApiClient.connect();
    }


    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.googleApiClient, locationListener);
    }


    private void showDeleteDialog(final Address address) {
        dialogDelete = new AlertDialogAddressListDelete(this, address);
        dialogDelete.getAlertDialog().show();
    }


    public void switchToMainActivity(int index) {
        Intent intent = new Intent(this, AddressShowActivity.class);
        intent.putExtra(ADDRESS_INDEX, index);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_new:
                openNew("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void openSettings() {
        // TODO open settings
        Log.i("openSettings", "SETTINGS selected.");
    }


    public void openNew(String inputString) {
        dialogNew = new AlertDialogAddressListNew(this,inputString);
        dialogNew.getAlertDialog().show();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean("dialogDelete")) {
            int index = savedInstanceState.getInt("dialogDeleteAddressIndex");
            showDeleteDialog( addresses.get(index) );
        }
        if (savedInstanceState.getBoolean("dialogNew")) {
            String inputString = savedInstanceState.getString("dialogNewInputString");
            dialogNew = new AlertDialogAddressListNew(this,inputString);
            dialogNew.getAlertDialog().show();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dialogDelete != null && dialogDelete.getAlertDialog().isShowing()) {
            outState.putBoolean("dialogDelete", true);
            outState.putInt("dialogDeleteAddressIndex", this.addresses.indexOf(dialogDelete.getAddressToBeDeleted()));
            dialogDelete.getAlertDialog().dismiss();
        }
        if (dialogNew != null && dialogNew.getAlertDialog().isShowing()) {
            outState.putBoolean("dialogNew",true);
            outState.putString("dialogNewInputString", dialogNew.getEditText().getText().toString());
            dialogNew.getAlertDialog().dismiss();
        }
    }

}
