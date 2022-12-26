package com.example.mandarinlearning.ui.login;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.ActivityLoginBinding;
import com.example.mandarinlearning.ui.base.BaseActivity;
import com.example.mandarinlearning.ui.detail.DetailCharacterActivity;
import com.example.mandarinlearning.utils.ApplicationHelper;
import com.example.mandarinlearning.utils.Const;
import com.google.firebase.FirebaseApp;

public class LoginActivity extends BaseActivity implements ILoginActivityView {
    private LoginActivityPresenter loginActivityViewModel;
    private ActivityLoginBinding binding;

    public static void starter(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginActivityViewModel = new LoginActivityPresenter(this);
        //getSupportActionBar().hide();
        setActivityTitle("");

        FirebaseApp.initializeApp(LoginActivity.this);
        bind();
    }



    /*callback*/
    @Override
    public void onLoginChange(Boolean isLoggedIn) {
        if (isLoggedIn) {
            finish();
            ApplicationHelper.overrideAnimation(this,1);
        }
    }

    @Override
    public void onErrorMessageChange(String errorMessage) {
        binding.errorMessage.setVisibility(View.VISIBLE);
        binding.errorMessage.setText(errorMessage);
    }
    /*end callback*/

    private void bind() {
        binding.login.setOnClickListener(v -> loginActivityViewModel.onLoginClicked(binding.userName.getText().toString(), binding.password.getText().toString()));
        binding.signup.setOnClickListener(v -> loginActivityViewModel.onSignUpClicked(binding.userName.getText().toString(), binding.password.getText().toString()));
        binding.forgetPassword.setOnClickListener(v -> loginActivityViewModel.onForgotClicked(binding.userName.getText().toString()));
    }

}