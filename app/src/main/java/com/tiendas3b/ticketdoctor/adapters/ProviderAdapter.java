package com.tiendas3b.ticketdoctor.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfa on 14/03/2016.
 */
public class ProviderAdapter extends ArrayAdapter<Provider> {

    private final List<Provider> items;
    private final List<Provider> itemsAll;
    private final List<Provider> suggestions;
    private final Activity mContext;

    public ProviderAdapter(Activity context, int resource, ArrayList<Provider> items) {
        super(context, resource, items);
        this.mContext = context;
        this.items = items;
        this.itemsAll = (ArrayList<Provider>) items.clone();
        this.suggestions = new ArrayList<>();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_ticket_status, null);
        }
        Provider user = items.get(position);
        if (user != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.lblStatus);
            if (customerNameLabel != null) {
                customerNameLabel.setText(user.getBusinessName());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            Provider user = ((Provider)(resultValue));
            String str = user.getBusinessName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint == null) {
                return new FilterResults();
            } else {
                suggestions.clear();
                for (Provider user : itemsAll) {
                    String alias = user.getBusinessName();
                    if(/*alias != null && */alias.toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(user);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Provider> filteredList = (ArrayList<Provider>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Provider c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
