package org.cmucreatelab.tasota.airprototype.views.uielements;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressListActivity;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

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

    private class AlertDialogBuilderAddressListNew extends AlertDialog.Builder {
        public AlertDialogBuilderAddressListNew(final AddressListActivity activityContext, final EditText inputField, final String inputString) {
            super(activityContext);
            final Context appContext;
            appContext = activityContext.getApplicationContext();
            this.setMessage("Enter a zipcode, city, or address below.");
            // TODO test color schemes... ideally dialog boxes should know what colors they are supposed to use.
            inputField.setTextColor(activityContext.getResources().getColor(R.color.primary_text_default_material_light));
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
                    Log.d(Constants.LOG_TAG, "AlertDialogAddressListNew onClick: Adding address=" + addressName);
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
                                    result = AddressDbHelper.createAddressInDatabase(appContext, addressName, latd, longd);
                                    GlobalHandler.getInstance(appContext).addressFeedsHashMap.addAddress(result);

                                    activityContext.listAdapter.notifyDataSetChanged();
                                } else {
                                    Log.w(Constants.LOG_TAG, "Received status code '" + status + "' from GoogleGeocodeAPI; not processing response.");
                                    // TODO present user with message saying that there was an error
                                    AlertDialog.Builder popupError = new AlertDialog.Builder(activityContext);
                                    popupError.setMessage("Could not parse address '" + addressName + "'.");
                                    popupError.setPositiveButton("OK", null);
                                    popupError.create().show();
                                }
                            } catch (Exception e) {
                                Log.e(Constants.LOG_TAG, "Failed to parse JSON: " + e.getLocalizedMessage());
                            }
                        }
                    };
                    Response.ErrorListener error = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(Constants.LOG_TAG, "Received error from Volley: " + error.getLocalizedMessage());
                        }
                    };
                    GlobalHandler.getInstance(appContext).httpRequestHandler.requestGoogleGeocode(addressName, response, error);

                }
            });
//            this.setNegativeButton("Cancel", null);
            // TODO just a testa
            this.setNegativeButton("Number2", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Geocoder geocoder = new Geocoder(activityContext, Locale.getDefault());

                    try {
                        List<Address> results = geocoder.getFromLocationName(inputField.getText().toString(), 5);
                        for (Address address : results) {
                            Log.d(Constants.LOG_TAG,address.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    public AlertDialogAddressListNew(final AddressListActivity activityContext) {
        this(activityContext,"");
    }


    public AlertDialogAddressListNew(final AddressListActivity activityContext, String inputString) {
        this.editText = new EditText(activityContext);
        this.editText.setText(inputString);
        this.alertDialog = (new AlertDialogBuilderAddressListNew(activityContext,this.editText,inputString)).create();
    }

}
