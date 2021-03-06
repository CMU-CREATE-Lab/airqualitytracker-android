package org.cmucreatelab.tasota.airprototype.activities.address_search;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import java.util.ArrayList;

/**
 * Created by mike on 6/25/15.
 */
public class AddressSearchArrayAdapter extends ArrayAdapter<SimpleAddress>
        implements AdapterView.OnItemClickListener {

    private final AddressSearchActivity activity;


    private void setupListView() {
        ListView listView = (ListView) activity.findViewById(R.id.listViewAddressSearch);
        listView.setAdapter(this);
        listView.setOnItemClickListener(this);
    }


    public AddressSearchArrayAdapter(AddressSearchActivity activity) {
        super(activity, R.layout.__address_search__item, new ArrayList<SimpleAddress>());
        this.activity = activity;
        setupListView();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout;
        SimpleAddress address;

        linearLayout= new LinearLayout(this.activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        address = this.getItem(position);
        if (address.getName() != null) {
            TextView textView = new TextView(this.activity);
            textView.setText(address.getName());
            textView.setTextSize(24);
            linearLayout.addView(textView);
        }

        return linearLayout;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SimpleAddress address = this.getItem(i);
        activity.returnAddress(address);
    }

}
