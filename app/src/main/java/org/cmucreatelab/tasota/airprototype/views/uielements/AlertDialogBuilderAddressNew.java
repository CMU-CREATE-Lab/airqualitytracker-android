package org.cmucreatelab.tasota.airprototype.views.uielements;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressListActivity;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.HttpRequestHandler;
import org.json.JSONObject;

/**
 * Created by mike on 6/5/15.
 */
public class AlertDialogBuilderAddressNew extends AlertDialog.Builder {


    public AlertDialogBuilderAddressNew(final AddressListActivity activityContext) {
        super(activityContext);
        final Context appContext;
        final EditText input;
        Log.i("openNew", "action bar selected.");

        appContext = activityContext.getApplicationContext();
        this.setMessage("Enter a zipcode, city, or address below.");
        input = new EditText(appContext);
        // TODO test color schemes... ideally dialog boxes should know what colors they are supposed to use.
        input.setTextColor( activityContext.getResources().getColor(R.color.primary_text_default_material_light) );
        input.setSingleLine(true);

        this.setView(input);
        this.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // OUTLINE FOR SEAMLESS INTERFACE FOR WAITING FOR JSON RESPONSE
                // have a list "newAddressQueue" that holds the status of the json requests made.
                // Each element in list will have a unique identifier (timestamp?)
                // On completion from JSON response/google API, remove timestamp from list.
                // While list is nonempty, display a spinner beneath your list of addresses.
                final String addressName = input.getText().toString();
                Log.i("onClick", "Adding address=" + addressName);
                Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("OK")) {
                                JSONObject locations;
                                double latd, longd;
                                Address result;

                                // TODO get formatted_address field?
                                // TODO this grabs only the first result but maybe we want to provide options? Doubt it though
                                locations = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                                latd = Double.parseDouble(locations.getString("lat"));
                                longd = Double.parseDouble(locations.getString("lng"));
                                result = Address.createAddressInDatabase(appContext, addressName, latd, longd);
                                GlobalHandler.getInstance(appContext).addAddress(result);

                                activityContext.listAdapter.notifyDataSetChanged();
                            } else {
                                Log.i("onResponse", "WARNING - Received status code '" + status + "' from GoogleGeocodeAPI; not processing response.");
                            }
                        } catch (Exception e) {
                            // TODO catch exception "failed to find JSON attr"
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener error = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO handle errors
                    }
                };
                HttpRequestHandler.getInstance(appContext).requestGoogleGeocode(addressName, response, error);
            }
        });
        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
    }

}
