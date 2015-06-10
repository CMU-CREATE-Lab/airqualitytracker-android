package org.cmucreatelab.tasota.airprototype.views.uielements;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressListActivity;
import org.json.JSONObject;

/**
 * Created by mike on 6/8/15.
 */
public class AlertDialogAddressListNew {

    private EditText editText;
    private AlertDialog alertDialog;
    public EditText getEditText() {
        return editText;
    }
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }


    public AlertDialogAddressListNew(final AddressListActivity activityContext) {
        this(activityContext,"");
    }


    public AlertDialogAddressListNew(final AddressListActivity activityContext, String inputString) {
        this.editText = new EditText(activityContext);
        this.editText.setText(inputString);
        this.alertDialog = (new AlertDialogBuilderAddressListNew(activityContext,this.editText,inputString)).create();
    }


    private class AlertDialogBuilderAddressListNew extends AlertDialog.Builder {
        public AlertDialogBuilderAddressListNew(final AddressListActivity activityContext, final EditText inputField, String inputString) {
            super(activityContext);
            final Context appContext;
            Log.i("openNew", "action bar selected.");
            appContext = activityContext.getApplicationContext();
            this.setMessage("Enter a zipcode, city, or address below.");
            // TODO test color schemes... ideally dialog boxes should know what colors they are supposed to use.
            inputField.setTextColor( activityContext.getResources().getColor(R.color.primary_text_default_material_light) );
            inputField.setSingleLine(true);
            inputField.setText(inputString);
            this.setView(inputField);
            this.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // OUTLINE FOR SEAMLESS INTERFACE FOR WAITING FOR JSON RESPONSE
                    // have a list "newAddressQueue" that holds the status of the json requests made.
                    // Each element in list will have a unique identifier (timestamp?)
                    // On completion from JSON response/google API, remove timestamp from list.
                    // While list is nonempty, display a spinner beneath your list of addresses.
                    final String addressName = inputField.getText().toString();
                    Log.i("onClick", "Adding address=" + addressName);
                    Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                if (status.equals("OK")) {
                                    JSONObject locations;
                                    double latd, longd;
                                    SimpleAddress result;

                                    // TODO get formatted_address field?
                                    // TODO this grabs only the first result but maybe we want to provide options? Doubt it though
                                    locations = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                                    latd = Double.parseDouble(locations.getString("lat"));
                                    longd = Double.parseDouble(locations.getString("lng"));
                                    result = SimpleAddress.createAddressInDatabase(appContext, addressName, latd, longd);
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
                    GlobalHandler.getInstance(appContext).httpRequestHandler.requestGoogleGeocode(addressName, response, error);

                }
            });
            this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // does nothing
                }
            });
        }
    }

}
