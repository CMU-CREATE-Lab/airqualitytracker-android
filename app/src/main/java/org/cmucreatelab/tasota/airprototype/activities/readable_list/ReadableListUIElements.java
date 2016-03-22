package org.cmucreatelab.tasota.airprototype.activities.readable_list;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.UIElements;
import org.cmucreatelab.tasota.airprototype.activities.options_menu.login.LoginSessionExpiredDialog;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.sticky_grid.StickyGridFragment;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 3/22/16.
 */
public class ReadableListUIElements extends UIElements<ReadableListActivity> {

    private StickyGridFragment stickyGrid;
    private SwipeRefreshLayout swipeRefresh;


    public ReadableListUIElements(ReadableListActivity activity) { super(activity); }


    public void populate() {
        GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
        }

        // work-around to get alert dialog to show up after check on app launch
        if (globalHandler.displaySessionExpiredDialog) {
            globalHandler.displaySessionExpiredDialog = false;
            LoginSessionExpiredDialog dialog = new LoginSessionExpiredDialog(activity);
            dialog.getAlertDialog().show();
        }

        globalHandler.updateReadings();

        swipeRefresh = (SwipeRefreshLayout)activity.findViewById(R.id.readable_list_refresher);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GlobalHandler.getInstance(activity.getApplicationContext()).updateReadings();
                swipeRefresh.setRefreshing(false);
            }
        });
    }


    protected void constructStickyGrid() {
        this.stickyGrid = new StickyGridFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .add(R.id.readable_list_refresher, stickyGrid, Constants.StickyGrid.GRID_TAG)
                .commit();
    }

}
