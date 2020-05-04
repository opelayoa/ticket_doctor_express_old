package com.tiendas3b.ticketdoctor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Diagnostic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfa on 14/03/2016.
 */
public class DiagnosticAdapter extends ArrayAdapter<Diagnostic> {

    private final List<Diagnostic> items;
    private final List<Diagnostic> itemsAll;
    private final List<Diagnostic> suggestions;
    private final Context mContext;

    public DiagnosticAdapter(Context context, int resource, ArrayList<Diagnostic> items) {
        super(context, resource, items);
        this.mContext = context;
        this.items = items;
        this.itemsAll = (ArrayList<Diagnostic>) items.clone();
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
        Diagnostic customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.lblStatus);
            if (customerNameLabel != null) {
                customerNameLabel.setText(customer.getDescription());
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
            return ((Diagnostic)(resultValue)).getDescription();
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint == null) {
                return new FilterResults();
            } else {
                suggestions.clear();
                for (Diagnostic user : itemsAll) {
                    String alias = user.getDescription();
                    if(alias != null && alias.toLowerCase().contains(constraint.toString().toLowerCase())){
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
            ArrayList<Diagnostic> filteredList = (ArrayList<Diagnostic>) results.values;
            if(results.count > 0) {
                clear();
                for (Diagnostic c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
