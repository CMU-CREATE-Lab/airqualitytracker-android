package org.cmucreatelab.tasota.airprototype.activities.readable_list;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.cmucreatelab.tasota.airprototype.activities.AboutAirQualityActivity;
import org.cmucreatelab.tasota.airprototype.activities.AboutSpeckActivity;
import org.cmucreatelab.tasota.airprototype.activities.login.LoginActivity;
import org.cmucreatelab.tasota.airprototype.activities.manage_trackers.ManageTrackersActivity;
import org.cmucreatelab.tasota.airprototype.classes.RefreshTimer;
import org.cmucreatelab.tasota.airprototype.classes.TwoFingerLongPressTimer;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.activities.address_search.AddressSearchActivity;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

public class ReadableListActivity extends ActionBarActivity {

    public DeleteDialogReadableList deleteDialog;
    public DebugDialogReadableList debugDialog;
    public StickyGridFragment stickyGrid;
    private SwipeRefreshLayout swipeRefresh;
    // Keeps track of the activity to know if it was being displayed before the app
    // gets thrown into the background. There is no Event in android to check for
    // when the app returns to the foreground (ios: applicationDidBecomeActive) so
    // we use this instead; only runs through this activity though.
    public boolean activityIsActive = true;
    // refresh every 5 minutes (...with some caveats regarding active activities)
    private RefreshTimer refreshTimer = new RefreshTimer(this, 300000);
    final protected TwoFingerLongPressTimer longPressTimer = new TwoFingerLongPressTimer(this,2000);


    public void openDebugDialog() {
        Log.i(Constants.LOG_TAG, "called openDebugDialog!");
        debugDialog = new DebugDialogReadableList(this);
        debugDialog.getAlertDialog().show();
    }


    public void openDeleteDialog(final StickyGridAdapter.LineItem lineItem) {
        if (lineItem.readable == null) {
            Log.e(Constants.LOG_TAG, "Tried deleting null Reading.");
        } else if (lineItem.readable == GlobalHandler.getInstance(this.getApplicationContext()).headerReadingsHashMap.gpsAddress) {
            Log.w(Constants.LOG_TAG, "Tried deleting hardcoded Address (gpsAddress).");
        } else {
            deleteDialog = new DeleteDialogReadableList(this, lineItem);
            deleteDialog.getAlertDialog().show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__readable_list__activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
        }

        GlobalHandler.getInstance(getApplicationContext()).updateReadings();

        if (savedInstanceState == null) {
            this.stickyGrid = new StickyGridFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.readable_list_refresher, stickyGrid, Constants.StickyGrid.GRID_TAG)
                    .commit();
            swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.readable_list_refresher);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    GlobalHandler.getInstance(getApplicationContext()).updateReadings();
                    swipeRefresh.setRefreshing(false);
                }
            });
        }
    }


    @Override
    protected void onResume() {
        Log.i(Constants.LOG_TAG, "onResume() was called");
        GlobalHandler.getInstance(this.getApplicationContext()).headerReadingsHashMap.refreshHash();
        if (!refreshTimer.isStarted)
            refreshTimer.startTimer();
        super.onResume();
    }


    @Override
    protected void onStop() {
        Log.i(Constants.LOG_TAG, "onStop() was called");
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


    // TODO this might not be used; just added this to be more thorough
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        activityIsActive = false;
        super.startActivityForResult(intent, requestCode, options);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(Constants.LOG_TAG, "ReadableListActivity onRestoreInstanceState");
        if (savedInstanceState.getBoolean("deleteDialog")) {
            int index = savedInstanceState.getInt("deleteDialogAddressIndex");
            openDeleteDialog(GlobalHandler.getInstance(getApplicationContext()).headerReadingsHashMap.adapterList.get(index));
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (deleteDialog != null && deleteDialog.getAlertDialog().isShowing()) {
            outState.putBoolean("deleteDialog", true);
            outState.putInt("deleteDialogAddressIndex", GlobalHandler.getInstance(getApplicationContext()).
                    headerReadingsHashMap.adapterList.indexOf(deleteDialog.getLineItemToBeDeleted()));
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
//            case R.id.action_settings:
//                Log.v(Constants.LOG_TAG, "onOptionsItemSelected: settings selected.");
//                startActivity(new Intent(this, SettingsActivity.class));
//                return true;
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
