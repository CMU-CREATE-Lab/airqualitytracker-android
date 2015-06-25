package org.cmucreatelab.tasota.airprototype.views.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import org.cmucreatelab.tasota.airprototype.views.uielements.AlertDialogAddressListDelete;
import org.cmucreatelab.tasota.airprototype.views.uielements.ArrayAdapterAddressList;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import java.util.ArrayList;

public class AddressListActivity extends ActionBarActivity {

    public ArrayList<SimpleAddress> addresses;
    public ArrayAdapterAddressList listAdapter;
    public AlertDialogAddressListDelete dialogDelete;


    public void openDialogDelete(final SimpleAddress simpleAddress) {
        if (simpleAddress == GlobalHandler.getInstance(this.getApplicationContext()).addressFeedsHashMap.getGpsAddress()) {
            Log.w(Constants.LOG_TAG, "Tried deleting hardcoded Address (gpsAddress).");
        } else {
            dialogDelete = new AlertDialogAddressListDelete(this, simpleAddress);
            dialogDelete.getAlertDialog().show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "AddressListActivity onCreate");
        setContentView(R.layout.activity_address_list);
        GlobalHandler globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        addresses = globalHandler.requestAddressesForDisplay();
        listAdapter = new ArrayAdapterAddressList(this, addresses);
        globalHandler.listAdapter = this.listAdapter;
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(Constants.LOG_TAG, "AddressListActivity onRestoreInstanceState");
        if (savedInstanceState.getBoolean("dialogDelete")) {
            int index = savedInstanceState.getInt("dialogDeleteAddressIndex");
            openDialogDelete(addresses.get(index));
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Log.d(Constants.LOG_TAG, "onOptionsItemSelected: settings selected.");
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                Log.d(Constants.LOG_TAG, "onOptionsItemSelected: REFRESH selected.");
                GlobalHandler.getInstance(this.getApplicationContext()).updateAddresses();
                return true;
            case R.id.action_new:
                Log.d(Constants.LOG_TAG, "onOptionsItemSelected: action bar selected.");
                startActivity(new Intent(this, AddressSearchActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
