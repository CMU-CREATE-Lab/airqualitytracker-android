package org.cmucreatelab.tasota.airprototype.activities.address_search;

import android.text.Editable;
import android.text.TextWatcher;
import org.cmucreatelab.tasota.airprototype.classes.timers.AutocompleteTimer;

/**
 * Created by mike on 3/22/16.
 */
public class AddressSearchTextWatcher implements TextWatcher {

    private AddressSearchActivity activity;
    public CharSequence searchText;
    public AutocompleteTimer timer;


    public AddressSearchTextWatcher(AddressSearchActivity activity) {
        this.activity = activity;
        timer = new AutocompleteTimer(activity,200);
    }


    @Override
    public void onTextChanged(final CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (!text.toString().equals("")) {
            this.searchText = text;
            timer.startTimer();
        } else {
            timer.stopTimer();
            // clear results
            activity.listAdapter.clear();
            activity.listAdapter.notifyDataSetChanged();
        }
    }
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    public void afterTextChanged(Editable editable) {}

}
