package com.tiendas3b.ticketdoctor.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Action;
import com.tiendas3b.ticketdoctor.db.dao.Provider;
import com.tiendas3b.ticketdoctor.db.dao.User;
import com.tiendas3b.ticketdoctor.util.Constants;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ActionRecyclerViewAdapter extends RecyclerView.Adapter<ActionRecyclerViewAdapter.ViewHolder> {

    private final List<Action> mValues;
    private final Activity mContext;
    private final SimpleDateFormat df;
//    private final DatabaseManager databaseManager;

    public ActionRecyclerViewAdapter(Activity context, List<Action> items) {
        mValues = items;
        mContext = context;
        df = new SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.getDefault());
//        databaseManager = new DatabaseManager(mContext);
//        databaseManager = DatabaseManager.getInstance(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext/*parent.getContext()*/).inflate(R.layout.row_action, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.txtDate.setText(df.format(holder.mItem.getDate()));
        User technician = holder.mItem.getTechnician();
        holder.txtTechnician.setText(technician.getLastName() + ", " + technician.getName());
        Provider provider = holder.mItem.getProvider();
        holder.txtProvider.setText(provider == null ? mContext.getString(R.string.ticket_none) : provider.getBusinessName());
        holder.lblDescription.setText(holder.mItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final EditText txtDate;
        public final EditText txtTechnician;
        public final EditText txtProvider;
        public final TextView lblDescription;
        public Action mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtDate = (EditText) view.findViewById(R.id.txtDate);
            txtTechnician = (EditText) view.findViewById(R.id.txtTechnician);
            txtProvider = (EditText) view.findViewById(R.id.txtProvider);
            lblDescription = (TextView) view.findViewById(R.id.lblDescription);

        }
    }
}
