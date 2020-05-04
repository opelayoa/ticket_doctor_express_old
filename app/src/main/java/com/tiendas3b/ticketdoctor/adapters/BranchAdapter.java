package com.tiendas3b.ticketdoctor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Branch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfa on 14/03/2016.
 */
public class BranchAdapter extends ArrayAdapter<Branch> {

    private final List<Branch> items;
    private final List<Branch> itemsAll;
    private final List<Branch> suggestions;
    private final Context mContext;

    public BranchAdapter(Context context, int resource, ArrayList<Branch> items) {
        super(context, resource, items);
        this.mContext = context;
        this.items = items;
        this.itemsAll = (ArrayList<Branch>) items.clone();
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
        Branch customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.lblStatus);
            if (customerNameLabel != null) {
                customerNameLabel.setText(customer.getNumber3b() + "-" + customer.getName());
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
            Branch branch = ((Branch)(resultValue));
            return branch.getNumber3b() + "-" + branch.getName();
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint == null) {
                return new FilterResults();
            } else {
                suggestions.clear();
                for (Branch branch : itemsAll) {
                    String alias = branch.getNumber3b() + "-" + branch.getName();
                    if(alias != null && alias.toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(branch);
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
            ArrayList<Branch> filteredList = (ArrayList<Branch>) results.values;
            if(results.count > 0) {
                clear();
                for (Branch c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
