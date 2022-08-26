package com.example.mandarinlearning.ui.login;

/**
 * Created by macos on 19,August,2022
 */
public interface ILoginActivityPresenter {
   void onLoginClicked(String username, String password);

    void onSignUpClicked(String username, String password);

    void onForgotClicked(String username);
}
