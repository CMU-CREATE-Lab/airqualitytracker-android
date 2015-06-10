package org.cmucreatelab.tasota.airprototype.views.uielements;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressListActivity;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressShowActivity;

import java.util.ArrayList;

/**
 * Created by mike on 6/2/15.
 */
public class ArrayAdapterAddressList extends ArrayAdapter<Address> {
    private final Context context;
    private final ArrayList<Address> values;
    private ListView listView;


    public ArrayAdapterAddressList(AddressListActivity context, ArrayList<Address> values) {
        super(context, R.layout.address_item, values);
        this.context = context;
        this.values = values;
        setupListView(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        View rowView;
        TextView textView;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.address_item, parent, false);

        // set address name
        textView = (TextView)rowView.findViewById(R.id.textAddressName);
        textView.setText(values.get(position).getName());

        // address's value
        // TODO address._id for now, but should be air quality.
        textView = (TextView)rowView.findViewById(R.id.textAddressValue);
        textView.setText(String.valueOf(values.get(position).get_id()));

        return rowView;
    }


    private void setupListView(final AddressListActivity context) {
        listView = (ListView)context.findViewById(R.id.listViewAddresses);
        listView.setAdapter(this);
        listView.setLongClickable(true);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(context, AddressShowActivity.class);
                        intent.putExtra(context.ADDRESS_INDEX, i);
                        context.startActivity(intent);
                    }
                }
        );
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("onItemLongClick", "DID LONG CLICK");
                        Address address = context.addresses.get(i);
                        if (address.get_id() < 0) {
                            Log.i("onItemLongClick", "WARNING - the long-clicked address has negative id=" + address.get_id());
                        } else {
                            context.showDeleteDialog(address);
                        }
                        return true;
                    }
                }
        );
    }

}
