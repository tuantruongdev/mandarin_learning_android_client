package com.jtinteractive.mandarinlearning.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jtinteractive.mandarinlearning.R;
import com.jtinteractive.mandarinlearning.databinding.ActivityLoginBinding;
import com.jtinteractive.mandarinlearning.ui.base.BaseActivity;
import com.jtinteractive.mandarinlearning.utils.ApplicationHelper;
import com.jtinteractive.mandarinlearning.utils.NotificationHelper;


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

     //   FirebaseApp.initializeApp(LoginActivity.this);
        bind();
    }


    /*callback*/
    @Override
    public void onLoginChange(Boolean isLoggedIn) {
        if (isLoggedIn) {
            finish();
            ApplicationHelper.overrideAnimation(this, 1);
        }
    }

    @Override
    public void onErrorMessageChange(String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NotificationHelper.showSnackBar(binding.getRoot(), 2, errorMessage);
                binding.errorMessage.setVisibility(View.VISIBLE);
                binding.errorMessage.setText(errorMessage);
            }
        });

    }
    /*end callback*/

    private void bind() {
        binding.login.setOnClickListener(v -> loginActivityViewModel.onLoginClicked(binding.userName.getText().toString(), binding.password.getText().toString()));
        binding.signup.setOnClickListener(v -> loginActivityViewModel.onSignUpClicked(binding.userName.getText().toString(), binding.password.getText().toString()));
        binding.forgetPassword.setOnClickListener(v -> loginActivityViewModel.onForgotClicked(binding.userName.getText().toString()));
    }

}