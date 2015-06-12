package org.cmucreatelab.tasota.airprototype.views.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import org.cmucreatelab.tasota.airprototype.views.uielements.AlertDialogAddressListDelete;
import org.cmucreatelab.tasota.airprototype.views.uielements.AlertDialogAddressListNew;
import org.cmucreatelab.tasota.airprototype.views.uielements.ArrayAdapterAddressList;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import java.util.ArrayList;

public class AddressListActivity extends ActionBarActivity {

    public ArrayList<SimpleAddress> addresses;
    public ArrayAdapterAddressList listAdapter;
    public final static String ADDRESS_INDEX = "org.cmucreatelab.tasota.airprototype.addressindex";
    public AlertDialogAddressListNew dialogNew;
    public AlertDialogAddressListDelete dialogDelete;


    public void openDialogDelete(final SimpleAddress simpleAddress) {
        if (simpleAddress == GlobalHandler.getInstance(this.getApplicationContext()).getGpsAddress()) {
            Log.w(Constants.LOG_TAG, "Tried deleting hardcoded Address (gpsAddress).");
        } else {
            dialogDelete = new AlertDialogAddressListDelete(this, simpleAddress);
            dialogDelete.getAlertDialog().show();
        }
    }


    public void openSettings() {
        // TODO open settings
    }


    public void openDialogNew(String inputString) {
        dialogNew = new AlertDialogAddressListNew(this,inputString);
        dialogNew.getAlertDialog().show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "AddressListActivity onCreate");
        setContentView(R.layout.activity_address_list);
        GlobalHandler globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        addresses = globalHandler.getAddresses();
        listAdapter = new ArrayAdapterAddressList(this, addresses);
        // for notifying when the dataset changes
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
                Log.d(Constants.LOG_TAG, "onOptionsItemSelected: settings selected.");
                openSettings();
                return true;
            case R.id.action_new:
                Log.d(Constants.LOG_TAG, "onOptionsItemSelected: action bar selected.");
                openDialogNew("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
