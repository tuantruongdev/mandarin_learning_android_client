package com.jtinteractive.mandarinlearning.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.jtinteractive.mandarinlearning.data.Repository;
import com.jtinteractive.mandarinlearning.data.local.dao.WordDao;
import com.jtinteractive.mandarinlearning.data.remote.model.LocalUser;
import com.jtinteractive.mandarinlearning.databinding.ActivityMainBinding;
import com.jtinteractive.mandarinlearning.ui.base.BaseActivity;
import com.jtinteractive.mandarinlearning.ui.base.MainFragment;
import com.jtinteractive.mandarinlearning.utils.ApplicationHelper;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ApplicationHelper.overrideAnimation(this, 2);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        actionBar = getSupportActionBar();
        actionBar.hide();
        ApplicationHelper.getInstance().init(getApplicationContext());
        Repository.getInstance().setWordDao(new WordDao(this));
        addFragment(binding.mainView.getId(), MainFragment.newInstance(), "main");
        initUser();
      //  throw new RuntimeException("Test Crash"); // Force a crash
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        List<Fragment> fms = getSupportFragmentManager().getFragments();
        MainFragment mainFragment = (MainFragment) fms.get(0);
        //back to first or quit program
        mainFragment.toFirstFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addFragment(@IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag) {
        getSupportFragmentManager().beginTransaction().add(containerViewId, fragment, fragmentTag)
                // .disallowAddToBackStack()
                .commit();
    }

    public void replaceFragment(@IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag, @Nullable String backStackStateName) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment, fragmentTag).addToBackStack(backStackStateName).commit();
    }

    public void initUser() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        LocalUser localUser = gson.fromJson(sharedPreferences.getString("user", "{}"), LocalUser.class);
        if (localUser == null || localUser.getToken() == null) return;
        ApplicationHelper.getInstance().setLocalUser(localUser);
    }


}

