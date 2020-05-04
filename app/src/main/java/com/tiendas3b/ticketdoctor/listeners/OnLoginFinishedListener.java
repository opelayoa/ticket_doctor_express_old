package com.tiendas3b.ticketdoctor.listeners;

import com.tiendas3b.ticketdoctor.dto.UserDTO;

/**
 * Created by dfa on 17/02/2016.
 */
public interface OnLoginFinishedListener {

    void onUsernameError();

    void onPasswordError();

    void onSuccess(UserDTO user);

    void hideProgress();

    void showProgress();

    void resetErrors();

    void onSuccess2();
}
