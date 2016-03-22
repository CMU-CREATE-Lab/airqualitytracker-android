package org.cmucreatelab.tasota.airprototype.activities;

import android.support.v7.app.ActionBarActivity;

/**
 * Created by mike on 3/22/16.
 */
public abstract class BaseActivity<U extends UIElements> extends ActionBarActivity {
    // where UI elements for the activity are stored
    protected U uiElements;
}
