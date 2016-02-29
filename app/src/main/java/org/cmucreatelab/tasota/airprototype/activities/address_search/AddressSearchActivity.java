package org.cmucreatelab.tasota.airprototype.activities.address_search;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.timers.AutocompleteTimer;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.system.database.AddressDbHelper;

public class AddressSearchActivity extends ActionBarActivity
        implements TextWatcher {

    public ArrayAdapterAddressSearch listAdapter;
    public CharSequence searchText;
    public AutocompleteTimer timer = new AutocompleteTimer(this,200);
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    public void afterTextChanged(Editable editable) {}


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
        ActionBar actionBar;
        EditText editText;

        // init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__address_search__activity);

        // setup action bar
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add a Tracker");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // add listener on text field
        editText = (EditText)findViewById(R.id.editTextAddressSearch);
        editText.addTextChangedListener(this);
        listAdapter = new ArrayAdapterAddressSearch(this);
    }


    @Override
    public void onTextChanged(final CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (!text.toString().equals("")) {
            this.searchText = text;
            timer.startTimer();
        } else {
            timer.stopTimer();
            // clear results
            this.listAdapter.clear();
            this.listAdapter.notifyDataSetChanged();
        }
    }

}
