package com.tiendas3b.ticketdoctor.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Ticket;
import com.tiendas3b.ticketdoctor.fragments.TicketsFragment.OnListFragmentInteractionListener;

import java.util.List;

public class TicketRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final List<Ticket> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Activity mContext;
    private final RecyclerView mRecyclerView;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 7;
    private int lastVisibleItem;
    private int totalItemCount;

    public TicketRecyclerViewAdapter(Activity context, List<Ticket> items, OnListFragmentInteractionListener listener, RecyclerView recyclerView) {
        mValues = items;
        mListener = listener;
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext/*parent.getContext()*/).inflate(R.layout.row_ticket, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder ticketViewHolder = (ViewHolder) holder;
            ticketViewHolder.mItem = mValues.get(position);
            String captionVal = ticketViewHolder.mItem.getCaption();
            String caption = captionVal == null ? "" : "-" + captionVal;
            ticketViewHolder.txtTicket.setText(ticketViewHolder.mItem.getId() + caption);
            ticketViewHolder.txtCaption.setText(ticketViewHolder.mItem.getCaption());

            ticketViewHolder.btnTckOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.showPopupMenu(ticketViewHolder.mItem, ticketViewHolder.btnTckOptions);
                }
            });
//        holder.lblStoreName.setText(mValues.get(position).getName());
//        holder.lblRegion.setText(mValues.get(position).getRegionName());

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(ticketViewHolder.mItem);
                    }
                }
            };
            ticketViewHolder.txtTicket.setOnClickListener(listener);
            ticketViewHolder.mView.setOnClickListener(listener);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return mValues.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void animateTo(List<Ticket> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Ticket> newModels) {
        for (int i = mValues.size() - 1; i >= 0; i--) {
            final Ticket model = mValues.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Ticket> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Ticket model = newModels.get(i);
            if (!mValues.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Ticket> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Ticket model = newModels.get(toPosition);
            final int fromPosition = mValues.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Ticket removeItem(int position) {
        final Ticket model = mValues.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Ticket model) {
        mValues.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Ticket model = mValues.remove(fromPosition);
        mValues.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
    //---------

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageButton btnTckOptions;
        public final EditText txtTicket;
        public final EditText txtCaption;
        public Ticket mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            btnTckOptions = (ImageButton) view.findViewById(R.id.btnTckOptions);
            txtTicket = (EditText) view.findViewById(R.id.txtTicket);
            txtCaption = (EditText) view.findViewById(R.id.txtCaption);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    public void addOnScrollListener() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        isLoading = true;
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }
}
