package com.tiendas3b.ticketdoctor.interactors;

import android.content.Context;

import com.tiendas3b.ticketdoctor.GlobalState;
import com.tiendas3b.ticketdoctor.db.dao.User;
import com.tiendas3b.ticketdoctor.listeners.OnDownloadCatalogsFinishedListener;
import com.tiendas3b.ticketdoctor.listeners.OnLoginFinishedListener;

/**
 * Created by dfa on 17/02/2016.
 */
public interface LoginInteractor {

    void login(String username, String password, OnLoginFinishedListener listener);

    void downloadCatalogs(GlobalState context, OnDownloadCatalogsFinishedListener listener);

    void savePreferences(Context mContext, User user);

    void loginWithoutMd5(String login, String password, OnLoginFinishedListener listener);
}
