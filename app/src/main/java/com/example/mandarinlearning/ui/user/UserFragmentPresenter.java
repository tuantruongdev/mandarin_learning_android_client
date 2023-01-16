package com.example.mandarinlearning.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.remote.api.ApiFetch;
import com.example.mandarinlearning.data.remote.api.INetCallback;
import com.example.mandarinlearning.utils.ApplicationHelper;
import com.example.mandarinlearning.utils.NotificationHelper;

import java.io.IOException;

/**
 * Created by macos on 06,September,2022
 */
public class UserFragmentPresenter implements IUserFragmentPresenter {
    private IUserFragment cb;
    private Repository repository;

    public UserFragmentPresenter(IUserFragment cb) {
        this.cb = cb;
        repository =  Repository.getInstance();
    }

    void logout() {
        ApplicationHelper.getInstance().setLocalUser(null);
        //handle logout here
        SharedPreferences sharedPreferences = ApplicationHelper.getInstance().getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("user", "{}").apply();
    }

    //why callback? im i idiot?
    void validatePasswords(String currentPass, String newPass, String reNewPass) {
        if (currentPass.length() < 6) {
            cb.onValidateResult("Your current password is incorrect");
            return;
        }
        if (newPass.length() < 6 || reNewPass.length() < 6) {
            cb.onValidateResult("New password must have at least 6 character");
            return;
        }
        if (!TextUtils.equals(newPass, reNewPass)) {
            cb.onValidateResult("Your new password no match");
            return;
        }
        cb.onValidateResult("");
    }

    public void changePassword(String currentPassword, String newPassword) {

    }
    public void changeName(String name) {
        repository.changeName(name, new INetCallback() {
            @Override
            public void onSuccess(String body) {

            }

            @Override
            public void onFailure(IOException e) {
                cb.onValidateResult(e.getMessage());
            }
        });
    }


}
