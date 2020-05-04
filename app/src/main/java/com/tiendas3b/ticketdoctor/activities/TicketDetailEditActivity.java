package com.tiendas3b.ticketdoctor.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.farbod.labelledspinner.LabelledSpinner;
import com.tiendas3b.ticketdoctor.GlobalState;
import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.adapters.ActionRecyclerViewAdapter;
import com.tiendas3b.ticketdoctor.adapters.ApplicantAdapter;
import com.tiendas3b.ticketdoctor.adapters.BranchAdapter;
import com.tiendas3b.ticketdoctor.adapters.CategoryAdapter;
import com.tiendas3b.ticketdoctor.adapters.DiagnosticAdapter;
import com.tiendas3b.ticketdoctor.adapters.ImpCauseAdapter;
import com.tiendas3b.ticketdoctor.adapters.PossibleOriginAdapter;
import com.tiendas3b.ticketdoctor.adapters.ProjectStatusAdapter;
import com.tiendas3b.ticketdoctor.adapters.ProviderAdapter;
import com.tiendas3b.ticketdoctor.adapters.SymptomAdapter;
import com.tiendas3b.ticketdoctor.adapters.TicketStatusAdapter;
import com.tiendas3b.ticketdoctor.adapters.TypeAdapter;
import com.tiendas3b.ticketdoctor.db.dao.Action;
import com.tiendas3b.ticketdoctor.db.dao.Branch;
import com.tiendas3b.ticketdoctor.db.dao.Category;
import com.tiendas3b.ticketdoctor.db.dao.Diagnostic;
import com.tiendas3b.ticketdoctor.db.dao.ImpCause;
import com.tiendas3b.ticketdoctor.db.dao.PossibleOrigin;
import com.tiendas3b.ticketdoctor.db.dao.ProjectStatus;
import com.tiendas3b.ticketdoctor.db.dao.Provider;
import com.tiendas3b.ticketdoctor.db.dao.StandardSolution;
import com.tiendas3b.ticketdoctor.db.dao.Symptom;
import com.tiendas3b.ticketdoctor.db.dao.SymptomDiagnostic;
import com.tiendas3b.ticketdoctor.db.dao.Ticket;
import com.tiendas3b.ticketdoctor.db.dao.TicketStatus;
import com.tiendas3b.ticketdoctor.db.dao.Type;
import com.tiendas3b.ticketdoctor.db.dao.TypeSymptom;
import com.tiendas3b.ticketdoctor.db.dao.User;
import com.tiendas3b.ticketdoctor.db.manager.DatabaseManager;
import com.tiendas3b.ticketdoctor.http.GeneralResponse;
import com.tiendas3b.ticketdoctor.http.TicketDoctorService;
import com.tiendas3b.ticketdoctor.util.Constants;
import com.tiendas3b.ticketdoctor.util.FileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

@SuppressWarnings("ConstantConditions")
public class TicketDetailEditActivity extends AppCompatActivity {

    private static final String TAG = "TicketDetailEdit";
    private static final int PICK_FILE_REQUEST_CODE = 2;
    private GlobalState mContext;
    private DatabaseManager databaseManager;
    private Ticket ticket;
    private LabelledSpinner spnTicketStatus;
    private EditText txtOpeningDate;
    private EditText txtSolutionDate;
    private EditText txtCaption;
    private AutoCompleteTextView txtApplicant;
    private AutoCompleteTextView txtTechnician;
    private LabelledSpinner spnCategory;
    private LabelledSpinner spnType;
    private AutoCompleteTextView txtSymptom;
    private AutoCompleteTextView txtDiagnostic;
    private AutoCompleteTextView txtBranch;
    private EditText txtObservations;
    private LabelledSpinner spnPossibleOrigin;
    private LabelledSpinner spnProjectStatus;
    private long ticketStatusId;
    private long applicantId;
    private long technicianId;
    private long typeId;
    private long symptomId;
    private long diagnosticId;
    private long branchId;
    private long possibleOriginId;
    private long projectStatusId;
    private long categoryId;
    private String mCaption;
    private String mObservations;
    private EditText txtFile;
    private String filePath;
    private Date solutionDate;
    private CategoryAdapter categoryAdapter;
    private TypeAdapter typeAdapter;
    private PossibleOriginAdapter possibleOriginAdapter;
    private ProjectStatusAdapter projectStatusAdapter;
    private AutoCompleteTextView txtSolution;
    private Switch swtRemoteSolution;
    private Button btnAddAction;
    private boolean clearSymptom;
    private AutoCompleteTextView txtWaitingForUser;
    private AutoCompleteTextView txtWaitingForProvider;
    private AutoCompleteTextView txtCause;
    private long waitingForUserId;
    private long waitingForProviderId;
    private long causeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail_edit);

        mContext = (GlobalState) getApplicationContext();//TicketDetailEditActivity.this;
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
//                Snackbar.make(view, "algo", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            }
//        });

        init();

//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int screenHeight = displaymetrics.heightPixels;
//
//        int actionBarHeight = 0;
//        TypedValue tv = new TypedValue();
//        if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
//        }
//
//        LinearLayout view = (LinearLayout) findViewById(R.id.llTicketDetailEdit);
//        view.setMinimumHeight(screenHeight + actionBarHeight);
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

    private void init() {
        txtCaption = (EditText) findViewById(R.id.txtCaption);
        txtApplicant = (AutoCompleteTextView) findViewById(R.id.txtApplicant);
        txtTechnician = (AutoCompleteTextView) findViewById(R.id.txtTechnician);
        spnCategory = (LabelledSpinner) findViewById(R.id.spnCategory);
        spnType = (LabelledSpinner) findViewById(R.id.spnType);
        txtSymptom = (AutoCompleteTextView) findViewById(R.id.txtSymptom);
        txtDiagnostic = (AutoCompleteTextView) findViewById(R.id.txtDiagnostic);
        txtBranch = (AutoCompleteTextView) findViewById(R.id.txtBranch);
        txtFile = (EditText) findViewById(R.id.txtFile);
        txtObservations = (EditText) findViewById(R.id.txtObservations);
        spnPossibleOrigin = (LabelledSpinner) findViewById(R.id.spnPossibleOrigin);
        spnProjectStatus = (LabelledSpinner) findViewById(R.id.spnProjectStatus);

//        //dummy
//        txtCaption.setText("Ticket prueba android n");
//        txtObservations.setText("Ticket de prueba n");
//        applicantId = 89L;
//        technicianId = 504L;
//        categoryId = 3L;
//        typeId = 4L;
//        symptomId = 469L;
//        diagnosticId = 772L;
//        branchId = 805L;
//        possibleOriginId = 6L;
//        projectStatusId = 2L;
//        //


        ArrayList<User> applicants = DatabaseManager.getInstance(this).listApplicants();
        txtApplicant.setThreshold(3);
        txtApplicant.setAdapter(new ApplicantAdapter(this, android.R.layout.simple_expandable_list_item_1, applicants));
        txtApplicant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                applicantId = id;
            }
        });

        ArrayList<User> technicians = DatabaseManager.getInstance(this).listTechnicians();
        txtTechnician.setThreshold(1);
        txtTechnician.setAdapter(new ApplicantAdapter(this, android.R.layout.simple_expandable_list_item_1, technicians));//TODO definir tecnico
        txtTechnician.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                technicianId = id;
            }
        });

        ArrayList<Category> categories = createCategories();
        categoryAdapter = new CategoryAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spnCategory.setCustomAdapter(categoryAdapter);
        spnCategory.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                    categoryId = id;
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        ArrayList<Type> types = DatabaseManager.getInstance(this).listTypesWithNone(getString(R.string.opt_none));
//            spnStatus.setDefaultErrorEnabled(true);
//            spnStatus.setDefaultErrorText("This is a compulsory field.");  // Displayed when first item remains selected
        typeAdapter = new TypeAdapter(this, android.R.layout.simple_spinner_dropdown_item, types);
        spnType.setCustomAdapter(typeAdapter);
//            spnType.setSelection(0);
        spnType.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                typeId = id;
                symptomId = 0L;
                diagnosticId = 0L;
                txtSymptom.setThreshold(2);
                if(clearSymptom){
                    txtSymptom.setText("");
                }else{
                    clearSymptom = true;
                }
                ArrayList<Symptom> symptoms = databaseManager.listSymptoms(typeId);
                txtSymptom.setAdapter(new SymptomAdapter(TicketDetailEditActivity.this, android.R.layout.simple_expandable_list_item_1, symptoms));
                txtSymptom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        symptomId = id;
                        diagnosticId = 0L;
                        final ArrayList<Diagnostic> diagnostics = databaseManager.listDiagnostics(symptomId);
                        txtDiagnostic.setThreshold(1);
                        txtDiagnostic.setText("");
                        txtDiagnostic.setAdapter(new DiagnosticAdapter(TicketDetailEditActivity.this, android.R.layout.simple_expandable_list_item_1, diagnostics));
                        txtDiagnostic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                diagnosticId = id;
                            }
                        });
                    }
                });
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
                Log.e(TAG, "spnType-onNothingChosen");
            }
        });

        final ArrayList<Branch> diagnostics = databaseManager.listBranches();
        txtBranch.setThreshold(3);
        txtBranch.setAdapter(new BranchAdapter(this, android.R.layout.simple_expandable_list_item_1, diagnostics));
        txtBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                branchId = id;
            }
        });

        txtFile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    findFile();
                }
            }
        });
        txtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findFile();
            }
        });

        final ArrayList<PossibleOrigin> possibleOrigins = DatabaseManager.getInstance(this).listPossibleOriginsWithNone(getString(R.string.opt_none));
//            spnPossibleOrigin.setDefaultErrorEnabled(true);
//            spnPossibleOrigin.setDefaultErrorText("This is a compulsory field.");  // Displayed when first item remains selected
        possibleOriginAdapter = new PossibleOriginAdapter(this, android.R.layout.simple_spinner_dropdown_item, possibleOrigins);
        spnPossibleOrigin.setCustomAdapter(possibleOriginAdapter);
        spnPossibleOrigin.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                possibleOriginId = id;
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        final ArrayList<ProjectStatus> projectStatuses = DatabaseManager.getInstance(this).listProjectStatusesWithNone(getString(R.string.opt_none));
        projectStatusAdapter = new ProjectStatusAdapter(this, android.R.layout.simple_spinner_dropdown_item, projectStatuses);
        spnProjectStatus.setCustomAdapter(projectStatusAdapter);
        spnProjectStatus.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                projectStatusId = id;
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });


        long ticketId = getIntent().getLongExtra(Constants.EXTRA_TICKET, 0L);
        if (ticketId == 0L) {
            //TODO implementar
        } else {
            ticket = databaseManager.getTicketById(ticketId);
            fillFields();
        }
    }

    private void fillFields() {
        setTitle(getString(R.string.ticket_id, ticket.getId()));

        spnTicketStatus = (LabelledSpinner) findViewById(R.id.spnTicketStatus);
        TextInputLayout tilOpeningDate = (TextInputLayout) findViewById(R.id.tilOpeningDate);
        txtOpeningDate = (EditText) findViewById(R.id.txtOpeningDate);
        TextInputLayout tilSolutionDate = (TextInputLayout) findViewById(R.id.tilSolutionDate);
        txtSolutionDate = (EditText) findViewById(R.id.txtSolutionDate);
        TextInputLayout tilSolution = (TextInputLayout) findViewById(R.id.tilSolution);
        txtSolution = (AutoCompleteTextView) findViewById(R.id.txtSolution);
        swtRemoteSolution = (Switch) findViewById(R.id.swtRemoteSolution);
        btnAddAction = (Button) findViewById(R.id.btnAddAction);
        TextInputLayout tilWaitingForUser = (TextInputLayout) findViewById(R.id.tilWaitingForUser);
        txtWaitingForUser = (AutoCompleteTextView) findViewById(R.id.txtWaitingForUser);
        TextInputLayout tilWaitingForProvider = (TextInputLayout) findViewById(R.id.tilWaitingForProvider);
        txtWaitingForProvider = (AutoCompleteTextView) findViewById(R.id.txtWaitingForProvider);
        TextInputLayout tilCause = (TextInputLayout) findViewById(R.id.tilCause);
        txtCause = (AutoCompleteTextView) findViewById(R.id.txtCause);


        final ArrayList<TicketStatus> ticketStatus = DatabaseManager.getInstance(this).listTicketStatus();
        spnTicketStatus.setVisibility(View.VISIBLE);
//        spnTicketStatus.setDefaultErrorEnabled(true);
//        spnTicketStatus.setDefaultErrorText("This is a compulsory field.");  // Displayed when first item remains selected
        TicketStatusAdapter ticketStatusAdapter = new TicketStatusAdapter(this, android.R.layout.simple_spinner_dropdown_item, ticketStatus);
        spnTicketStatus.setCustomAdapter(ticketStatusAdapter);
        spnTicketStatus.setSelection(ticketStatusAdapter.getPosition(ticket.getStatus()));
        spnTicketStatus.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                ticketStatusId = id;
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());

        txtOpeningDate.setText(df.format(ticket.getOpeningDate()));
        tilOpeningDate.setVisibility(View.VISIBLE);

        tilSolutionDate.setVisibility(View.VISIBLE);
        txtSolutionDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showCalendar();
                }
            }
        });
        txtSolutionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        txtCaption.setText(ticket.getCaption());

        User applicant = ticket.getApplicant();
        txtApplicant.setText(applicant.getLastName() + ", " + applicant.getName());
        applicantId = ticket.getApplicantId();

        User technician = ticket.getTechnician();
        txtTechnician.setText(technician.getLastName() + ", " + technician.getName());
        technicianId = ticket.getTechnicianId();

        spnCategory.setSelection(categoryAdapter.getPosition(ticket.getCategory()));

        spnType.setSelection(typeAdapter.getPosition(ticket.getType()));

        TypeSymptom typeSymptom = ticket.getTypeSymptom();
        if(typeSymptom != null){
            Symptom symptom = typeSymptom.getSymptom();
            txtSymptom.setText(symptom.getName());
            symptomId = ticket.getSymptomId();
            clearSymptom = false;
        }

        SymptomDiagnostic symptomDiagnostic = ticket.getSymptomDiagnostic();
        if(symptomDiagnostic != null){
            Diagnostic diagnostic = symptomDiagnostic.getDiagnostic();
            txtDiagnostic.setText(diagnostic.getDescription());
            diagnosticId = ticket.getDiagnosticId();
        }

        Branch branch = ticket.getBranch();
        txtBranch.setText(branch.getNumber3b() + "-" + branch.getName());
        branchId = ticket.getBranchId();

        tilSolution.setVisibility(View.VISIBLE);
        StandardSolution standardSolution = ticket.getStandardSolution();
        txtSolution.setText(standardSolution == null ? "" : standardSolution.getName());

        swtRemoteSolution.setChecked(ticket.getRemoteSolution());
        swtRemoteSolution.setVisibility(View.VISIBLE);

        spnPossibleOrigin.setSelection(possibleOriginAdapter.getPosition(ticket.getPossibleOrigin()));

        tilWaitingForUser.setVisibility(View.VISIBLE);
        ArrayList<User> users = DatabaseManager.getInstance(this).listUser();
        txtWaitingForUser.setThreshold(2);
        txtWaitingForUser.setAdapter(new ApplicantAdapter(this, android.R.layout.simple_expandable_list_item_1, users));//TODO definir tecnico
        User waitingForUser = ticket.getWaitingForUser();
        txtWaitingForUser.setText(waitingForUser == null ? "" : waitingForUser.getLastName() + ", " + waitingForUser.getName());
        txtWaitingForUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                waitingForUserId = id;
            }
        });

        tilWaitingForProvider.setVisibility(View.VISIBLE);
        ArrayList<Provider> providers = DatabaseManager.getInstance(this).listProviders();
        txtWaitingForProvider.setThreshold(2);
        txtWaitingForProvider.setAdapter(new ProviderAdapter(this, android.R.layout.simple_expandable_list_item_1, providers));//TODO definir tecnico
        Provider waitingForProvider = ticket.getWaitingForProvider();
        txtWaitingForProvider.setText(waitingForProvider == null ? "" : waitingForProvider.getBusinessName());
        txtWaitingForProvider.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                waitingForProviderId = id;
            }
        });

        tilCause.setVisibility(View.VISIBLE);
        ArrayList<ImpCause> causes = DatabaseManager.getInstance(this).listCauses();
        txtCause.setThreshold(2);
        txtCause.setAdapter(new ImpCauseAdapter(this, android.R.layout.simple_expandable_list_item_1, causes));//TODO definir tecnico
        ImpCause cause = ticket.getCause();
        txtCause.setText(cause == null ? "" : cause.getDescription() + " - " + cause.getImpSolution() + " - " + cause.getCode());
        txtCause.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                causeId = id;
            }
        });

        txtObservations.setText(ticket.getObservations());

        spnProjectStatus.setSelection(projectStatusAdapter.getPosition(ticket.getProjectStatus()));

        btnAddAction.setVisibility(View.VISIBLE);
        btnAddAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO formulario accion
            }
        });

        TicketDoctorService ticketDoctorService = mContext.getHttpServiceWithAuth();
        Call<List<Action>> call = ticketDoctorService.getActions(ticket.getId());
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

    private void showCalendar() {
        final SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
//                Date d = new Date();//TODO definir que hora
                solutionDate = c.getTime();
                txtSolutionDate.setText(df.format(solutionDate));
            }
        }, year, month, day);
        dateDialog.getDatePicker().setMinDate(ticket.getOpeningDate().getTime());
        dateDialog.show();

//        DateDialog dialog=new DateDialog();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        dialog.show(ft, "DatePicker");


//        CalendarDialogBuilder calendar;
////        if(initialDate != null) {
////            calendar = new CalendarDialogBuilder(this, this, initialDate.getTime());
////        } else {
//            calendar = new CalendarDialogBuilder(this, this);
////        }
//
//        calendar.setStartDate(new Date().getTime());
////        calendar.setEndDate(endDate.getTime());
//        calendar.showCalendar();
    }

    private void findFile() {
        if (!mayRequestPermission()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    private ArrayList<Category> createCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(0L, getString(R.string.opt_none), null));
        categories.add(new Category(1L, getString(R.string.ticket_category_central_support), null));
        categories.add(new Category(2L, getString(R.string.ticket_category_central_systems), null));
        categories.add(new Category(3L, getString(R.string.ticket_category_regional_support), null));
        categories.add(new Category(4L, getString(R.string.ticket_category_regional_systems), null));
        return categories;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ticket_detail_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveOrUpdate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveOrUpdate() {
        if(valid()){
            final Ticket newTicket = createTicket();
            TicketDoctorService ticketDoctorService = mContext.getHttpServiceWithAuth();
            ticketDoctorService.insertTicket(newTicket).enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(mContext, response.body().getDescription(), Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        //                    returnIntent.putExtra("result", result);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Toast.makeText(mContext, "Hubo un error, verifica en versión web", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure saveOrUpdate Ticket");
                    Toast.makeText(mContext, "Hubo un error, verifica en versión web", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean valid() {
        mCaption = txtCaption.getText().toString();
//        if(mCaption.isEmpty()){
////            ((TextInputLayout)findViewById(R.id.tilCaption)).setError("Campo no válido");
//            txtCaption.setError(getString(R.string.ticket_field_no_valid));
//            txtCaption.requestFocus();
//            return false;
//        }

        if(txtApplicant.getText().toString().isEmpty() || applicantId == 0L){
            txtApplicant.setError(getString(R.string.ticket_field_no_valid));
            txtApplicant.requestFocus();
            return false;
        }

//        if(txtTechnician.getText().toString().isEmpty() || technicianId == 0L){
//            txtTechnician.setError(getString(R.string.ticket_field_no_valid));
//            txtTechnician.requestFocus();
//            return false;
//        }

        if(spnCategory.getSpinner().getSelectedItemId() == 0L || categoryId == 0L){
            spnCategory.requestFocus();
            return false;
        }

        if(spnType.getSpinner().getSelectedItemId() == 0L || typeId == 0L){
            spnType.requestFocus();
            return false;
        }

        if(txtSymptom.getText().toString().isEmpty() || symptomId == 0L){
            txtSymptom.setError(getString(R.string.ticket_field_no_valid));
            txtSymptom.requestFocus();
            return false;
        }

        if(txtDiagnostic.getText().toString().isEmpty() || diagnosticId == 0L){
            txtDiagnostic.setError(getString(R.string.ticket_field_no_valid));
            txtDiagnostic.requestFocus();
            return false;
        }

        if(txtBranch.getText().toString().isEmpty() || branchId == 0L){
            txtBranch.setError(getString(R.string.ticket_field_no_valid));
            txtBranch.requestFocus();
            return false;
        }

        if(!txtFile.getText().toString().isEmpty()){
            File attach = new File(filePath);
            if(!attach.exists()){
                txtFile.setError(getString(R.string.ticket_field_no_valid));
            }
        }

//        if(spnPossibleOrigin.getSpinner().getSelectedItemId() == 0L || possibleOriginId == 0L){
//            spnPossibleOrigin.requestFocus();
//            return false;
//        }

        mObservations = txtObservations.getText().toString();
        if(mObservations.isEmpty()){
            txtObservations.setError(getString(R.string.ticket_field_no_valid));
            txtObservations.requestFocus();
            return false;
        }

//        if(spnProjectStatus.getSpinner().getSelectedItemId() == 0L || projectStatusId == 0L){
//            spnProjectStatus.requestFocus();
//            return false;
//        }

        return true;
    }

    private Ticket createTicket() {
        Ticket newTicket = new Ticket();
        newTicket.setCaption(mCaption);
        newTicket.setApplicantId(applicantId);
        newTicket.setTechnicianId(technicianId);
        newTicket.setCategoryId(categoryId);
        newTicket.setTypeId(typeId);
        newTicket.setSymptomId(databaseManager.getTypeSymptomId(typeId, symptomId));
        newTicket.setDiagnosticId(databaseManager.getSymptomDiagnostic(symptomId, diagnosticId));
        newTicket.setBranchId(branchId);
        newTicket.setPossibleOriginId(possibleOriginId);
        newTicket.setObservations(mObservations);
        newTicket.setProjectStatusId(projectStatusId);
        return newTicket;
    }

    private boolean mayRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            Snackbar.make(txtFile, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, PICK_FILE_REQUEST_CODE);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, PICK_FILE_REQUEST_CODE);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PICK_FILE_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findFile();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                filePath = FileUtil.getRealPathFromURI(mContext, data.getData());
                int cut = filePath.lastIndexOf('/');
                if (cut != -1) {
                    txtFile.setText(filePath.substring(cut + 1));
                }else{
                    txtFile.setText(filePath);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
