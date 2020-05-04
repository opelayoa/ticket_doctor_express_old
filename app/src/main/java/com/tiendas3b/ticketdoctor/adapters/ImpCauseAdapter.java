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
import com.tiendas3b.ticketdoctor.db.dao.ImpCause;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfa on 14/03/2016.
 */
public class ImpCauseAdapter extends ArrayAdapter<ImpCause> {

    private final List<ImpCause> items;
    private final List<ImpCause> itemsAll;
    private final List<ImpCause> suggestions;
    private final Activity mContext;

    public ImpCauseAdapter(Activity context, int resource, ArrayList<ImpCause> items) {
        super(context, resource, items);
        this.mContext = context;
        this.items = items;
        this.itemsAll = (ArrayList<ImpCause>) items.clone();
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
        ImpCause item = items.get(position);
        if (item != null) {
            TextView txtItem = (TextView) v.findViewById(R.id.lblStatus);
            if (txtItem != null) {
                txtItem.setText(getText(item));
            }
        }
        return v;
    }

    private String getText(ImpCause item) {
        return item.getDescription() + " - " + item.getImpSolution() + " - " + item.getCode();
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            ImpCause item = ((ImpCause)(resultValue));
            String str = getText(item);
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint == null) {
                return new FilterResults();
            } else {
                suggestions.clear();
                for (ImpCause item : itemsAll) {
                    String alias = getText(item);
                    if(/*alias != null && */alias.toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(item);
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
            ArrayList<ImpCause> filteredList = (ArrayList<ImpCause>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (ImpCause c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
