package com.example.mandarinlearning.ui.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mandarinlearning.utils.ApplicationHelper;

/**
 * Created by macos on 06,September,2022
 */
public class UserFragmentPresenter implements IUserFragmentPresenter {
    void logout() {
        ApplicationHelper.getInstance().setLocalUser(null);
        //handle logout here
        SharedPreferences sharedPreferences = ApplicationHelper.getInstance().getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("user", "{}").apply();
    }

}
