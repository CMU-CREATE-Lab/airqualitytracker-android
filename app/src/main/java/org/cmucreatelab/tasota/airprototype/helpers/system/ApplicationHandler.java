package org.cmucreatelab.tasota.airprototype.helpers.system;

import android.app.Application;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.Date;

/**
 * Created by mike on 2/18/16.
 */
public class ApplicationHandler extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(Constants.LOG_TAG, "==== Application Launches! ====");
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        if (globalHandler.esdrAuthHandler.alertLogout()) {
            globalHandler.esdrLoginHandler.updateEsdrTokens("","",0);
            globalHandler.servicesHandler.stopEsdrRefreshService();
        }
    }

}
