package com.tiendas3b.ticketdoctor.views;

import java.util.List;

/**
 * Created by dfa on 17/02/2016.
 */
public interface LoginView {

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateToHome();

    void resetErrors();

    void addEmailsToAutoComplete(List<String> emailAddressCollection);
}
