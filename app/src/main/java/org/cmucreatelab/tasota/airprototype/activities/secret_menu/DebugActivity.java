package org.cmucreatelab.tasota.airprototype.activities.secret_menu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

import java.util.ArrayList;

public class DebugActivity extends ActionBarActivity {

    private ListView listFeedsSecretMenu;
    private Button buttonRequestFeedsSecretMenu;
    private TextView textSecretMenuUsername;
    private TextView textSecretMenuUserId;
    private TextView textSecretMenuAccessToken;
    private TextView textSecretMenuRefreshToken;
    private TextView textSecretMenuDeviceIdIgnoreList;
    private TextView textSecretMenuAppVersion;


    private void populate() {
        GlobalHandler globalHandler;
        String username, accessToken, refreshToken, deviceIdIgnoreLst, appVersion;

        globalHandler = GlobalHandler.getInstance(getApplicationContext());
        username = globalHandler.settingsHandler.getUsername();
        long userid = globalHandler.settingsHandler.getUserId();
        accessToken = globalHandler.settingsHandler.getAccessToken();
        refreshToken = globalHandler.settingsHandler.getRefreshToken();
        deviceIdIgnoreLst = globalHandler.settingsHandler.getStringifiedBlacklistedDeviceIds();

        textSecretMenuUsername.setText(username);
        textSecretMenuUserId.setText(String.valueOf(userid));
        textSecretMenuAccessToken.setText(accessToken);
        textSecretMenuRefreshToken.setText(refreshToken);
        textSecretMenuDeviceIdIgnoreList.setText(deviceIdIgnoreLst);
        textSecretMenuAppVersion.setText(Constants.APP_VERSION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__secret_menu__debug_activity);

        textSecretMenuUsername = (TextView)findViewById(R.id.textSecretMenuUsername);
        textSecretMenuUserId = (TextView)findViewById(R.id.textSecretMenuUserId);
        textSecretMenuAccessToken = (TextView)findViewById(R.id.textSecretMenuAccessToken);
        textSecretMenuRefreshToken = (TextView)findViewById(R.id.textSecretMenuRefreshToken);
        textSecretMenuDeviceIdIgnoreList = (TextView)findViewById(R.id.textSecretMenuDeviceIdIgnoreList);
        textSecretMenuAppVersion = (TextView)findViewById(R.id.textSecretMenuAppVersion);
        listFeedsSecretMenu = (ListView)findViewById(R.id.listFeedsSecretMenu);
        buttonRequestFeedsSecretMenu = (Button)findViewById(R.id.buttonRequestFeedsSecretMenu);

        final GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        globalHandler.headerReadingsHashMap.populateAdapterList();
        ListFeedsAdapter adapter = new ListFeedsAdapter(this,globalHandler.headerReadingsHashMap.debugFeedsList);
        globalHandler.listFeedsAdapter = adapter;
        listFeedsSecretMenu.setAdapter(adapter);

        populate();

        buttonRequestFeedsSecretMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ListFeedsAdapter.ListFeedsItem> items = globalHandler.headerReadingsHashMap.debugFeedsList;
                for (ListFeedsAdapter.ListFeedsItem item : items) {
                    if (!item.isHeader) {
                        Feed feed = item.feed;
                        globalHandler.esdrFeedsHandler.requestChannelReading(feed, feed.getChannels().get(0));
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debug, menu);
        return true;
    }

}
