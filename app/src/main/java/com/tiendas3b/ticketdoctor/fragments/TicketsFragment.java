package com.tiendas3b.ticketdoctor.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tiendas3b.ticketdoctor.GlobalState;
import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.activities.TicketDetailEditActivity;
import com.tiendas3b.ticketdoctor.adapters.OnLoadMoreListener;
import com.tiendas3b.ticketdoctor.adapters.TicketRecyclerViewAdapter;
import com.tiendas3b.ticketdoctor.db.dao.Branch;
import com.tiendas3b.ticketdoctor.db.dao.Ticket;
import com.tiendas3b.ticketdoctor.db.manager.DatabaseManager;
import com.tiendas3b.ticketdoctor.http.TicketDoctorService;
import com.tiendas3b.ticketdoctor.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import com.tiendas3b.almacen.fragments.dummy.DummyContent.DummyItem;
//
//import java.util.List;

public class TicketsFragment extends Fragment /*implements SearchView.OnQueryTextListener*/ {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "TicketsFragment";
    private static final int REQUEST_CODE = 1;
    // TODO: Customize parameters
    private int mColumnCount = 0;
    private OnListFragmentInteractionListener mListener;
    private GlobalState mContext;
    private ViewSwitcher viewSwitcher;
    private RecyclerView recyclerView;
    private TicketRecyclerViewAdapter adapter;
    private List<Ticket> tickets;
    private SearchView searchView;
    private boolean canDownloadTickets;
    private DatabaseManager ticketManager;

    public TicketsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TicketsFragment newInstance(int columnCount) {
        TicketsFragment fragment = new TicketsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets_list, container, false);
        getActivity().setTitle(R.string.menu_drw_all_tickets);

        if (view instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) view);
            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
            viewSwitcher.setInAnimation(slide_in_left);
            viewSwitcher.setOutAnimation(slide_out_right);
        }

            ticketManager = new DatabaseManager(mContext);
        canDownloadTickets = mContext.canDownloadTickets();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(tickets != null){
                    final List<Ticket> filteredModelList = filter(tickets, query);
                    adapter.animateTo(filteredModelList);
                    recyclerView.scrollToPosition(0);
                }
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when collapsed
                return true;       // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapter.setLoaded();
                return true;      // Return true to expand action view
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(!canDownloadTickets) menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_search:
                return true;
            case R.id.action_new_ticket:
                navigateToNewTicket();
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToNewTicket() {
        if(mContext.canCreateTicket()){
            startActivityForResult(new Intent(mContext, TicketDetailEditActivity.class), REQUEST_CODE);
        }else{
            Toast.makeText(mContext, R.string.no_permissions, Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        if (tickets == null) {
            if (NetworkUtil.isConnected(mContext)) {
                downloadTickets();
            } else {
                getDatabaseInfo();
            }
        }
    }

    private void downloadTickets() {
        if(canDownloadTickets){
            TicketDoctorService ticketDoctorService = mContext.getHttpServiceWithAuth();
            Call<List<Ticket>> call;
            if(mColumnCount == 0) {
                call = ticketDoctorService.getTickets(0);
            } else {
                call = ticketDoctorService.getClosedTickets(0);
            }
            call.enqueue(new Callback<List<Ticket>>() {
                @Override
                public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                    if (response.isSuccessful()) {
                        response(response.body());
                    } else {
                        getDatabaseInfo();
                    }
                }

                @Override
                public void onFailure(Call<List<Ticket>> call, Throwable t) {
                    Log.e(TAG, mContext.getString(R.string.error_ticket_fragment_get_tickets));
                    getDatabaseInfo();
                }
            });
        }else{
            tickets = new ArrayList<>();
            viewSwitcher.showNext();
            TextView emptyView = (TextView) viewSwitcher.findViewById(R.id.emptyView);
            emptyView.setText(getString(R.string.no_permissions));
            emptyView.setVisibility(View.VISIBLE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
        }
    }

    private void getDatabaseInfo() {
        View view = viewSwitcher.findViewById(R.id.list);
        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;
            if(mColumnCount == 0) {
                tickets = ticketManager.listTickets();
            } else {
                tickets = ticketManager.listTickets(2L);
            }
            View emptyView = viewSwitcher.findViewById(R.id.emptyView);
            if (tickets.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                adapter = new TicketRecyclerViewAdapter(getActivity(), new ArrayList<>(tickets), mListener, recyclerView);
                recyclerView.setAdapter(adapter);
            }
            viewSwitcher.showNext();
        }
    }

    private void response(List<Ticket> ticketsList) {
        View view = viewSwitcher.findViewById(R.id.list);
        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;
            saveTickets(ticketsList);
            if(mColumnCount == 0) {
                tickets = ticketManager.listTickets();
            } else {
                tickets = ticketManager.listTickets(2L);
            }
            adapter = new TicketRecyclerViewAdapter(getActivity(), new ArrayList<>(tickets), mListener, recyclerView);
            recyclerView.setAdapter(adapter);
            adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if(recyclerView.isComputingLayout()) {
                        adapter.setLoaded();
                    }else{
                        if (adapter.getItemCount() > 0 && searchView.isIconified()) {
                            tickets.add(null);
                            adapter.notifyItemInserted(tickets.size() - 1);

                            TicketDoctorService ticketDoctorService = mContext.getHttpServiceWithAuth();
                            Call<List<Ticket>> call = ticketDoctorService.getTickets(tickets.size() - 1);
                            call.enqueue(new Callback<List<Ticket>>() {
                                @Override
                                public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                                    if (response.isSuccessful()) {
                                        //Remove loading item
                                        tickets.remove(tickets.size() - 1);
                                        adapter.notifyItemRemoved(tickets.size());
                                        tickets.addAll(response.body());
                                        adapter.animateTo(tickets);
                                        adapter.setLoaded();
                                        saveTickets(tickets);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Ticket>> call, Throwable t) {
                                    Log.e("TAG", "FAIL 2");
                                }
                            });
                        }
                    }

                }
            });
            viewSwitcher.showNext();
            adapter.addOnScrollListener();
        }
    }

    private void saveTickets(final List<Ticket> tickets) {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                ITicketManager databaseManager = new DatabaseManager(mContext);
                ticketManager.insertOrReplaceInTxTickets(tickets.toArray(new Ticket[tickets.size()]));
//                databaseManager.closeDbConnections();
//                return null;
//            }
//
//        }.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mContext = (GlobalState) getActivity().getApplicationContext();
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String query) {
//        MenuItemCompat.collapseActionView(searchItem);
//        final List<Ticket> filteredModelList = filter(tickets, query);
//        adapter.animateTo(filteredModelList);
//        recyclerView.scrollToPosition(0);
//        return true;
//    }

    private List<Ticket> filter(List<Ticket> models, String query) {
        query = query.toLowerCase();
        final List<Ticket> filteredModelList = new ArrayList<>();
        for (Ticket model : models) {
            final String text = String.valueOf(model.getId());
            final String textCaption = model.getCaption();
            Branch branch = model.getBranch();
            String number = branch.getNumber3b();
            final String textBranch = number == null ? "" : number + branch.getName();
            if ((textCaption != null && textCaption.toLowerCase().contains(query)) || textBranch.toLowerCase().contains(query) || text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onDestroyView() {
        tickets = null;
        if (ticketManager != null) ticketManager.closeDbConnections();
        super.onDestroyView();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Ticket ticket);

        void showPopupMenu(Ticket ticket, ImageButton btnTckOptions);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
//                String result=data.getStringExtra("result");
                viewSwitcher.showPrevious();
                downloadTickets();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
