package org.cmucreatelab.tasota.airprototype.activities.address_search;

import android.location.Address;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import java.util.ArrayList;

/**
 * Created by mike on 6/25/15.
 */
public class ArrayAdapterAddressSearch extends ArrayAdapter<Address>
        implements AdapterView.OnItemClickListener {

    private final AddressSearchActivity context;


    private void setupListView() {
        ListView listView = (ListView)context.findViewById(R.id.listViewAddressSearch);
        listView.setAdapter(this);
        listView.setOnItemClickListener(this);
    }


    public ArrayAdapterAddressSearch(AddressSearchActivity context) {
        super(context, R.layout.__address_search__item, new ArrayList<Address>());
        this.context = context;
        setupListView();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout;
        Address address;
        int size,i;

        linearLayout= new LinearLayout(this.context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        address = this.getItem(position);
        if (address.getFeatureName() != null) {
            TextView textView = new TextView(this.context);
            textView.setText(address.getFeatureName());
            textView.setTextSize(24);
            linearLayout.addView(textView);
        }

        size = address.getMaxAddressLineIndex();
        for (i=0;i<size;i++) {
            TextView textView = new TextView(this.context);
            textView.setText(address.getAddressLine(i));
            textView.setTextSize(16);
            linearLayout.addView(textView);
        }

        return linearLayout;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Address address = this.getItem(i);
        context.returnAddress(address);
    }

}
