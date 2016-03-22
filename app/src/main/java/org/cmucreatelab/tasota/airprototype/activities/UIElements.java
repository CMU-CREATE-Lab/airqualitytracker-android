package org.cmucreatelab.tasota.airprototype.activities;

import android.app.Activity;

/**
 * Created by mike on 3/22/16.
 */
public abstract class UIElements<A extends Activity> {

    // the activity to which the UIElements belong to
    protected A activity;

    // default constructor
    public UIElements(A activity) {
        this.activity = activity;
    }

    // usually responsible for instantiating UI Elements and displaying them to the main View
    public abstract void populate();

}
