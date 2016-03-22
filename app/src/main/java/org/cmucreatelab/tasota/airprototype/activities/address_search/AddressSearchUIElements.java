package org.cmucreatelab.tasota.airprototype.activities.address_search;

import android.support.v7.app.ActionBar;
import android.widget.EditText;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.UIElements;

/**
 * Created by mike on 3/22/16.
 */
public class AddressSearchUIElements extends UIElements<AddressSearchActivity> {


    public AddressSearchUIElements(AddressSearchActivity activity) { super(activity); }


    public void populate() {
        ActionBar actionBar;
        EditText editText;

        // setup action bar
        actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add a Tracker");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // add listener on text field
        editText = (EditText)activity.findViewById(R.id.editTextAddressSearch);
        editText.addTextChangedListener(activity.textWatcher);
    }

}
