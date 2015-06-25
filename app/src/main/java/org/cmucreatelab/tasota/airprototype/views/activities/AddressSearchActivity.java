package org.cmucreatelab.tasota.airprototype.views.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.views.uielements.ArrayAdapterAddressSearch;
import java.util.List;
import java.util.Locale;

public class AddressSearchActivity extends ActionBarActivity
        implements TextWatcher {

    private ArrayAdapterAddressSearch listAdapter;
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    public void afterTextChanged(Editable editable) {}


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

        editText = (EditText)findViewById(R.id.editTextAddressSearch);
        editText.addTextChangedListener(this);
        listAdapter = new ArrayAdapterAddressSearch(this);
    }


    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (!text.toString().equals("")) {
            // TODO this really needs to be asynchronous (it bogs down the app otherwise)
            Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            try {
                List<Address> results = geocoder.getFromLocationName(text.toString(), 5);
                this.listAdapter.clear();
                for (Address address : results) {
                    Log.d(Constants.LOG_TAG, address.toString());
                    this.listAdapter.add(address);
                }
                this.listAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(Constants.LOG_TAG,"size of adapter is" +this.listAdapter.getCount());
        } else {
            // clear results
            this.listAdapter.clear();
            this.listAdapter.notifyDataSetChanged();
        }
    }

}
