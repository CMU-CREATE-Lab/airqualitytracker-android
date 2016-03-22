package org.cmucreatelab.tasota.airprototype.activities.secret_menu;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.UIElements;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 3/22/16.
 */
public class SecretMenuUIElements extends UIElements<SecretMenuActivity> {

    private ListView listFeedsSecretMenu;
    private Button buttonRequestFeedsSecretMenu;
    private TextView textSecretMenuUsername;
    private TextView textSecretMenuUserId;
    private TextView textSecretMenuAccessToken;
    private TextView textSecretMenuRefreshToken;
    private TextView textSecretMenuDeviceIdIgnoreList;
    private TextView textSecretMenuAppVersion;
    private TextView textSecretMenuExpiresAt;


    public SecretMenuUIElements(SecretMenuActivity activity) { super(activity); }


    public void populate() {
        final GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
        String username, accessToken, refreshToken, deviceIdIgnoreLst, appVersion;
        long userId,expiresAt;

        textSecretMenuUsername = (TextView)activity.findViewById(R.id.textSecretMenuUsername);
        textSecretMenuUserId = (TextView)activity.findViewById(R.id.textSecretMenuUserId);
        textSecretMenuAccessToken = (TextView)activity.findViewById(R.id.textSecretMenuAccessToken);
        textSecretMenuRefreshToken = (TextView)activity.findViewById(R.id.textSecretMenuRefreshToken);
        textSecretMenuDeviceIdIgnoreList = (TextView)activity.findViewById(R.id.textSecretMenuDeviceIdIgnoreList);
        textSecretMenuAppVersion = (TextView)activity.findViewById(R.id.textSecretMenuAppVersion);
        textSecretMenuExpiresAt = (TextView)activity.findViewById(R.id.textSecretMenuExpiresAt);
        listFeedsSecretMenu = (ListView)activity.findViewById(R.id.listFeedsSecretMenu);
        buttonRequestFeedsSecretMenu = (Button)activity.findViewById(R.id.buttonRequestFeedsSecretMenu);

        globalHandler.readingsHandler.populateAdapterList();
        SecretMenuListFeedsAdapter adapter = new SecretMenuListFeedsAdapter(activity,globalHandler.readingsHandler.debugFeedsList);
        globalHandler.secretMenuListFeedsAdapter = adapter;
        listFeedsSecretMenu.setAdapter(adapter);

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

        buttonRequestFeedsSecretMenu.setOnClickListener(activity.buttonClickerListener);
    }

}
