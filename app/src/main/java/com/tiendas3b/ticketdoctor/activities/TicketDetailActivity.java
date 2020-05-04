package com.tiendas3b.ticketdoctor.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.GlobalState;
import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.adapters.ActionRecyclerViewAdapter;
import com.tiendas3b.ticketdoctor.db.dao.Action;
import com.tiendas3b.ticketdoctor.db.dao.Branch;
import com.tiendas3b.ticketdoctor.db.dao.ImpCause;
import com.tiendas3b.ticketdoctor.db.dao.PossibleOrigin;
import com.tiendas3b.ticketdoctor.db.dao.ProjectStatus;
import com.tiendas3b.ticketdoctor.db.dao.Provider;
import com.tiendas3b.ticketdoctor.db.dao.StandardSolution;
import com.tiendas3b.ticketdoctor.db.dao.SymptomDiagnostic;
import com.tiendas3b.ticketdoctor.db.dao.Ticket;
import com.tiendas3b.ticketdoctor.db.dao.Type;
import com.tiendas3b.ticketdoctor.db.dao.User;
import com.tiendas3b.ticketdoctor.db.manager.DatabaseManager;
import com.tiendas3b.ticketdoctor.http.TicketDoctorService;
import com.tiendas3b.ticketdoctor.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketDetailActivity extends AppCompatActivity {

    private Ticket ticket;
    private GlobalState mContext;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        mContext = (GlobalState) getApplicationContext();
        databaseManager = new DatabaseManager(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Algo", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            }
//        });

        init();

    }

    @Override
    protected void onRestart() {
        if (databaseManager == null) databaseManager = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        databaseManager = DatabaseManager.getInstance(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onStop();
    }

    @SuppressWarnings("ConstantConditions")
    private void init() {
        long ticketId = getIntent().getLongExtra(Constants.EXTRA_TICKET, 0L);
        ticket = databaseManager.getTicketById(ticketId);
        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        setTitle(getString(R.string.ticket_id, ticket.getId()));
        ((TextView) findViewById(R.id.lblStatus)).setText(ticket.getStatus().getName());
        ((TextView) findViewById(R.id.lblOpeningDate)).setText(df.format(ticket.getOpeningDate()));
        Date solutionDate = ticket.getSolutionDate();
        ((TextView) findViewById(R.id.lblSolutionDate)).setText(solutionDate == null ? df.format(new Date()) : df.format(solutionDate));//TODO sumar 2 dias a fecha de apertura
        ((TextView) findViewById(R.id.lblCaption)).setText(ticket.getCaption());
        User applicant = ticket.getApplicant();
        ((TextView) findViewById(R.id.lblApplicant)).setText(applicant.getLastName() + ", " + applicant.getName());
        User technician = ticket.getTechnician();
        ((TextView) findViewById(R.id.lblTechnician)).setText(technician == null ? getString(R.string.ticket_na) : technician.getLastName() + ", " + technician.getName());//TODO definir mensaje
        ((TextView) findViewById(R.id.lblCategory)).setText(getCategoryFake(ticket.getCategory().getId()));//TODO
        Type type = ticket.getType();
        ((TextView) findViewById(R.id.lblType)).setText(type == null ? getString(R.string.ticket_na) : type.getName());
        ((TextView) findViewById(R.id.lblSymptom)).setText(ticket.getTypeSymptom().getSymptom().getName());
        SymptomDiagnostic symptomDiagnostic = ticket.getSymptomDiagnostic();
        ((TextView) findViewById(R.id.lblDiagnostic)).setText(symptomDiagnostic == null ? getString(R.string.ticket_na) : symptomDiagnostic.getDiagnostic().getDescription());
        Branch branch = ticket.getBranch();
        ((TextView) findViewById(R.id.lblBranch)).setText(branch.getNumber3b() + "-" + branch.getName());
        StandardSolution standardSolution = ticket.getStandardSolution();
        ((TextView) findViewById(R.id.lblSolution)).setText(standardSolution == null ? getString(R.string.ticket_none) : standardSolution.getName());
        Boolean rs = ticket.getRemoteSolution();
        ((TextView) findViewById(R.id.lblRemoteSolution)).setText(rs != null && rs ? R.string.yes : R.string.no);
        PossibleOrigin possibleOrigin = ticket.getPossibleOrigin();
        ((TextView) findViewById(R.id.lblPossibleOrigin)).setText(possibleOrigin == null ? getString(R.string.ticket_none) : possibleOrigin.getName());
        Date updateDate = ticket.getUpdateDate();
        ((TextView) findViewById(R.id.lblUpdateDate)).setText(updateDate == null ? getString(R.string.ticket_na) : df.format(updateDate));
        User waitingForUser = ticket.getWaitingForUser();
        ((TextView) findViewById(R.id.lblWaitingForUser)).setText(waitingForUser == null ? getString(R.string.ticket_none) : waitingForUser.getAlias());
        Provider waitingForProvider = ticket.getWaitingForProvider();
        ((TextView) findViewById(R.id.lblWaitingForProvider)).setText(waitingForProvider == null ? getString(R.string.ticket_none) : waitingForProvider.getBusinessName());
        ImpCause impCause = ticket.getCause();
        ((TextView) findViewById(R.id.lblCause)).setText(impCause == null ? getString(R.string.ticket_none) : createCause(impCause));
        User keyboarder = ticket.getKeyboarder();
        ((TextView) findViewById(R.id.lblKeyboarder)).setText(keyboarder == null ? "No se encontró" : keyboarder.getLastName() + ", " + keyboarder.getName());//TODO
        ProjectStatus projectStatus = ticket.getProjectStatus();
        ((TextView) findViewById(R.id.lblProjectStatus)).setText(projectStatus == null ? getString(R.string.ticket_none) : projectStatus.getName());
        ((TextView) findViewById(R.id.lblObservations)).setText(ticket.getObservations());

        TicketDoctorService ticketDoctorService = mContext.getHttpServiceWithAuth();
        Call<List<Action>> call = ticketDoctorService.getActions(ticketId);
        call.enqueue(new Callback<List<Action>>() {
            @Override
            public void onResponse(Call<List<Action>> call, Response<List<Action>> response) {
                if (response.isSuccessful()) {
                    saveActions(response.body());
                } else {
                    getDatabaseInfo();
                }
            }

            @Override
            public void onFailure(Call<List<Action>> call, Throwable t) {
                Log.e("TAG", "poner un log XD");
                getDatabaseInfo();
            }
        });

        findViewById(R.id.llActions).setVisibility(View.VISIBLE);
    }

    private String getCategoryFake(Long id) {
        switch (id.intValue()){
            case 1:
                return getString(R.string.ticket_category_central_support);
            case 2:
                return getString(R.string.ticket_category_central_systems);
            case 3:
                return getString(R.string.ticket_category_regional_support);
            case 4:
                return getString(R.string.ticket_category_regional_systems);
        }
        return getString(R.string.ticket_na);
    }

    private void getDatabaseInfo() {
        if (!isFinishing()) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
            List<Action> actions = databaseManager.getActionsByTicketId(ticket.getId());
            View emptyView = findViewById(R.id.emptyView);
            if (actions == null || actions.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                recyclerView.setAdapter(new ActionRecyclerViewAdapter(this, databaseManager.getActionsByTicketId(ticket.getId())));
            }
        }
    }

    private void saveActions(List<Action> actions) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        databaseManager.insertOrReplaceInTxActions(actions.toArray(new Action[actions.size()]));
        recyclerView.setAdapter(new ActionRecyclerViewAdapter(this, databaseManager.getActionsByTicketId(ticket.getId())));
    }

    private CharSequence createCause(ImpCause impCause) {
        return impCause.getDescription() + " - " + impCause.getImpSolution() + " - " + impCause.getCode();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_ticket_detail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_edit) {
//            Intent intentTicketDetail = new Intent(this, TicketDetailEditActivity.class);
//            intentTicketDetail.putExtra(Constants.EXTRA_TICKET, ticket.getId());
//            startActivity(intentTicketDetail);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
