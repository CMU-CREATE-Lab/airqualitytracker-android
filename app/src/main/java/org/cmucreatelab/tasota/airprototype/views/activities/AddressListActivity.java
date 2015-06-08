package org.cmucreatelab.tasota.airprototype.views.activities;

import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import org.cmucreatelab.tasota.airprototype.views.uielements.AlertDialogBuilderAddressListDelete;
import org.cmucreatelab.tasota.airprototype.views.uielements.AlertDialogBuilderAddressNew;
import org.cmucreatelab.tasota.airprototype.views.uielements.ArrayAdapterAddressList;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

import java.util.ArrayList;

public class AddressListActivity extends ActionBarActivity {

    public ArrayList<Address> addresses;
    public Address addressToBeDeleted;
    public EditText editTextCreateNew;
    public ArrayAdapterAddressList listAdapter;
    public final static String ADDRESS_INDEX = "org.cmucreatelab.tasota.airprototype.addressindex";
    public AlertDialog createNew, delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("DEBUG", "executing onCreate.");
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
    }


    private void showDeleteDialog(final Address address) {
        addressToBeDeleted = address;
        AlertDialogBuilderAddressListDelete builder = new AlertDialogBuilderAddressListDelete(this, addressToBeDeleted);
        delete = builder.create();
        delete.show();
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
                openNew();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void openSettings() {
        // TODO open settings
        Log.i("openSettings", "SETTINGS selected.");
    }


    public void openNew() {
        editTextCreateNew = new EditText(this.getApplicationContext());
        AlertDialogBuilderAddressNew builder = new AlertDialogBuilderAddressNew(this,editTextCreateNew);
        createNew = builder.create();
        createNew.show();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("DEBUG","onRestoreInstanceState");
        if (savedInstanceState.getBoolean("dialogDelete")) {
            int index = savedInstanceState.getInt("dialogDeleteAddressIndex");
            showDeleteDialog( addresses.get(index) );
        }
        if (savedInstanceState.getBoolean("dialogCreateNew")) {
            String inputString = savedInstanceState.getString("dialogCreateNewInputString");
            editTextCreateNew = new EditText(this.getApplicationContext());
            createNew = new AlertDialogBuilderAddressNew(this,editTextCreateNew,inputString).create();
            createNew.show();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("DEBUG", "onSaveInstanceState");
        if (delete != null && delete.isShowing()) {
            Log.i("DEBUG", "delete.isShowing");
            outState.putBoolean("dialogDelete", true);
            outState.putInt("dialogDeleteAddressIndex", this.addresses.indexOf(addressToBeDeleted));
            delete.dismiss();
        }
        if (createNew != null && createNew.isShowing()) {
            Log.i("DEBUG", "createNew.isShowing");
            outState.putBoolean("dialogCreateNew",true);
            outState.putString("dialogCreateNewInputString", editTextCreateNew.getText().toString());
            createNew.dismiss();
        }
    }

}
