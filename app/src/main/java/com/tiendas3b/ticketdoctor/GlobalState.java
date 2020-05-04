package com.tiendas3b.ticketdoctor;

import android.app.Application;

import com.tiendas3b.ticketdoctor.db.manager.DatabaseManager;
import com.tiendas3b.ticketdoctor.db.manager.IAccessManager;
import com.tiendas3b.ticketdoctor.http.ServiceGenerator;
import com.tiendas3b.ticketdoctor.http.TicketDoctorService;
import com.tiendas3b.ticketdoctor.util.Constants;
import com.tiendas3b.ticketdoctor.util.Preferences;

/**
 * Created by dfa on 17/02/2016.
 */
public class GlobalState extends Application {

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    public TicketDoctorService getHttpServiceWithAuth() {
        Preferences p = new Preferences(this);
        String login = p.getSharedStringSafe(Preferences.KEY_LOGIN, "NO LOGIN");
        String password = p.getSharedStringSafe(Preferences.KEY_PASS, "NO PASS");
        return ServiceGenerator.createService(TicketDoctorService.class, login, password);
    }

    public boolean canDownloadTickets() {
        Preferences preferences = new Preferences(this);
        long profileId = preferences.getSharedLongSafe(Preferences.KEY_PROFILE, -1L);
        IAccessManager databaseManager = new DatabaseManager(this);
        int accessId = databaseManager.getAccessByProfileId(profileId, Constants.NODE_ADMIN);
        databaseManager.closeDbConnections();
        return accessId > 0;
//        return true;
    }

    public boolean canCreateTicket() {
        Preferences preferences = new Preferences(this);
        long profileId = preferences.getSharedLongSafe(Preferences.KEY_PROFILE, -1L);
        IAccessManager databaseManager = new DatabaseManager(this);
        int accessId = databaseManager.getAccessByProfileId(profileId, Constants.NODE_ADD);
        databaseManager.closeDbConnections();
        return accessId > 0;
//        return true;
    }
}
