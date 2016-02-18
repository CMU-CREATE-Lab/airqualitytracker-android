package org.cmucreatelab.tasota.airprototype.activities.secret_menu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.w3c.dom.Text;

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
    private TextView textSecretMenuExpiresAt;


    private void populate() {
        GlobalHandler globalHandler;
        String username, accessToken, refreshToken, deviceIdIgnoreLst, appVersion;
        long userId,expiresAt;

        globalHandler = GlobalHandler.getInstance(getApplicationContext());
        username = globalHandler.esdrAccount.getUsername();
        userId = globalHandler.esdrAccount.getUserId();
        expiresAt = globalHandler.esdrAccount.getExpiresAt();
        accessToken = globalHandler.esdrAccount.getAccessToken();
        refreshToken = globalHandler.esdrAccount.getRefreshToken();
        deviceIdIgnoreLst = globalHandler.settingsHandler.getStringifiedBlacklistedDeviceIds();

        textSecretMenuUsername.setText(username);
        textSecretMenuUserId.setText(String.valueOf(userId));
        textSecretMenuExpiresAt.setText(String.valueOf(expiresAt));
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
        textSecretMenuExpiresAt = (TextView)findViewById(R.id.textSecretMenuExpiresAt);
        listFeedsSecretMenu = (ListView)findViewById(R.id.listFeedsSecretMenu);
        buttonRequestFeedsSecretMenu = (Button)findViewById(R.id.buttonRequestFeedsSecretMenu);

        final GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        globalHandler.readingsHandler.populateAdapterList();
        ListFeedsAdapter adapter = new ListFeedsAdapter(this,globalHandler.readingsHandler.debugFeedsList);
        globalHandler.listFeedsAdapter = adapter;
        listFeedsSecretMenu.setAdapter(adapter);

        populate();

        buttonRequestFeedsSecretMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ListFeedsAdapter.ListFeedsItem> items = globalHandler.readingsHandler.debugFeedsList;
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
