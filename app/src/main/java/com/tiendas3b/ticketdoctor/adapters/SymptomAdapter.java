package com.tiendas3b.ticketdoctor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Symptom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfa on 14/03/2016.
 */
public class SymptomAdapter extends ArrayAdapter<Symptom> {

    private final List<Symptom> items;
    private final List<Symptom> itemsAll;
    private final List<Symptom> suggestions;
    private final Context mContext;

    public SymptomAdapter(Context context, int resource, ArrayList<Symptom> items) {
        super(context, resource, items);
        this.mContext = context;
        this.items = items;
        this.itemsAll = items == null ? items : (ArrayList<Symptom>) items.clone();
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
        Symptom symptom = items.get(position);
        if (symptom != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.lblStatus);
            if (customerNameLabel != null) {
                customerNameLabel.setText(symptom.getName());
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
            String str = ((Symptom)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint == null) {
                return new FilterResults();
            } else {
                suggestions.clear();
                for (Symptom user : itemsAll) {
                    String name = user.getName();
                    if(name != null && name.toLowerCase().contains(constraint.toString().toLowerCase())){
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
        protected synchronized void publishResults(CharSequence constraint, FilterResults results) {
            if(results != null && results.count > 0) {
            ArrayList<Symptom> filteredList = (ArrayList<Symptom>) results.values;
                clear();
                for (Symptom c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
