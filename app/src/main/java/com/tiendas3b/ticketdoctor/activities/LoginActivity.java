package com.tiendas3b.ticketdoctor.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiendas3b.ticketdoctor.GlobalState;
import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.presenters.LoginPresenter;
import com.tiendas3b.ticketdoctor.presenters.LoginPresenterImpl;
import com.tiendas3b.ticketdoctor.views.LoginView;

import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final int REQUEST_READ_CONTACTS = 0;
    private AutoCompleteTextView txtUser;
    private EditText txtPassword;
    private ProgressDialog progressDialog;
    private View loginForm;
    private LoginPresenter presenter;
    private GlobalState mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = (GlobalState) getApplicationContext();
        presenter = new LoginPresenterImpl(this, mContext);
        txtUser = (AutoCompleteTextView) findViewById(R.id.txt_user);
        populateAutoComplete();

        txtPassword = (EditText) findViewById(R.id.password);
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    presenter.validateCredentials(txtUser.getText().toString(), txtPassword.getText().toString());
                    return true;
                }
                return false;
            }
        });

        Button btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.validateCredentials(txtUser.getText().toString(), txtPassword.getText().toString());
            }
        });

        loginForm = findViewById(R.id.login_form);
        loginForm.setVisibility(View.GONE);
        final View imgLogo = findViewById(R.id.img_logo);
        Animation anmTranslate = AnimationUtils.loadAnimation(this, R.anim.translate_login);
        anmTranslate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgLogo.clearAnimation();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imgLogo.getWidth(), imgLogo.getHeight());
                lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                imgLogo.setLayoutParams(lp);
                loginForm.setVisibility(View.VISIBLE);
                Animation animFade = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_login);
                animFade.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        txtUser.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txtUser, InputMethodManager.SHOW_IMPLICIT);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                loginForm.startAnimation(animFade);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imgLogo.startAnimation(anmTranslate);

        presenter.validateCredentialsWithoutMd5();

//        //dummy
//        txtUser.setText("dfa");
//        txtPassword.setText("gggggg");
//        //
    }

    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//        getLoaderManager().initLoader(0, null, presenter);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(txtUser, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                @TargetApi(Build.VERSION_CODES.M)
                public void onClick(View v) {
                    requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                }
            });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @Override
    public void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        txtUser.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void hideProgress() {
        showProgress(false);
    }

    @Override
    public void setUsernameError() {
        txtUser.setError(getString(R.string.error_invalid_user));
        txtUser.requestFocus();
    }

    @Override
    public void setPasswordError() {
        txtPassword.setError(getString(R.string.error_invalid_password));
        txtPassword.requestFocus();
    }

    @Override
    public void navigateToHome() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        startActivity(new Intent(this, MainActivity.class));
//        hideProgress();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
//        overridePendingTransition(R.anim.fadein, R.anim.fade_login);
    }

    @Override
    public void resetErrors() {
        txtUser.setError(null);
        txtPassword.setError(null);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        if (show) {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.login_loading));
            progressDialog.show();

        } else {
            progressDialog.dismiss();
        }
    }
}

