package org.cmucreatelab.tasota.airprototype.activities.address_search;

import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import java.util.List;
import java.util.Locale;

public class AddressSearchActivity extends ActionBarActivity
        implements TextWatcher {

    private ArrayAdapterAddressSearch listAdapter;
    private long searchTextChangedAt; // a way to track what the last string to be searched was (avoid pointers because Java is trash)
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    public void afterTextChanged(Editable editable) {}


    private void populateSearch(CharSequence text) {
        Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        try {
            List<Address> results = geocoder.getFromLocationName(text.toString(), 5);
            this.listAdapter.clear();
            for (Address address : results) {
                this.listAdapter.add(address);
            }
            this.listAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void returnAddress(Address address) {
        double latd,longd;
        String name;
        SimpleAddress result;

        latd = address.getLatitude();
        longd = address.getLongitude();
        if (address.getFeatureName() != null) {
            name = address.getFeatureName();
        } else {
            name = ((EditText)findViewById(R.id.editTextAddressSearch)).getText().toString();
        }

        Log.d(Constants.LOG_TAG,"AddressSearchActivity returning with latd="+latd+", longd="+longd+" using name="+name);
        result = AddressDbHelper.createAddressInDatabase(this, name, latd, longd);
        GlobalHandler globalHandler = GlobalHandler.getInstance(this);
        globalHandler.addressFeedsHashMap.addAddress(result);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText editText;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add a Tracker");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editText = (EditText)findViewById(R.id.editTextAddressSearch);
        editText.addTextChangedListener(this);
        listAdapter = new ArrayAdapterAddressSearch(this);
    }


    @Override
    public void onTextChanged(final CharSequence text, int start, int lengthBefore, int lengthAfter) {
        final long timestamp = System.currentTimeMillis();
        this.searchTextChangedAt = timestamp;
        if (!text.toString().equals("")) {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (timestamp == searchTextChangedAt) {
                        populateSearch(text);
                    }
                }
            };
            handler.postDelayed(runnable, 200);
        } else {
            // clear results
            this.listAdapter.clear();
            this.listAdapter.notifyDataSetChanged();
        }
    }

}
