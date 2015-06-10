package org.cmucreatelab.tasota.airprototype.views.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        GlobalHandler globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        addresses = globalHandler.addresses;
        listAdapter = new ArrayAdapterAddressList(this, addresses);
        // for notifying when the dataset changes
        globalHandler.listAdapter = this.listAdapter;
    }


    public void showDeleteDialog(final Address address) {
        dialogDelete = new AlertDialogAddressListDelete(this, address);
        dialogDelete.getAlertDialog().show();
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
