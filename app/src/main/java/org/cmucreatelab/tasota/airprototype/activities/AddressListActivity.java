package org.cmucreatelab.tasota.airprototype.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.AddressListArrayAdapter;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.HttpRequestHandler;
import org.json.JSONObject;
import java.util.ArrayList;


public class AddressListActivity extends ActionBarActivity {
    ArrayList<Address> addresses;
    AddressListArrayAdapter listAdapter;

    public final static String ADDRESS_INDEX = "org.cmucreatelab.tasota.airprototype.addressindex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        addresses = GlobalHandler.getInstance(this.getApplicationContext()).addresses;

        ListView lv = (ListView)findViewById(R.id.listViewAddresses);
        lv.setLongClickable(true);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switchToMainActivity(i);
                    }
                }
        );
        lv.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("onItemLongClick","DID LONG CLICK");
                        Address address = addresses.get(i);
                        if (address.get_id() < 0) {
                            Log.i("onItemLongClick","WARNING - the long-clicked address has negative id="+address.get_id());
                        } else {
                            showDeleteDialog(address);
                        }
                        return true;
                    }
                }
        );
        listAdapter = new AddressListArrayAdapter(this, addresses);
        lv.setAdapter(listAdapter);
    }


    private void showDeleteDialog(final Address address) {
        final Context ctx = this.getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Remove this Address from your list?");
        builder.setPositiveButton("Erase", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GlobalHandler.getInstance(ctx).removeAddress(address);
                address.destroy(ctx);
                AddressListActivity.this.listAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();
    }


    public void switchToMainActivity(int index) {
        Intent intent = new Intent(this, AddressShowActivity.class);
        intent.putExtra(ADDRESS_INDEX,index);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_new:
                openNew();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void openSettings() {
        // TODO open settings
        Log.i("openSettings", "action bar selected.");
    }


    public void openNew() {
        Log.i("openNew", "action bar selected.");

        final Context ctx = this.getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter a zipcode, city, or address below.");

        final EditText input = new EditText(ctx);
        // TODO test color schemes... ideally dialog boxes should know what colors they are supposed to use.
        input.setTextColor( getResources().getColor(R.color.primary_text_default_material_light) );
        input.setSingleLine(true);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
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
                                // TODO get formatted_address field?
                                // TODO this grabs only the first result but maybe we want to provide options? Doubt it though
                                JSONObject locations = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                                double latd = Double.parseDouble(locations.getString("lat"));
                                double longd = Double.parseDouble(locations.getString("lng"));

                                Address result = Address.createAddressInDatabase(ctx,addressName,latd,longd);
                                GlobalHandler.getInstance(ctx).addAddress(result);

                                listAdapter.notifyDataSetChanged();
                            } else {
                                Log.i("onResponse", "WARNING - Received status code '"+status+"' from GoogleGeocodeAPI; not processing response.");
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

                HttpRequestHandler.getInstance(ctx).requestGoogleGeocode(addressName,response,error);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();
    }
}
