package org.cmucreatelab.tasota.airprototype.activities.address_search;

import android.os.Bundle;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.BaseActivity;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.system.database.AddressDbHelper;

public class AddressSearchActivity extends BaseActivity<AddressSearchUIElements> {

    protected AddressSearchArrayAdapter listAdapter;
    protected AddressSearchTextWatcher textWatcher;


    // helper for AutocompleteTimer
    public void clearAdapter() {
        listAdapter.clear();
    }


    // helper for AutocompleteTimer
    public void addAddressToAdapter(SimpleAddress address) {
        listAdapter.add(address);
    }


    // helper for AutocompleteTimer
    public String getTextFromWatcher() {
        return textWatcher.searchText.toString();
    }


    public void returnAddress(SimpleAddress address) {
        // add to database and data structure
        AddressDbHelper.addAddressToDatabase(this,address);
        GlobalHandler globalHandler = GlobalHandler.getInstance(this);
        globalHandler.readingsHandler.addReading(address);
        globalHandler.updateReadings();

        // finish activity
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__address_search__activity);

        textWatcher = new AddressSearchTextWatcher(this);
        uiElements = new AddressSearchUIElements(this);
        uiElements.populate();
        listAdapter = new AddressSearchArrayAdapter(this);
    }

}
