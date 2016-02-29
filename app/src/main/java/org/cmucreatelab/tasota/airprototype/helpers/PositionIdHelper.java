package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.SharedPreferences;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.SpeckDbHelper;
import java.util.ArrayList;

/**
 * Created by mike on 12/21/15.
 */
public class PositionIdHelper {

    protected GlobalHandler globalHandler;
    private SharedPreferences sharedPreferences;


    // GlobalHandler accesses the constructor
    protected PositionIdHelper(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = globalHandler.settingsHandler.getSharedPreferences();
    }


    private void setAddressLastPosition(int position) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(Constants.SettingsKeys.addressLastPosition, position);
        editor.apply();
    }


    private void setSpeckLastPosition(int position) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(Constants.SettingsKeys.speckLastPosition, position);
        editor.apply();
    }


    public int getAddressLastPosition() {
        int position = this.sharedPreferences.getInt(Constants.SettingsKeys.addressLastPosition, (int) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.addressLastPosition));
        setAddressLastPosition(position + 1);
        return position;
    }


    public int getSpeckLastPosition() {
        int position = this.sharedPreferences.getInt(Constants.SettingsKeys.speckLastPosition, (int) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.speckLastPosition));
        setSpeckLastPosition(position + 1);
        return position;
    }


    public void reorderAddressPositions(ArrayList<SimpleAddress> addresses) {
        int index = 1;
        for (SimpleAddress address: addresses) {
            address.setPositionId(index);
            AddressDbHelper.updateAddressInDatabase(globalHandler.appContext, address);
            index ++;
        }
        setAddressLastPosition(index);
    }


    public void reorderSpeckPositions(ArrayList<Speck> specks) {
        int index = 1;
        for (Speck speck: specks) {
            speck.setPositionId(index);
            SpeckDbHelper.updateSpeckInDatabase(globalHandler.appContext, speck);
            index ++;
        }
        setSpeckLastPosition(index);
    }

}
