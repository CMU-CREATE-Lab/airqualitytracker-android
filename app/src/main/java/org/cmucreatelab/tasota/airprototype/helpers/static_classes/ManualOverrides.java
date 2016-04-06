package org.cmucreatelab.tasota.airprototype.helpers.static_classes;

import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import java.util.Date;

/**
 * Created by mike on 4/6/16.
 */
public class ManualOverrides {


    public static void loginEsdr(GlobalHandler globalHandler) {
        long time,userId;
        String username,accessToken,refreshToken;

        time = new Date().getTime() + 1209600; // 2 weeks
        userId = Constants.ManualOverrides.userId;
        username = Constants.ManualOverrides.username;
        accessToken = Constants.ManualOverrides.accessToken;
        refreshToken = Constants.ManualOverrides.refreshToken;

        globalHandler.esdrLoginHandler.updateEsdrAccount(username,userId,accessToken,refreshToken,time);
        globalHandler.esdrLoginHandler.setUserLoggedIn(true);
    }

}
