package com.tiendas3b.ticketdoctor.interactors;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.tiendas3b.ticketdoctor.GlobalState;
import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.db.dao.Branch;
import com.tiendas3b.ticketdoctor.db.dao.BranchType;
import com.tiendas3b.ticketdoctor.db.dao.Category;
import com.tiendas3b.ticketdoctor.db.dao.Diagnostic;
import com.tiendas3b.ticketdoctor.db.dao.ImpCause;
import com.tiendas3b.ticketdoctor.db.dao.PossibleOrigin;
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
import com.tiendas3b.ticketdoctor.db.manager.IBranchManager;
import com.tiendas3b.ticketdoctor.db.manager.IBranchTypeManager;
import com.tiendas3b.ticketdoctor.db.manager.ICategoryManager;
import com.tiendas3b.ticketdoctor.db.manager.IDiagnosticManager;
import com.tiendas3b.ticketdoctor.db.manager.IImpCauseManager;
import com.tiendas3b.ticketdoctor.db.manager.IPossibleOriginManager;
import com.tiendas3b.ticketdoctor.db.manager.IProjectStatusManager;
import com.tiendas3b.ticketdoctor.db.manager.IProviderManager;
import com.tiendas3b.ticketdoctor.db.manager.IStandardSolutionManager;
import com.tiendas3b.ticketdoctor.db.manager.ISymptomDiagnosticManager;
import com.tiendas3b.ticketdoctor.db.manager.ISymptomManager;
import com.tiendas3b.ticketdoctor.db.manager.ITicketStatusManager;
import com.tiendas3b.ticketdoctor.db.manager.ITypeSymptomManager;
import com.tiendas3b.ticketdoctor.db.manager.IUserManager;
import com.tiendas3b.ticketdoctor.dto.UserDTO;
import com.tiendas3b.ticketdoctor.http.ServiceGenerator;
import com.tiendas3b.ticketdoctor.http.TicketDoctorService;
import com.tiendas3b.ticketdoctor.listeners.OnDownloadCatalogsFinishedListener;
import com.tiendas3b.ticketdoctor.listeners.OnLoginFinishedListener;
import com.tiendas3b.ticketdoctor.services.SyncCatalogsServices;
import com.tiendas3b.ticketdoctor.util.Encryption;
import com.tiendas3b.ticketdoctor.util.Preferences;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dfa on 17/02/2016.
 */
public class LoginInteractorImpl implements LoginInteractor {

    private static final String TAG = "LoginInteractorImpl";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

//    private ITicketStatusManager databaseManager;

    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {

        if (mAuthTask != null) {
            return;
        }

        listener.resetErrors();
        boolean cancel = false;
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            listener.onPasswordError();
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            listener.onUsernameError();
            cancel = true;
        } else if (!isEmailValid(username)) {
            listener.onUsernameError();
            cancel = true;
        }

        if (!cancel) {
            mAuthTask = new UserLoginTask(username, new Encryption().md5(password), listener);
            mAuthTask.execute(true);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Boolean, Void, Boolean> {

        private final String username;
        private final String password;
        private final OnLoginFinishedListener listener;
        private UserDTO user;
        private Boolean sync;

        UserLoginTask(String username, String password, OnLoginFinishedListener listener) {
            this.username = username;
            this.password = password;
            this.listener = listener;
        }

//        @Override
//        protected void onPreExecute() {
////            listener.showProgress();
//        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            sync = params[0];
            //auth
            try {
                TicketDoctorService loginService = ServiceGenerator.createService(TicketDoctorService.class, username, password);
                Call<UserDTO> call = loginService.login();
                user = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //auth
//            //test
//            user = new User(3L);
//            user.setName("NAME");
//            //test

            return user != null;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mAuthTask = null;
            if (success) {
                if (sync) {
                    listener.onSuccess(user);
                } else {
                    listener.onSuccess2();
                }
            } else {
                if (sync) {
                    listener.onPasswordError();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            listener.hideProgress();
        }
    }

    @Override
    public void savePreferences(Context context, User user) {
        Preferences sp = new Preferences(context);
        sp.saveData(user.getLogin(), user.getPassword(), user.getProfileId());
    }

    @Override
    public void loginWithoutMd5(String login, String password, OnLoginFinishedListener listener) {
        mAuthTask = new UserLoginTask(login, password, listener);
        mAuthTask.execute(false);
    }

    @Override
    public void downloadCatalogs(final GlobalState context, OnDownloadCatalogsFinishedListener listener) {
        Intent msgIntent = new Intent(context, SyncCatalogsServices.class);
//        msgIntent.putExtra(SyncCatalogsServices.PARAM_IN_MSG, strInputMsg);
        context.startService(msgIntent);
//        new AsyncTask<Void, Void, Void>(){
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                downloadStatusCat(context);
//                downloadUsersCat(context);
//                downloadCategoryCat(context);
//                downloadTypesCat(context);
//                downloadProjectStatusCat(context);
//                downloadSymptomCat(context);
//                downloadDiagnosticCat(context);
//                downloadTypeSymptomCat(context);
//                downloadSymptomDiagnosticCat(context);
//                downloadImpCauseCat(context);
//                downloadBranchTypeCat(context);
//                downloadBranchCat(context);
//                downloadStandardSolutionCat(context);
//                downloadPossibleOriginCat(context);
//                downloadProviderCat(context);
//                return null;
//            }
//        }.execute();
//TODO notificar... si quiero...
    }

    private void downloadProviderCat(final GlobalState context) {
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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

//    private ArrayList<TicketStatus> createStatus() {
//        ArrayList<TicketStatus> ticketStatus = new ArrayList<>();
//        TicketStatus ts1 = new TicketStatus(null, "Abierto", null, null, false);
//        TicketStatus ts2 = new TicketStatus(null, "Abierto sin clasificar", null, null, false);
//        TicketStatus ts3 = new TicketStatus(null, "Cerrado", null, null, false);
//        TicketStatus ts4 = new TicketStatus(null, "Esperando a tercero", null, null, false);
//        TicketStatus ts5 = new TicketStatus(null, "Esperando a usuario", null, null, false);
//        TicketStatus ts6 = new TicketStatus(null, "Esperando autorización", null, null, false);
//        TicketStatus ts7 = new TicketStatus(null, "Esperando técnico", null, null, false);
//        TicketStatus ts8 = new TicketStatus(null, "Esperando VoBo", null, null, false);
//        TicketStatus ts9 = new TicketStatus(null, "Inactivo", null, null, false);
//        TicketStatus ts10 = new TicketStatus(null, "Suspendido", null, null, false);
//
//        ticketStatus.add(ts1);
//        ticketStatus.add(ts2);
//        ticketStatus.add(ts3);
//        ticketStatus.add(ts4);
//        ticketStatus.add(ts5);
//        ticketStatus.add(ts6);
//        ticketStatus.add(ts7);
//        ticketStatus.add(ts8);
//        ticketStatus.add(ts9);
//        ticketStatus.add(ts10);
//        return ticketStatus;
//    }

    private void downloadUsersCat(final GlobalState context) {
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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
        TicketDoctorService ticketDoctorService = context.getHttpServiceWithAuth();
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }
}
