package org.cmucreatelab.tasota.airprototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.classes.Address;
import java.util.ArrayList;

/**
 * Created by mike on 6/2/15.
 */
public class AddressListArrayAdapter extends ArrayAdapter<Address> {
    private final Context context;
    private final ArrayList<Address> values;

    public AddressListArrayAdapter(Context context, ArrayList<Address> values) {
        super(context, R.layout.address_item, values);
        this.context = context;
        this.values = values;
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

}
