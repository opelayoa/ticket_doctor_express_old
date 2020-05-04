package com.tiendas3b.ticketdoctor.db.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tiendas3b.ticketdoctor.db.dao.Access;
import com.tiendas3b.ticketdoctor.db.dao.AccessDao;
import com.tiendas3b.ticketdoctor.db.dao.Action;
import com.tiendas3b.ticketdoctor.db.dao.ActionDao;
import com.tiendas3b.ticketdoctor.db.dao.Branch;
import com.tiendas3b.ticketdoctor.db.dao.BranchDao;
import com.tiendas3b.ticketdoctor.db.dao.BranchType;
import com.tiendas3b.ticketdoctor.db.dao.BranchTypeDao;
import com.tiendas3b.ticketdoctor.db.dao.Category;
import com.tiendas3b.ticketdoctor.db.dao.CategoryDao;
import com.tiendas3b.ticketdoctor.db.dao.DaoMaster;
import com.tiendas3b.ticketdoctor.db.dao.DaoSession;
import com.tiendas3b.ticketdoctor.db.dao.Diagnostic;
import com.tiendas3b.ticketdoctor.db.dao.DiagnosticDao;
import com.tiendas3b.ticketdoctor.db.dao.ImpCause;
import com.tiendas3b.ticketdoctor.db.dao.ImpCauseDao;
import com.tiendas3b.ticketdoctor.db.dao.PossibleOrigin;
import com.tiendas3b.ticketdoctor.db.dao.PossibleOriginDao;
import com.tiendas3b.ticketdoctor.db.dao.Profile;
import com.tiendas3b.ticketdoctor.db.dao.ProfileDao;
import com.tiendas3b.ticketdoctor.db.dao.ProjectStatus;
import com.tiendas3b.ticketdoctor.db.dao.ProjectStatusDao;
import com.tiendas3b.ticketdoctor.db.dao.Provider;
import com.tiendas3b.ticketdoctor.db.dao.ProviderDao;
import com.tiendas3b.ticketdoctor.db.dao.StandardSolution;
import com.tiendas3b.ticketdoctor.db.dao.StandardSolutionDao;
import com.tiendas3b.ticketdoctor.db.dao.Symptom;
import com.tiendas3b.ticketdoctor.db.dao.SymptomDao;
import com.tiendas3b.ticketdoctor.db.dao.SymptomDiagnostic;
import com.tiendas3b.ticketdoctor.db.dao.SymptomDiagnosticDao;
import com.tiendas3b.ticketdoctor.db.dao.Ticket;
import com.tiendas3b.ticketdoctor.db.dao.TicketDao;
import com.tiendas3b.ticketdoctor.db.dao.TicketStatus;
import com.tiendas3b.ticketdoctor.db.dao.TicketStatusDao;
import com.tiendas3b.ticketdoctor.db.dao.Type;
import com.tiendas3b.ticketdoctor.db.dao.TypeDao;
import com.tiendas3b.ticketdoctor.db.dao.TypeSymptom;
import com.tiendas3b.ticketdoctor.db.dao.TypeSymptomDao;
import com.tiendas3b.ticketdoctor.db.dao.User;
import com.tiendas3b.ticketdoctor.db.dao.UserDao;
import com.tiendas3b.ticketdoctor.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * @author dfa
 */
public class DatabaseManager implements IDatabaseManager, AsyncOperationListener, ITicketStatusManager, ITicketManager, IUserManager, ICategoryManager, ITypeManager, IProjectStatusManager,
        IProviderManager, ISymptomManager, IDiagnosticManager, ISymptomDiagnosticManager, ITypeSymptomManager, IImpCauseManager, IBranchManager, IBranchTypeManager, IStandardSolutionManager,
        IPossibleOriginManager, IActionManager, IAccessManager, IProfileManager{

    /**
     * Class tag. Used for debug.
     */
    private static final String TAG = DatabaseManager.class.getSimpleName();
    /**
     * Instance of DatabaseManager
     */
    private static DatabaseManager instance;
    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    protected DaoMaster.DevOpenHelper mHelper;
    protected SQLiteDatabase database;
    protected DaoMaster daoMaster;
    protected DaoSession daoSession;
    protected AsyncSession asyncSession;
    protected List<AsyncOperation> completedOperations;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link Context}.
     */
    public DatabaseManager(final Context context) {
        mHelper = new DaoMaster.DevOpenHelper(context, "database", null);
        completedOperations = new CopyOnWriteArrayList<>();
    }

    /**
     * @param context The Android {@link Context}.
     * @return this.instance
     */
    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    private void assertWaitForCompletion1Sec() {
        asyncSession.waitForCompletion(1000);
        asyncSession.isCompleted();
    }

    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    @Override
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public synchronized void dropDatabase() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true); // drops all tables
            mHelper.onCreate(database);              // creates the tables
            asyncSession.deleteAll(Ticket.class);    // clear all elements from a table
            asyncSession.deleteAll(TicketStatus.class);
            asyncSession.deleteAll(Action.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ticket status
     */
    @Override
    public synchronized TicketStatus insertTicketStatus(TicketStatus ticketStatus) {
        try {
            if (ticketStatus != null) {
                openWritableDb();
                TicketStatusDao ticketDao = daoSession.getTicketStatusDao();
                ticketDao.insert(ticketStatus);
                Log.d(TAG, "Inserted ticket");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticketStatus;
    }

    @Override
    public void insertOrReplaceInTxTicketStatus(TicketStatus... ticketStatus) {
        try {
            if (ticketStatus != null) {
                openWritableDb();
                TicketStatusDao actionDao = daoSession.getTicketStatusDao();
                actionDao.insertOrReplaceInTx(ticketStatus);
                Log.d(TAG, "Inserted or replaced ticketStatus");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized ArrayList<TicketStatus> listTicketStatus() {
        List<TicketStatus> tickets = null;
        try {
            openReadableDb();
            TicketStatusDao ticketDao = daoSession.getTicketStatusDao();
            tickets = ticketDao.loadAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tickets != null) {
            return new ArrayList<>(tickets);
        }
        return null;
    }

    @Override
    public synchronized void updateTicketStatus(TicketStatus ticket) {
        try {
            if (ticket != null) {
                openWritableDb();
                daoSession.update(ticket);
                Log.d(TAG, "Updated ticket Status");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteTicketStatus(TicketStatus ticket) {
        deleteTicketStatusById(ticket.getId());
    }

    @Override
    public synchronized boolean deleteTicketStatusById(Long ticketId) {
        try {
            openWritableDb();
            TicketStatusDao ticketDao = daoSession.getTicketStatusDao();
            ticketDao.deleteByKey(ticketId);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized TicketStatus getTicketStatusById(Long ticketId) {
        TicketStatus ticket = null;
        try {
            openReadableDb();
            TicketStatusDao ticketDao = daoSession.getTicketStatusDao();
            ticket = ticketDao.load(ticketId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    @Override
    public synchronized void deleteTicketStatus() {
        try {
            openWritableDb();
            TicketStatusDao ticketDao = daoSession.getTicketStatusDao();
            ticketDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all tickets from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ticket
     */
    @Override
    public synchronized Ticket insertTicket(Ticket ticket) {
        try {
            if (ticket != null) {
                openWritableDb();
                TicketDao ticketDao = daoSession.getTicketDao();
                ticketDao.insert(ticket);
                Log.d(TAG, "Inserted ticket");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    @Override
    public void insertOrReplaceInTxTickets(Ticket... tickets) {
        try {
            if (tickets != null) {
                openWritableDb();
                TicketDao actionDao = daoSession.getTicketDao();
                actionDao.insertOrReplaceInTx(tickets);
                Log.d(TAG, "Inserted or replaced tickets");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized ArrayList<Ticket> listTickets() {
        List<Ticket> tickets = null;
        try {
            openReadableDb();
            QueryBuilder<Ticket> ticketDao = daoSession.getTicketDao().queryBuilder();
            tickets = ticketDao.where(TicketDao.Properties.StatusId.notEq(2))
                    .orderDesc(TicketDao.Properties.Id).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tickets != null) {
            return new ArrayList<>(tickets);
        }
        return null;
    }

    @Override
    public synchronized void updateTicket(Ticket ticket) {
        try {
            if (ticket != null) {
                openWritableDb();
                daoSession.update(ticket);
                Log.d(TAG, "Updated ticket");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteTicket(Ticket ticket) {
        deleteTicketById(ticket.getId());
    }

    @Override
    public synchronized boolean deleteTicketById(Long ticketId) {
        try {
            openWritableDb();
            TicketDao ticketDao = daoSession.getTicketDao();
            ticketDao.deleteByKey(ticketId);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized Ticket getTicketById(Long ticketId) {
        Ticket ticket = null;
        try {
            openReadableDb();
            TicketDao ticketDao = daoSession.getTicketDao();
            ticket = ticketDao.load(ticketId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    @Override
    public synchronized void deleteTickets() {
        try {
            openWritableDb();
            TicketDao ticketDao = daoSession.getTicketDao();
            ticketDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all tickets from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * user
     */
    @Override
    public synchronized User insertUser(User user) {
        try {
            if (user != null) {
                openWritableDb();
                UserDao userDao = daoSession.getUserDao();
                userDao.insert(user);
//                Log.d(TAG, "Inserted user");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void insertOrReplaceInTxUser(User... users) {
        try {
            if (users != null) {
                openWritableDb();
                UserDao actionDao = daoSession.getUserDao();
                actionDao.insertOrReplaceInTx(users);
                Log.d(TAG, "Inserted or replaced users");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<User> listUser() {
        List<User> users = null;
        try {
            openReadableDb();
            QueryBuilder<User> userDao = daoSession.getUserDao().queryBuilder();
            userDao.where(UserDao.Properties.Status.eq(true))
                    .orderAsc(UserDao.Properties.LastName);
            users = userDao.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (users != null) {
            return new ArrayList<>(users);
        }
        return null;
    }

    @Override
    public void updateUser(User user) {
Log.e(TAG, "update user no impl");
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public boolean deleteUserById(Long userId) {
        return false;
    }

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    @Override
    public void deleteUser() {
    }

    @Override
    public ArrayList<User> listApplicants() {
        List<User> users = null;
        try {
            openReadableDb();
            UserDao userDao = daoSession.getUserDao();
//            users = userDao.queryRaw("order by _id DESC");
            users = userDao.loadAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (users != null) {
            return new ArrayList<>(users);
        }
        return null;
    }

    @Override
    public ArrayList<User> listTechnicians() {
        List<User> users = null;
        try {
            openReadableDb();
//            UserDao userDao = daoSession.getUserDao();
//            users = userDao.queryRaw("order by _id DESC");
            QueryBuilder qb = daoSession.getUserDao().queryBuilder();
            qb.where(UserDao.Properties.ProfileId.in(2, 4, 5, 18, 21, 22, 24, 25), UserDao.Properties.Status.eq(true));
            users = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (users != null) {
            return new ArrayList<>(users);
        }
        return null;
    }

    /**
     * category
     */
    @Override
    public Category insertCategory(Category category) {
        try {
            if (category != null) {
                openWritableDb();
                CategoryDao categoryDao = daoSession.getCategoryDao();
                categoryDao.insert(category);
                Log.d(TAG, "Inserted category");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public void insertOrReplaceInTxCategory(Category... categories) {
        try {
            if (categories != null) {
                openWritableDb();
                CategoryDao actionDao = daoSession.getCategoryDao();
                actionDao.insertOrReplaceInTx(categories);
                Log.d(TAG, "Inserted or replaced categories");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * type
     */
    @Override
    public Type insertType(Type type) {
        try {
            if (type != null) {
                openWritableDb();
                TypeDao categoryDao = daoSession.getTypeDao();
                categoryDao.insert(type);
                Log.d(TAG, "Inserted type");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    @Override
    public void insertOrReplaceInTxType(Type... types) {
        try {
            if (types != null) {
                openWritableDb();
                TypeDao actionDao = daoSession.getTypeDao();
                actionDao.insertOrReplaceInTx(types);
                Log.d(TAG, "Inserted or replaced types");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Type> listTypes() {
        List<Type> types = null;
        try {
            openReadableDb();
            QueryBuilder<Type> typeDao = daoSession.getTypeDao().queryBuilder();
            types = typeDao.orderAsc(TypeDao.Properties.Name).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (types != null) {
            return new ArrayList<>(types);
        }
        return null;
    }

    @Override
    public ArrayList<Type> listTypesWithNone(String message) {
        ArrayList<Type> returnList = new ArrayList<>();
        returnList.add(new Type(0L, message, null, null, true));
        List<Type> listDB = null;
        try {
            openReadableDb();
            QueryBuilder<Type> qb = daoSession.getTypeDao().queryBuilder();
            qb.orderAsc(TypeDao.Properties.Name);
            listDB = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listDB != null) {
            returnList.addAll(listDB);
        }
        return returnList;
    }

    /**
     * projectStatus
     */
    @Override
    public ProjectStatus insertProjectStatus(ProjectStatus projectStatus) {
        try {
            if (projectStatus != null) {
                openWritableDb();
                ProjectStatusDao projectStatusDao = daoSession.getProjectStatusDao();
                projectStatusDao.insert(projectStatus);
                Log.d(TAG, "Inserted projectStatus");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectStatus;
    }

    @Override
    public void insertOrReplaceInTxProjectStatus(ProjectStatus... projectStatus) {
        try {
            if (projectStatus != null) {
                openWritableDb();
                ProjectStatusDao actionDao = daoSession.getProjectStatusDao();
                actionDao.insertOrReplaceInTx(projectStatus);
                Log.d(TAG, "Inserted or replaced projectStatus");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<ProjectStatus> listProjectStatuses() {
        List<ProjectStatus> possibleOrigins = null;
        try {
            openReadableDb();
            QueryBuilder<ProjectStatus> qb = daoSession.getProjectStatusDao().queryBuilder();
            qb.orderAsc(ProjectStatusDao.Properties.Name);
            possibleOrigins = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (possibleOrigins != null) {
            return new ArrayList<>(possibleOrigins);
        }
        return null;
    }

    @Override
    public ArrayList<ProjectStatus> listProjectStatusesWithNone(String message) {
        ArrayList<ProjectStatus> projectStatuses = new ArrayList<>();
        projectStatuses.add(new ProjectStatus(0L, message, null, true));
        List<ProjectStatus> projectStatusesDB = null;
        try {
            openReadableDb();
            QueryBuilder<ProjectStatus> qb = daoSession.getProjectStatusDao().queryBuilder();
            qb.orderAsc(ProjectStatusDao.Properties.Name);
            projectStatusesDB = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (projectStatusesDB != null) {
            projectStatuses.addAll(projectStatusesDB);
        }
        return projectStatuses;
    }

    /**
     * provider
     */
    @Override
    public Provider insertProvider(Provider provider) {
        try {
            if (provider != null) {
                openWritableDb();
                ProviderDao providerDao = daoSession.getProviderDao();
                providerDao.insert(provider);
                Log.d(TAG, "Inserted provider");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    @Override
    public void insertOrReplaceInTxProvider(Provider... providers) {
        try {
            if (providers != null) {
                openWritableDb();
                ProviderDao actionDao = daoSession.getProviderDao();
                actionDao.insertOrReplaceInTx(providers);
                Log.d(TAG, "Inserted or replaced providers");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Provider> listProviders() {
        List<Provider> providers = null;
        try {
            openReadableDb();
            QueryBuilder<Provider> qb = daoSession.getProviderDao().queryBuilder();
//            qb.where(ProviderDao.Properties.Status.eq(symptomId));
            qb.orderAsc(ProviderDao.Properties.BusinessName);
            providers = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (providers != null) {
            return new ArrayList<>(providers);
        }
        return null;
    }

    /**
     * diagnostic
     */
    @Override
    public Diagnostic insertDiagnostic(Diagnostic diagnostic) {
        try {
            if (diagnostic != null) {
                openWritableDb();
                DiagnosticDao diagnosticDao = daoSession.getDiagnosticDao();
                diagnosticDao.insert(diagnostic);
                Log.d(TAG, "Inserted diagnostic");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diagnostic;
    }

    @Override
    public void insertOrReplaceInTxDiagnostic(Diagnostic... diagnostics) {
        try {
            if (diagnostics != null) {
                openWritableDb();
                DiagnosticDao actionDao = daoSession.getDiagnosticDao();
                actionDao.insertOrReplaceInTx(diagnostics);
                Log.d(TAG, "Inserted or replaced diagnostics");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Diagnostic> listDiagnostics(long symptomId) {
        List<Diagnostic> symptoms = null;
        try {
            openReadableDb();
            QueryBuilder<Diagnostic> qb = daoSession.getDiagnosticDao().queryBuilder();
            qb.join(SymptomDiagnostic.class, SymptomDiagnosticDao.Properties.DiagnosticId)
                    .where(SymptomDiagnosticDao.Properties.SymptomId.eq(symptomId));
            symptoms = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (symptoms != null) {
            return new ArrayList<>(symptoms);
        }
        return null;
    }

    /**
     * symptomDiagnostic
     */
    @Override
    public SymptomDiagnostic insertSymptomDiagnostic(SymptomDiagnostic symptomDiagnostic) {
        try {
            if (symptomDiagnostic != null) {
                openWritableDb();
                SymptomDiagnosticDao symptomDiagnosticDao = daoSession.getSymptomDiagnosticDao();
                symptomDiagnosticDao.insert(symptomDiagnostic);
                Log.d(TAG, "Inserted symptomDiagnostic");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return symptomDiagnostic;
    }

    @Override
    public void insertOrReplaceInTxSymptomDiagnostic(SymptomDiagnostic... symptomDiagnostics) {
        try {
            if (symptomDiagnostics != null) {
                openWritableDb();
                SymptomDiagnosticDao actionDao = daoSession.getSymptomDiagnosticDao();
                actionDao.insertOrReplaceInTx(symptomDiagnostics);
                Log.d(TAG, "Inserted or replaced symptomDiagnostics");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long getSymptomDiagnostic(long symptomId, long diagnosticId) {
        SymptomDiagnostic symptomDiagnostic = null;
        try {
            openReadableDb();
//            UserDao userDao = daoSession.getUserDao();
//            users = userDao.queryRaw("order by _id DESC");
            QueryBuilder<SymptomDiagnostic> qb = daoSession.getSymptomDiagnosticDao().queryBuilder();
            qb.where(SymptomDiagnosticDao.Properties.DiagnosticId.eq(diagnosticId), SymptomDiagnosticDao.Properties.SymptomId.eq(symptomId));
            symptomDiagnostic = qb.list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (symptomDiagnostic != null) {
            return symptomDiagnostic.getId();
        }
        return -1L;
    }

    /**
     * symptom
     */
    @Override
    public Symptom insertSymptom(Symptom symptom) {
        try {
            if (symptom != null) {
                openWritableDb();
                SymptomDao symptomDao = daoSession.getSymptomDao();
                symptomDao.insert(symptom);
                Log.d(TAG, "Inserted symptom");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return symptom;
    }

    @Override
    public void insertOrReplaceInTxSymptom(Symptom... symptoms) {
        try {
            if (symptoms != null) {
                openWritableDb();
                SymptomDao actionDao = daoSession.getSymptomDao();
                actionDao.insertOrReplaceInTx(symptoms);
                Log.d(TAG, "Inserted or replaced symptoms");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Symptom> listSymptoms(long typeId) {
        List<Symptom> symptoms = null;
        try {
            openReadableDb();
//            UserDao userDao = daoSession.getUserDao();
//            users = userDao.queryRaw("order by _id DESC");
            QueryBuilder<Symptom> qb = daoSession.getSymptomDao().queryBuilder();
            qb.join(TypeSymptom.class, TypeSymptomDao.Properties.SymptomId)
                    .where(TypeSymptomDao.Properties.TypeId.eq(typeId));
            symptoms = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (symptoms != null) {
            return new ArrayList<>(symptoms);
        }
        return null;
    }

    /**
     * typeSymptom
     */
    @Override
    public TypeSymptom insertTypeSymptom(TypeSymptom typeSymptom) {
        try {
            if (typeSymptom != null) {
                openWritableDb();
                TypeSymptomDao typeSymptomDao = daoSession.getTypeSymptomDao();
                typeSymptomDao.insert(typeSymptom);
                Log.d(TAG, "Inserted typeSymptom");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeSymptom;
    }

    @Override
    public void insertOrReplaceInTxTypeSymptom(TypeSymptom... typeSymptoms) {
        try {
            if (typeSymptoms != null) {
                openWritableDb();
                TypeSymptomDao actionDao = daoSession.getTypeSymptomDao();
                actionDao.insertOrReplaceInTx(typeSymptoms);
                Log.d(TAG, "Inserted or replaced typeSymptoms");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<TypeSymptom> listTypeSymptoms(long typeId) {
        List<TypeSymptom> typeSymptoms = null;
        try {
            openReadableDb();
//            UserDao userDao = daoSession.getUserDao();
//            users = userDao.queryRaw("order by _id DESC");
            QueryBuilder<TypeSymptom> qb = daoSession.getTypeSymptomDao().queryBuilder();
            qb.where(TypeSymptomDao.Properties.TypeId.eq(typeId));
            typeSymptoms = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (typeSymptoms != null) {
            return new ArrayList<>(typeSymptoms);
        }
        return null;
    }

    @Override
    public long getTypeSymptomId(long typeId, long symptomId) {
        TypeSymptom typeSymptoms = null;
        try {
            openReadableDb();
//            UserDao userDao = daoSession.getUserDao();
//            users = userDao.queryRaw("order by _id DESC");
            QueryBuilder<TypeSymptom> qb = daoSession.getTypeSymptomDao().queryBuilder();
            qb.where(TypeSymptomDao.Properties.TypeId.eq(typeId), TypeSymptomDao.Properties.SymptomId.eq(symptomId));
            typeSymptoms = qb.list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (typeSymptoms != null) {
            return typeSymptoms.getId();
        }
        return -1L;
    }

    /**
     * impCause
     */
    @Override
    public ImpCause insertImpCause(ImpCause impCause) {
        try {
            if (impCause != null) {
                openWritableDb();
                ImpCauseDao typeSymptomDao = daoSession.getImpCauseDao();
                typeSymptomDao.insert(impCause);
                Log.d(TAG, "Inserted impCause");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return impCause;
    }

    @Override
    public void insertOrReplaceInTxImpCause(ImpCause... impCauses) {
        try {
            if (impCauses != null) {
                openWritableDb();
                ImpCauseDao actionDao = daoSession.getImpCauseDao();
                actionDao.insertOrReplaceInTx(impCauses);
                Log.d(TAG, "Inserted or replaced impCauses");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<ImpCause> listCauses() {
        List<ImpCause> branches = null;
        try {
            openReadableDb();
            QueryBuilder<ImpCause> qb = daoSession.getImpCauseDao().queryBuilder();
            qb.orderAsc(ImpCauseDao.Properties.Description);
            branches = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (branches != null) {
            return new ArrayList<>(branches);
        }
        return null;
    }

    /**
     * branch
     */
    @Override
    public Branch insertBranch(Branch branch) {
        try {
            if (branch != null) {
                openWritableDb();
                BranchDao branchDao = daoSession.getBranchDao();
                branchDao.insert(branch);
                Log.d(TAG, "Inserted branch");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branch;
    }

    @Override
    public void insertOrReplaceInTxBranch(Branch... branches) {
        try {
            if (branches != null) {
                openWritableDb();
                BranchDao actionDao = daoSession.getBranchDao();
                actionDao.insertOrReplaceInTx(branches);
                Log.d(TAG, "Inserted or replaced branches");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Branch> listBranches() {
        List<Branch> branches = null;
        try {
            openReadableDb();
//            UserDao userDao = daoSession.getUserDao();
//            users = userDao.queryRaw("order by _id DESC");
            QueryBuilder qb = daoSession.getBranchDao().queryBuilder();
//            qb.where(UserDao.Properties.ProfileId.in(2, 4, 5, 18, 21, 22, 24, 25), UserDao.Properties.Status.eq(true));
            qb.orderAsc(BranchDao.Properties.Code);
            branches = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (branches != null) {
            return new ArrayList<>(branches);
        }
        return null;
    }

    /**
     * branchType
     */
    @Override
    public BranchType insertBranchType(BranchType branchType) {
        try {
            if (branchType != null) {
                openWritableDb();
                BranchTypeDao branchTypeDao = daoSession.getBranchTypeDao();
                branchTypeDao.insert(branchType);
                Log.d(TAG, "Inserted branchType");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branchType;
    }

    @Override
    public void insertOrReplaceInTxBranchType(BranchType... branchTypes) {
        try {
            if (branchTypes != null) {
                openWritableDb();
                BranchTypeDao actionDao = daoSession.getBranchTypeDao();
                actionDao.insertOrReplaceInTx(branchTypes);
                Log.d(TAG, "Inserted or replaced branchTypes");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * standardSolution
     */
    @Override
    public StandardSolution insertStandardSolution(StandardSolution standardSolution) {
        try {
            if (standardSolution != null) {
                openWritableDb();
                StandardSolutionDao standardSolutionDao = daoSession.getStandardSolutionDao();
                standardSolutionDao.insert(standardSolution);
                Log.d(TAG, "Inserted standardSolution");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return standardSolution;
    }

    @Override
    public void insertOrReplaceInTxStandardSolution(StandardSolution... standardSolutions) {
        try {
            if (standardSolutions != null) {
                openWritableDb();
                StandardSolutionDao actionDao = daoSession.getStandardSolutionDao();
                actionDao.insertOrReplaceInTx(standardSolutions);
                Log.d(TAG, "Inserted or replaced standardSolutions");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * possibleOrigin
     */
    @Override
    public PossibleOrigin insertPossibleOrigin(PossibleOrigin possibleOrigin) {
        try {
            if (possibleOrigin != null) {
                openWritableDb();
                PossibleOriginDao possibleOriginDao = daoSession.getPossibleOriginDao();
                possibleOriginDao.insert(possibleOrigin);
                Log.d(TAG, "Inserted possibleOrigin");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return possibleOrigin;
    }

    @Override
    public void insertOrReplaceInTxPossibleOrigin(PossibleOrigin... possibleOrigins) {
        try {
            if (possibleOrigins != null) {
                openWritableDb();
                PossibleOriginDao actionDao = daoSession.getPossibleOriginDao();
                actionDao.insertOrReplaceInTx(possibleOrigins);
                Log.d(TAG, "Inserted or replaced possibleOrigins");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<PossibleOrigin> listPossibleOrigins() {
        List<PossibleOrigin> possibleOrigins = null;
        try {
            openReadableDb();
            QueryBuilder<PossibleOrigin> qb = daoSession.getPossibleOriginDao().queryBuilder();
            qb.orderAsc(PossibleOriginDao.Properties.Name);
            possibleOrigins = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (possibleOrigins != null) {
            return new ArrayList<>(possibleOrigins);
        }
        return null;
    }

    @Override
    public ArrayList<PossibleOrigin> listPossibleOriginsWithNone(String name) {
        ArrayList<PossibleOrigin> possibleOrigins = new ArrayList<>();
        possibleOrigins.add(new PossibleOrigin(0L, name, null));
        List<PossibleOrigin> possibleOriginsDB = null;
        try {
            openReadableDb();
            QueryBuilder<PossibleOrigin> qb = daoSession.getPossibleOriginDao().queryBuilder();
            qb.orderAsc(PossibleOriginDao.Properties.Name);
            possibleOriginsDB = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (possibleOriginsDB != null) {
            possibleOrigins.addAll(possibleOriginsDB);
        }
        return possibleOrigins;
    }

    /**
     * action
     */
    @Override
    public Action insertAction(Action action) {
        try {
            if (action != null) {
                openWritableDb();
                ActionDao actionDao = daoSession.getActionDao();
                actionDao.insert(action);
                Log.d(TAG, "Inserted action");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return action;
    }

    @Override
    public List<Action> getActionsByTicketId(Long id) {
        List<Action> tickets = null;
        try {
            openReadableDb();
            ActionDao ticketDao = daoSession.getActionDao();
            tickets = ticketDao._queryTicket_Actions(id);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tickets != null) {
            return new ArrayList<>(tickets);
        }
        return null;
    }

    @Override
    public void insertOrReplaceInTxActions(Action... actions) {
        try {
            if (actions != null) {
                openWritableDb();
                ActionDao actionDao = daoSession.getActionDao();
                actionDao.insertOrReplaceInTx(actions);
                Log.d(TAG, "Inserted or replaced actions");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Access
     */
    @Override
    public int getAccessByProfileId(Long profileId, String action) {
        List<Access> access = null;
        try {
            openReadableDb();
//            UserDao userDao = daoSession.getUserDao();
//            users = userDao.queryRaw("order by _id DESC");
            QueryBuilder<Access> qb = daoSession.getAccessDao().queryBuilder();
            qb.where(AccessDao.Properties.ProfileId.eq(profileId), AccessDao.Properties.Action.eq(action),
                    qb.or(AccessDao.Properties.Node.eq(Constants.TICKETS_OPEN_TICKET), AccessDao.Properties.Node.eq(Constants.REGIONAL_TICKETS_OPEN_TICKET)));
            access = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (access != null) {
            return access.size();
        }
        return -1;
    }

    @Override
    public void insertOrReplaceInTxAccesses(Access... accesses) {
        try {
            if (accesses != null) {
                openWritableDb();
                AccessDao accessDao = daoSession.getAccessDao();
                accessDao.insertOrReplaceInTx(accesses);
                Log.d(TAG, "Inserted or replaced accesses");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Profile
     */
    @Override
    public List<Profile> getProfileByUserId(Long id) {
        return null;
    }

    @Override
    public void insertOrReplaceInTxProfiles(Profile... profiles) {
        try {
            if (profiles != null) {
                openWritableDb();
                ProfileDao profileDao = daoSession.getProfileDao();
                profileDao.insertOrReplaceInTx(profiles);
                Log.d(TAG, "Inserted or replaced profiles");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Ticket> listTickets(long status) {
        List<Ticket> tickets = null;
        try {
            openReadableDb();
            QueryBuilder<Ticket> ticketDao = daoSession.getTicketDao().queryBuilder();
            tickets = ticketDao.where(TicketDao.Properties.StatusId.eq(status))
            .orderDesc(TicketDao.Properties.Id).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tickets != null) {
            return new ArrayList<>(tickets);
        }
        return null;
    }
}
