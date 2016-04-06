package org.cmucreatelab.tasota.airprototype.activities.readable_list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.cmucreatelab.tasota.airprototype.activities.BaseActivity;
import org.cmucreatelab.tasota.airprototype.activities.options_menu.about_air_quality.AboutAirQualityActivity;
import org.cmucreatelab.tasota.airprototype.activities.options_menu.about_speck.AboutSpeckActivity;
import org.cmucreatelab.tasota.airprototype.activities.options_menu.login.LoginActivity;
import org.cmucreatelab.tasota.airprototype.activities.options_menu.manage_trackers.ManageTrackersActivity;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.sticky_grid.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.classes.timers.RefreshTimer;
import org.cmucreatelab.tasota.airprototype.classes.timers.TwoFingerLongPressTimer;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.activities.address_search.AddressSearchActivity;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.ManualOverrides;

public class ReadableListActivity extends BaseActivity<ReadableListUIElements> {

    public ReadableListDeleteDialog deleteDialog;
    public ReadableListDebugDialog debugDialog;
    // Keeps track of the activity to know if it was being displayed before the app
    // gets thrown into the background. There is no Event in android to check for
    // when the app returns to the foreground (ios: applicationDidBecomeActive) so
    // we use this instead; only runs through this activity though.
    public boolean activityIsActive = true;
    // refresh every 5 minutes (...with some caveats regarding active activities)
    private RefreshTimer refreshTimer = new RefreshTimer(this, 300000);
    final public TwoFingerLongPressTimer longPressTimer = new TwoFingerLongPressTimer(this,2000);


    public void openDebugDialog() {
        Log.i(Constants.LOG_TAG, "called openDebugDialog!");
        debugDialog = new ReadableListDebugDialog(this);
        debugDialog.getAlertDialog().show();
    }


    public void openDeleteDialog(final StickyGridAdapter.LineItem lineItem) {
        if (lineItem.readable == null) {
            Log.e(Constants.LOG_TAG, "Tried deleting null Reading.");
        } else if (lineItem.readable == GlobalHandler.getInstance(this.getApplicationContext()).readingsHandler.gpsReadingHandler.gpsAddress) {
            Log.w(Constants.LOG_TAG, "Tried deleting hardcoded Address (gpsAddress).");
        } else {
            deleteDialog = new ReadableListDeleteDialog(this, lineItem);
            deleteDialog.getAlertDialog().show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_list__activity);

        uiElements = new ReadableListUIElements(this);
        uiElements.populate();
        if (savedInstanceState == null) {
            uiElements.constructStickyGrid();
        }

        // manually sets login info for ESDR
        if (Constants.ManualOverrides.MANUAL_ESDR_LOGIN) {
            ManualOverrides.loginEsdr(GlobalHandler.getInstance(getApplicationContext()));
        }
    }


    @Override
    protected void onResume() {
        GlobalHandler.getInstance(this.getApplicationContext()).readingsHandler.refreshHash();
        if (!refreshTimer.isStarted)
            refreshTimer.startTimer();
        super.onResume();
    }


    @Override
    protected void onStop() {
        if (activityIsActive)
            refreshTimer.stopTimer();
        super.onStop();
    }


    @Override
    protected void onRestart() {
        if (activityIsActive) {
            Log.i(Constants.LOG_TAG,"onRestart() was called and activityIsActive! running updateReadings()");
            GlobalHandler.getInstance(this).updateReadings();
        }
        activityIsActive = true;
        super.onRestart();
    }


    @Override
    public void startActivity(Intent intent) {
        activityIsActive = false;
        super.startActivity(intent);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(Constants.LOG_TAG, "ReadableListActivity onRestoreInstanceState");
        if (savedInstanceState.getBoolean("deleteDialog")) {
            int index = savedInstanceState.getInt("deleteDialogAddressIndex");
            openDeleteDialog(GlobalHandler.getInstance(getApplicationContext()).readingsHandler.adapterList.get(index));
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (deleteDialog != null && deleteDialog.getAlertDialog().isShowing()) {
            outState.putBoolean("deleteDialog", true);
            outState.putInt("deleteDialogAddressIndex", GlobalHandler.getInstance(getApplicationContext()).
                    readingsHandler.adapterList.indexOf(deleteDialog.getLineItemToBeDeleted()));
            deleteDialog.getAlertDialog().dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_login:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: LOGIN selected.");
                startActivity(new Intent(this, LoginActivity.class));
                return true;
//            case R.id.action_refresh:
//                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: REFRESH selected.");
//                GlobalHandler.getInstance(this.getApplicationContext()).updateReadings();
//                return true;
            case R.id.action_new:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: action bar selected.");
                startActivity(new Intent(this, AddressSearchActivity.class));
                return true;
            case R.id.action_about_airquality:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: about selected.");
                startActivity(new Intent(this, AboutAirQualityActivity.class));
                return true;
            case R.id.action_about_speck:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: about speck selected.");
                startActivity(new Intent(this, AboutSpeckActivity.class));
                return true;
            case R.id.action_trackers:
                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: edit trackers selected.");
                startActivity(new Intent(this, ManageTrackersActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
