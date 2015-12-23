package org.cmucreatelab.tasota.airprototype.helpers.abstract_classes;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.database.SpeckDbHelper;

/**
 * Created by mike on 12/22/15.
 */
public abstract class ReadingsHandlerEditable extends ReadingsHandlerCore {


    public void removeReading(org.cmucreatelab.tasota.airprototype.classes.Readable readable) {
        Readable.Type type = readable.getReadableType();
        switch(type) {
            case ADDRESS:
                this.addresses.remove((SimpleAddress) readable);
                break;
            case SPECK:
                this.specks.remove((Speck) readable);
                break;
            default:
                Log.e(Constants.LOG_TAG, "Tried to remove Readable of unknown Type in HeaderReadingsHashMap ");
        }
        refreshHash();
    }


    public void renameReading(Readable readable, String name) {
        Readable.Type type = readable.getReadableType();
        switch(type) {
            case ADDRESS:
                SimpleAddress address = (SimpleAddress)readable;
                address.setName(name);
                AddressDbHelper.updateAddressInDatabase(globalHandler.getAppContext(), address);
                break;
            case SPECK:
                Speck speck = (Speck)readable;
                speck.setName(name);
                SpeckDbHelper.updateSpeckInDatabase(globalHandler.getAppContext(), speck);
                break;
            default:
                Log.e(Constants.LOG_TAG, "Tried to rename Readable of unknown Type in HeaderReadingsHashMap ");
        }
    }


    // NOTICE: the operation performed via android code does NOT match
    // the operation performed by iOS code. In iOS, we only handle after
    // the user has finished their swapping. In Android, we handle each
    // neighbor's swap (since it had to be implemented from scratch).
    // Regardless, the function logic still works in Java.
    public void reorderReading(Readable reading, Readable destination) {
        if (reading.getReadableType() == destination.getReadableType()) {
            int to,from;
            Readable.Type type = reading.getReadableType();
            switch (type) {
                case ADDRESS:
                    to = addresses.indexOf(destination);
                    addresses.remove(reading);
                    addresses.add(to,(SimpleAddress)reading);
                    break;
                case SPECK:
                    to = specks.indexOf(destination);
                    specks.remove(reading);
                    specks.add(to,(Speck)reading);
                    break;
                default:
                    Log.e(Constants.LOG_TAG, "Tried to reorder Readables of unknown (matching) Type in HeaderReadingsHashMap ");
            }
            globalHandler.positionIdHelper.reorderAddressPositions(addresses);
            globalHandler.positionIdHelper.reorderSpeckPositions(specks);
            // TODO this crashes in the TrackersAdapter code (TrackersAdapter.getItemId) but would be nice (for completeness sake) if it could be called in this method
//            refreshHash();
        }
    }

}
