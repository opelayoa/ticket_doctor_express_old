package com.tiendas3b.ticketdoctor.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.ProjectStatus;

import java.util.List;

/**
 * Created by dfa on 14/03/2016.
 */
public class ProjectStatusAdapter extends ArrayAdapter<ProjectStatus> {

    private final List<ProjectStatus> ticketStatuses;
    private final Activity mContext;

    public ProjectStatusAdapter(Activity context, int resource, List<ProjectStatus> ticketStatuses) {
        super(context, resource, ticketStatuses);
        this.mContext = context;
        this.ticketStatuses = ticketStatuses;
    }

    @Override
    public long getItemId(int position) {
        return ticketStatuses.get(position).getId();
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
        String item = ticketStatuses.get(position).getName();
        if (item != null) {
            TextView text1 = (TextView) row.findViewById(R.id.lblStatus);
            text1.setText(item);
        }
        return row;
    }
}
