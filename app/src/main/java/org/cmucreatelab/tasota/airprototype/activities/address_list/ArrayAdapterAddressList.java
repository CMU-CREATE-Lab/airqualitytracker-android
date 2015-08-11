package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.address_show.AddressShowActivity;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
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
        super(context, R.layout.__address_list__address_item, values);
        this.context = context;
        this.values = values;
        setupListView();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = (inflater.inflate(R.layout.__address_list__address_item, parent, false));
        } else {
            rowView = convertView;
        }
        new RowViewAddressList(this.context,values.get(position),rowView).populateRowView();
        return rowView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, AddressShowActivity.class);
        intent.putExtra(Constants.AddressList.ADDRESS_INDEX, i);
        context.startActivity(intent);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        SimpleAddress simpleAddress = context.addresses.get(i);
        context.openDialogDelete(simpleAddress);
        return true;
    }

}
