package com.tiendas3b.ticketdoctor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Ticket;
import com.tiendas3b.ticketdoctor.db.manager.DatabaseManager;
import com.tiendas3b.ticketdoctor.db.manager.IDatabaseManager;
import com.tiendas3b.ticketdoctor.fragments.BuysTabsFragment;
import com.tiendas3b.ticketdoctor.fragments.TicketsFragment;
import com.tiendas3b.ticketdoctor.util.Constants;
import com.tiendas3b.ticketdoctor.util.Preferences;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TicketsFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView lblMail = ((TextView) navigationView.getHeaderView(0).findViewById(R.id.lbl_mail));
        lblMail.setText(new Preferences(this).getSharedStringSafe(Preferences.KEY_LOGIN, getString(R.string.caption)));

        changeMainContent(new BuysTabsFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //TODO regresar a "home" (definir home)
//            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.manu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            retrofitTest();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_my_tickets) {
//            changeMainContent(new TicketsFragment());
//        } else
        if (id == R.id.nav_all_tickets) {
            changeMainContent(new TicketsFragment());
//            changeMainContent(/*new fragment*/);
//        } else if (id == R.id.nav_regional_ticket) {
//            changeMainContent(/*new fragment*/);
//        } else if (id == R.id.nav_statistics) {
////                    changeMainContent(/*new fragment*/);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sing_out) {
            singOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void singOut() {
        Preferences preferences = new Preferences(this);
        preferences.deleteData();
        IDatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.dropDatabase();
        databaseManager.closeDbConnections();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.content_frame, fragment)
//                .commit();
//
//        // Highlight the selected item, update the title, and close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void changeMainContent(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    public void onListFragmentInteraction(Ticket ticket) {
        Intent intentTicketDetail = new Intent(MainActivity.this, TicketDetailActivity.class);
        intentTicketDetail.putExtra(Constants.EXTRA_TICKET, ticket.getId());
        startActivity(intentTicketDetail);
    }

    @Override
    public void showPopupMenu(final Ticket ticket, ImageButton btnTckOptions) {
        PopupMenu popup = new PopupMenu(this, btnTckOptions);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_ticket, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();
                switch (id) {
                    case R.id.action_detail:
                        Intent intentTicketDetail = new Intent(MainActivity.this, TicketDetailActivity.class);
                        intentTicketDetail.putExtra(Constants.EXTRA_TICKET, ticket.getId());
                        startActivity(intentTicketDetail);
                        break;
//                    case R.id.action_edit:
//                        Intent intentTicketDetailEdit = new Intent(MainActivity.this, TicketDetailEditActivity.class);
//                        intentTicketDetailEdit.putExtra(Constants.EXTRA_TICKET, ticket.getId());
//                        startActivity(intentTicketDetailEdit);
//                        break;
//                    case R.id.action_delete:
//                        Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                        break;
                }
                return true;
            }
        });
        popup.show();
    }
}
