package com.jtinteractive.mandarinlearning.ui.login;

/**
 * Created by macos on 16,August,2022
 */
public interface ILoginActivityView {
    void onLoginChange(Boolean isLoggedIn);

    void onErrorMessageChange(String errorMessage);
}
