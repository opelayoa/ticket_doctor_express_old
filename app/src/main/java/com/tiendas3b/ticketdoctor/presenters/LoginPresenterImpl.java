package com.tiendas3b.ticketdoctor.presenters;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.tiendas3b.ticketdoctor.GlobalState;
import com.tiendas3b.ticketdoctor.db.dao.Access;
import com.tiendas3b.ticketdoctor.db.manager.DatabaseManager;
import com.tiendas3b.ticketdoctor.db.manager.IAccessManager;
import com.tiendas3b.ticketdoctor.dto.UserDTO;
import com.tiendas3b.ticketdoctor.interactors.LoginInteractor;
import com.tiendas3b.ticketdoctor.interactors.LoginInteractorImpl;
import com.tiendas3b.ticketdoctor.listeners.OnDownloadCatalogsFinishedListener;
import com.tiendas3b.ticketdoctor.listeners.OnLoginFinishedListener;
import com.tiendas3b.ticketdoctor.util.Preferences;
import com.tiendas3b.ticketdoctor.views.LoginView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfa on 17/02/2016.
 */
public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener, OnDownloadCatalogsFinishedListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;
    private GlobalState mContext;

    public LoginPresenterImpl(LoginView loginView, GlobalState context) {
        this.loginView = loginView;
        this.mContext = context;
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void validateCredentials(String username, String password) {
        if (loginView != null) {
            showProgress();
        }
        loginInteractor.login(username, password, this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void validateCredentialsWithoutMd5() {
        Preferences p = new Preferences(mContext);
        String login = p.getSharedStringSafe(Preferences.KEY_LOGIN, "");
        String password = p.getSharedStringSafe(Preferences.KEY_PASS, "");
        if (!"".equals(login)) {
            loginInteractor.loginWithoutMd5(login, password, this);
        }
    }

    @Override
    public void onUsernameError() {
        if (loginView != null) {
            loginView.setUsernameError();
            hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if (loginView != null) {
            loginView.setPasswordError();
            hideProgress();
        }
    }

    @Override
    public void onError() {
        //TODO
    }

    @Override
    public void onDownloadCatalogs() {
        //TODO
    }

    @Override
    public void onSuccess(UserDTO user) {
        if (loginView != null) {
            saveAccess(user.getAccesses(), mContext);
            loginView.navigateToHome();
            loginInteractor.savePreferences(mContext, user.getUser());
            loginInteractor.downloadCatalogs(mContext, this);
        }
    }

    private void saveAccess(List<Access> accesses, GlobalState context) {
        IAccessManager databaseManager = new DatabaseManager(context);
        databaseManager.insertOrReplaceInTxAccesses(accesses.toArray(new Access[accesses.size()]));
        databaseManager.closeDbConnections();
    }

    @Override
    public void hideProgress() {
        loginView.hideProgress();
    }

    @Override
    public void showProgress() {
        loginView.showProgress();
    }

    @Override
    public void resetErrors() {
        loginView.resetErrors();
    }

    @Override
    public void onSuccess2() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(mContext,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY),

                ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        loginView.addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS,
//                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}
