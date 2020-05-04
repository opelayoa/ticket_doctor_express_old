package com.tiendas3b.ticketdoctor.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tiendas3b.ticketdoctor.GlobalState;
import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Access;
import com.tiendas3b.ticketdoctor.db.dao.Branch;
import com.tiendas3b.ticketdoctor.db.dao.BranchType;
import com.tiendas3b.ticketdoctor.db.dao.Category;
import com.tiendas3b.ticketdoctor.db.dao.Diagnostic;
import com.tiendas3b.ticketdoctor.db.dao.ImpCause;
import com.tiendas3b.ticketdoctor.db.dao.PossibleOrigin;
import com.tiendas3b.ticketdoctor.db.dao.Profile;
import com.tiendas3b.ticketdoctor.db.dao.ProjectStatus;
import com.tiendas3b.ticketdoctor.db.dao.Provider;
import com.tiendas3b.ticketdoctor.db.dao.StandardSolution;
import com.tiendas3b.ticketdoctor.db.dao.Symptom;
import com.tiendas3b.ticketdoctor.db.dao.SymptomDiagnostic;
import com.tiendas3b.ticketdoctor.db.dao.TicketStatus;
import com.tiendas3b.ticketdoctor.db.dao.Type;
import com.tiendas3b.ticketdoctor.db.dao.TypeSymptom;
import com.tiendas3b.ticketdoctor.db.dao.User;
import com.tiendas3b.ticketdoctor.db.manager.DatabaseManager;
import com.tiendas3b.ticketdoctor.db.manager.IAccessManager;
import com.tiendas3b.ticketdoctor.db.manager.IBranchManager;
import com.tiendas3b.ticketdoctor.db.manager.IBranchTypeManager;
import com.tiendas3b.ticketdoctor.db.manager.ICategoryManager;
import com.tiendas3b.ticketdoctor.db.manager.IDiagnosticManager;
import com.tiendas3b.ticketdoctor.db.manager.IImpCauseManager;
import com.tiendas3b.ticketdoctor.db.manager.IPossibleOriginManager;
import com.tiendas3b.ticketdoctor.db.manager.IProfileManager;
import com.tiendas3b.ticketdoctor.db.manager.IProjectStatusManager;
import com.tiendas3b.ticketdoctor.db.manager.IProviderManager;
import com.tiendas3b.ticketdoctor.db.manager.IStandardSolutionManager;
import com.tiendas3b.ticketdoctor.db.manager.ISymptomDiagnosticManager;
import com.tiendas3b.ticketdoctor.db.manager.ISymptomManager;
import com.tiendas3b.ticketdoctor.db.manager.ITicketStatusManager;
import com.tiendas3b.ticketdoctor.db.manager.ITypeSymptomManager;
import com.tiendas3b.ticketdoctor.db.manager.IUserManager;
import com.tiendas3b.ticketdoctor.http.TicketDoctorService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SyncCatalogsServices extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.tiendas3b.ticketdoctor.services.action.FOO";
    private static final String ACTION_BAZ = "com.tiendas3b.ticketdoctor.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.tiendas3b.ticketdoctor.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.tiendas3b.ticketdoctor.services.extra.PARAM2";
    private static final String TAG = "SyncCatalogsServices";
    private TicketDoctorService ticketDoctorService;

    public SyncCatalogsServices() {
        super("SyncCatalogsServices");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncCatalogsServices.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncCatalogsServices.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFoo(param1, param2);
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }

            GlobalState context = (GlobalState) getApplicationContext();
            ticketDoctorService = context.getHttpServiceWithAuth();

            downloadStatusCat(context);
            downloadUsersCat(context);
            downloadCategoryCat(context);
            downloadTypesCat(context);
            downloadProjectStatusCat(context);
            downloadSymptomCat(context);
            downloadDiagnosticCat(context);
            downloadTypeSymptomCat(context);
            downloadSymptomDiagnosticCat(context);
            downloadImpCauseCat(context);
            downloadBranchTypeCat(context);
            downloadBranchCat(context);
            downloadStandardSolutionCat(context);
            downloadPossibleOriginCat(context);
            downloadProviderCat(context);
            downloadProfileCat(context);
            downloadAccessCat(context);
        }
    }

    private void downloadAccessCat(final GlobalState context) {
        Call<List<Access>> call = ticketDoctorService.getAccessCatalog();
        call.enqueue(new Callback<List<Access>>() {
            @Override
            public void onResponse(Call<List<Access>> call, Response<List<Access>> response) {
                if (response.isSuccessful()) {
                    saveAccess(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<Access>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_provider));
            }
        });
    }

    private void saveAccess(List<Access> accesses, GlobalState context) {
        IAccessManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxAccesses(accesses.toArray(new Access[accesses.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadProfileCat(final GlobalState context) {
        Call<List<Profile>> call = ticketDoctorService.getProfileCatalog();
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if (response.isSuccessful()) {
                    saveProfile(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_provider));
            }
        });
    }

    private void saveProfile(List<Profile> profiles, GlobalState context) {
        IProfileManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxProfiles(profiles.toArray(new Profile[profiles.size()]));
        databaseManager.closeDbConnections();
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }




    private void downloadProviderCat(final GlobalState context) {
        Call<List<Provider>> call = ticketDoctorService.getProviderCatalog();
        call.enqueue(new Callback<List<Provider>>() {
            @Override
            public void onResponse(Call<List<Provider>> call, Response<List<Provider>> response) {
                if (response.isSuccessful()) {
                    saveProvider(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<Provider>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_provider));
            }
        });
    }

    private void saveProvider(List<Provider> providers, GlobalState context) {
        IProviderManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxProvider(providers.toArray(new Provider[providers.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadPossibleOriginCat(final GlobalState context) {
        Call<List<PossibleOrigin>> call = ticketDoctorService.getPossibleOriginCatalog();
        call.enqueue(new Callback<List<PossibleOrigin>>() {
            @Override
            public void onResponse(Call<List<PossibleOrigin>> call, Response<List<PossibleOrigin>> response) {
                if (response.isSuccessful()) {
                    savePossibleOrigin(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<PossibleOrigin>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_possible_origin));
            }
        });
    }

    private void savePossibleOrigin(List<PossibleOrigin> possibleOrigins, GlobalState context) {
        IPossibleOriginManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxPossibleOrigin(possibleOrigins.toArray(new PossibleOrigin[possibleOrigins.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadStandardSolutionCat(final GlobalState context) {
        Call<List<StandardSolution>> call = ticketDoctorService.getStandardSolutionCatalog();
        call.enqueue(new Callback<List<StandardSolution>>() {
            @Override
            public void onResponse(Call<List<StandardSolution>> call, Response<List<StandardSolution>> response) {
                if (response.isSuccessful()) {
                    saveStandardSolution(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<StandardSolution>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_standard_solution));
            }
        });
    }

    private void saveStandardSolution(List<StandardSolution> standardSolutions, GlobalState context) {
        IStandardSolutionManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxStandardSolution(standardSolutions.toArray(new StandardSolution[standardSolutions.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadBranchCat(final GlobalState context) {
        Call<List<Branch>> call = ticketDoctorService.getBranchCatalog();
        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                if (response.isSuccessful()) {
                    saveBranch(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_branch));
            }
        });
    }

    private void saveBranch(List<Branch> branches, GlobalState context) {
        IBranchManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxBranch(branches.toArray(new Branch[branches.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadBranchTypeCat(final GlobalState context) {
        Call<List<BranchType>> call = ticketDoctorService.getBranchTypeCatalog();
        call.enqueue(new Callback<List<BranchType>>() {
            @Override
            public void onResponse(Call<List<BranchType>> call, Response<List<BranchType>> response) {
                if (response.isSuccessful()) {
                    saveBranchType(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<BranchType>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_branch_type));
            }
        });
    }

    private void saveBranchType(List<BranchType> branchTypes, GlobalState context) {
        IBranchTypeManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxBranchType(branchTypes.toArray(new BranchType[branchTypes.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadImpCauseCat(final GlobalState context) {
        Call<List<ImpCause>> call = ticketDoctorService.getImpCauseCatalog();
        call.enqueue(new Callback<List<ImpCause>>() {
            @Override
            public void onResponse(Call<List<ImpCause>> call, Response<List<ImpCause>> response) {
                if (response.isSuccessful()) {
                    saveImpCause(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<ImpCause>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_imp_cause));
            }
        });
    }

    private void saveImpCause(List<ImpCause> impCauses, GlobalState context) {
        IImpCauseManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxImpCause(impCauses.toArray(new ImpCause[impCauses.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadSymptomDiagnosticCat(final GlobalState context) {
        Log.i("TAG", "downloadSymptomDiagnosticCat");
        Call<List<SymptomDiagnostic>> call = ticketDoctorService.getSymptomDiagnosticCatalog();
        call.enqueue(new Callback<List<SymptomDiagnostic>>() {
            @Override
            public void onResponse(Call<List<SymptomDiagnostic>> call, Response<List<SymptomDiagnostic>> response) {
                if (response.isSuccessful()) {
                    saveSymptomDiagnostics(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<SymptomDiagnostic>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_symptom_diagnostic));
            }
        });
    }

    private void saveSymptomDiagnostics(List<SymptomDiagnostic> symptomDiagnostics, GlobalState context) {
        ISymptomDiagnosticManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxSymptomDiagnostic(symptomDiagnostics.toArray(new SymptomDiagnostic[symptomDiagnostics.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadTypeSymptomCat(final GlobalState context) {
        Call<List<TypeSymptom>> call = ticketDoctorService.getTypeSymptomCatalog();
        call.enqueue(new Callback<List<TypeSymptom>>() {
            @Override
            public void onResponse(Call<List<TypeSymptom>> call, Response<List<TypeSymptom>> response) {
                if (response.isSuccessful()) {
                    saveTypeSymptoms(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<TypeSymptom>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_type_symptoms));
            }
        });
    }

    private void saveTypeSymptoms(List<TypeSymptom> typeSymptoms, GlobalState context) {
        ITypeSymptomManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxTypeSymptom(typeSymptoms.toArray(new TypeSymptom[typeSymptoms.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadDiagnosticCat(final GlobalState context) {
        Call<List<Diagnostic>> call = ticketDoctorService.getDiagnosticCatalog();
        call.enqueue(new Callback<List<Diagnostic>>() {
            @Override
            public void onResponse(Call<List<Diagnostic>> call, Response<List<Diagnostic>> response) {
                if (response.isSuccessful()) {
                    saveDiagnostics(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<Diagnostic>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_diagnostics));
            }
        });
    }

    private void saveDiagnostics(List<Diagnostic> diagnostics, GlobalState context) {
        IDiagnosticManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxDiagnostic(diagnostics.toArray(new Diagnostic[diagnostics.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadSymptomCat(final GlobalState context) {
        Call<List<Symptom>> call = ticketDoctorService.getSymptomCatalog();
        call.enqueue(new Callback<List<Symptom>>() {
            @Override
            public void onResponse(Call<List<Symptom>> call, Response<List<Symptom>> response) {
                if (response.isSuccessful()) {
                    saveSymptoms(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<Symptom>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_symptoms));
            }
        });
    }

    private void saveSymptoms(List<Symptom> symptoms, GlobalState context) {
        ISymptomManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxSymptom(symptoms.toArray(new Symptom[symptoms.size()]));
        databaseManager.closeDbConnections();
    }

    private void downloadStatusCat(final GlobalState context) {
        Log.i("TAG", "downloadStatusCat");

        Call<List<TicketStatus>> call = ticketDoctorService.getTicketStatusCatalog();
        call.enqueue(new Callback<List<TicketStatus>>() {
            @Override
            public void onResponse(Call<List<TicketStatus>> call, Response<List<TicketStatus>> response) {
                if (response.isSuccessful()) {
                    saveTicketStatus(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<TicketStatus>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_ticket_status));
            }
        });
    }

    private void saveTicketStatus(List<TicketStatus> ticketStatuses, GlobalState context) {
        ITicketStatusManager databaseManager = new DatabaseManager(context);
//        for (TicketStatus ticketStatus : ticketStatuses){
//            databaseManager.insertTicketStatus(ticketStatus);
        databaseManager.insertOrReplaceInTxTicketStatus(ticketStatuses.toArray(new TicketStatus[ticketStatuses.size()]));
//        }
        databaseManager.closeDbConnections();
    }

    private void downloadUsersCat(final GlobalState context) {
        Call<List<User>> call = ticketDoctorService.getUsersCatalog();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    saveUsers(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_users));
            }
        });
    }

    private void saveUsers(List<User> users, Context context) {
        IUserManager databaseManager = new DatabaseManager(context);
//        for (User user : users){
//            databaseManager.insertUser(user);
        databaseManager.insertOrReplaceInTxUser(users.toArray(new User[users.size()]));
//        }
        databaseManager.closeDbConnections();
    }

    private void downloadCategoryCat(final GlobalState context) {
        Call<List<Category>> call = ticketDoctorService.getCategoriesCatalog();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    saveCategories(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_categories));
            }
        });
    }

    private void saveCategories(List<Category> categories, GlobalState context) {
        ICategoryManager databaseManager = new DatabaseManager(context);
//        for (Category category : categories){
        databaseManager.insertOrReplaceInTxCategory(categories.toArray(new Category[categories.size()]));
//        }
        databaseManager.closeDbConnections();
    }

    private void downloadTypesCat(final GlobalState context) {
        Call<List<Type>> call = ticketDoctorService.getTypesCatalog();
        call.enqueue(new Callback<List<Type>>() {
            @Override
            public void onResponse(Call<List<Type>> call, Response<List<Type>> response) {
                if (response.isSuccessful()) {
                    saveTypes(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<Type>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_categories));
            }
        });
    }

    private void saveTypes(List<Type> types, GlobalState context) {
        DatabaseManager databaseManager = new DatabaseManager(context);
//        for (Type type : types){
        databaseManager.insertOrReplaceInTxType(types.toArray(new Type[types.size()]));
//        }
        databaseManager.closeDbConnections();
    }

    private void downloadProjectStatusCat(final GlobalState context) {
        Call<List<ProjectStatus>> call = ticketDoctorService.getProjectStatusCatalog();
        call.enqueue(new Callback<List<ProjectStatus>>() {
            @Override
            public void onResponse(Call<List<ProjectStatus>> call, Response<List<ProjectStatus>> response) {
                if (response.isSuccessful()) {
                    saveProjectStatus(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<List<ProjectStatus>> call, Throwable t) {
                Log.e(TAG, context.getString(R.string.error_login_categories));
            }
        });
    }

    private void saveProjectStatus(List<ProjectStatus> projectStatuses, GlobalState context) {
        IProjectStatusManager databaseManager = new DatabaseManager(context);
//        for (ProjectStatus projectStatus : projectStatuses){
        databaseManager.insertOrReplaceInTxProjectStatus(projectStatuses.toArray(new ProjectStatus[projectStatuses.size()]));
//        }
        databaseManager.closeDbConnections();
    }
}
