package com.example.mandarinlearning.ui.login;

/**
 * Created by macos on 16,August,2022
 */
public interface LoginActivityMvpView {
    void onLoginChange(Boolean isLoggedIn);

    void onErrorMessageChange(String errorMessage);
}
