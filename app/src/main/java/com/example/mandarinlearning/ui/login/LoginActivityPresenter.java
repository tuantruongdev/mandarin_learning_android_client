package com.example.mandarinlearning.ui.login;

import static android.content.ContentValues.TAG;

import android.text.TextUtils;
import android.util.Log;

import com.example.mandarinlearning.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by macos on 11,August,2022
 */
public class LoginActivityPresenter implements ILoginActivityPresenter {
    private ILoginActivityView loginActivityMvpView;
    private FirebaseAuth mAuth;
    private Boolean isLoggedIn;
    private String errorMessage;

    LoginActivityPresenter(ILoginActivityView cb) {
        this.mAuth = FirebaseAuth.getInstance();
        this.loginActivityMvpView = cb;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // reload();
        }
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        loginActivityMvpView.onErrorMessageChange(errorMessage);
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
        loginActivityMvpView.onLoginChange(isLoggedIn);
        //smth
    }

    /*callback call from view*/
    @Override
    public void onLoginClicked(String username, String password) {
        String validate = validateUser(username, password);
        if (validate != null) {
            setErrorMessage(validate);
            return;
        }
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        // FirebaseUser user = mAuth.getCurrentUser();
                        //  finish();
                        setLoggedIn(true);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        //  Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        //live data here
                        setLoggedIn(false);
                        Log.d(TAG, "onLoginClicked: " + task.getException().getLocalizedMessage());
                        setErrorMessage(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void onSignUpClicked(String username, String password) {
        String validate = validateUser(username, password);
        if (validate != null) {
            setErrorMessage(validate);
            return;
        }
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        //  FirebaseUser user = mAuth.getCurrentUser();
                        setLoggedIn(true);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        // Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        //live data
                        setErrorMessage(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void onForgotClicked(String username) {
        mAuth.sendPasswordResetEmail(username)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setErrorMessage("We have sent you instructions to reset your password!");
                        //    Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                    } else {
                        setErrorMessage("Failed to send reset email!");
                        //   Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /*end callback call from view*/

    private String validateUser(String username, String password) {
        if (username == null || password == null) {
            return Const.Auth.NO_USERNAME_OR_PASSWORD;
        }
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return Const.Auth.NO_USERNAME_OR_PASSWORD;
        }
        if (password.length() < Const.Auth.MINIMUM_PASSWORD_LENGTH) {
            return Const.Auth.PASSWORD_LESS_THAN_MINIMUM;
        }
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(Const.Auth.EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(username);
        if (!matcher.find()) {
            return Const.Auth.EMAIL_VALIDATE_FAILED;
        }
        return null;
    }
}
