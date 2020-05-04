package com.tiendas3b.ticketdoctor.presenters;

import android.app.LoaderManager;
import android.database.Cursor;

/**
 * Created by dfa on 17/02/2016.
 */
public interface LoginPresenter extends LoaderManager.LoaderCallbacks<Cursor> {

    void validateCredentials(String username, String password);

    void onDestroy();

    void validateCredentialsWithoutMd5();
}
