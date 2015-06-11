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
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressListActivity;
import org.cmucreatelab.tasota.airprototype.views.activities.AddressShowActivity;
import java.util.ArrayList;

/**
 * Created by mike on 6/2/15.
 */
public class ArrayAdapterAddressList extends ArrayAdapter<SimpleAddress>
        implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private final AddressListActivity context;
    private final ArrayList<SimpleAddress> values;


    private void setupListView() {
        ListView listView = (ListView)context.findViewById(R.id.listViewAddresses);
        listView.setAdapter(this);
        listView.setLongClickable(true);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }


    public ArrayAdapterAddressList(AddressListActivity context, ArrayList<SimpleAddress> values) {
        super(context, R.layout.address_item, values);
        this.context = context;
        this.values = values;
        setupListView();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        View rowView;
        TextView textView;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.address_item, parent, false);
        } else {
            rowView = convertView;
        }

        // set address name
        textView = (TextView)rowView.findViewById(R.id.textAddressName);
        textView.setText(values.get(position).getName());

        // address's value
        // TODO address._id for now, but should be air quality.
        textView = (TextView)rowView.findViewById(R.id.textAddressValue);
        if (values.get(position).getClosestFeed() == null) {
            textView.setText("id="+String.valueOf(values.get(position).get_id()));
        } else {
            textView.setText("feed="+String.valueOf(values.get(position).getClosestFeed().getFeed_id()));
        }

        return rowView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, AddressShowActivity.class);
        intent.putExtra(AddressListActivity.ADDRESS_INDEX, i);
        context.startActivity(intent);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("onItemLongClick", "DID LONG CLICK");
        SimpleAddress simpleAddress = context.addresses.get(i);
        if (simpleAddress.get_id() < 0) {
            Log.i("onItemLongClick", "WARNING - the long-clicked address has negative id=" + simpleAddress.get_id());
        } else {
            context.showDialogDelete(simpleAddress);
        }
        return true;
    }

}
