package org.cmucreatelab.tasota.airprototype.activities.secret_menu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

public class DebugActivity extends ActionBarActivity {

    private ListView listFeedsSecretMenu;
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
        // TODO app version
        textSecretMenuAppVersion.setText("0.aa");
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

        populate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debug, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
