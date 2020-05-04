package com.tiendas3b.ticketdoctor.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Type;

import java.util.List;

/**
 * Created by dfa on 14/03/2016.
 */
public class TypeAdapter extends ArrayAdapter<Type> {

    private final List<Type> types;
    private final Activity mContext;

    public TypeAdapter(Activity context, int resource, List<Type> types) {
        super(context, resource, types);
        this.mContext = context;
        this.types = types;
    }

    @Override
    public long getItemId(int position) {
        return types.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getRow(position, convertView, parent, R.layout.row_ticket_status_selected);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getRow(position, convertView, parent,R.layout.row_ticket_status);
    }

    private View getRow(int position, View convertView, ViewGroup parent, int layout) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
        }
        String item = types.get(position).getName();
        if (item != null) {
            TextView text1 = (TextView) row.findViewById(R.id.lblStatus);
            text1.setText(item);
        }
        return row;
    }
}
