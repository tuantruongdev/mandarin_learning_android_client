package com.example.mandarinlearning.ui.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.databinding.ActivityMainBinding;
import com.example.mandarinlearning.ui.base.BaseActivity;
import com.example.mandarinlearning.ui.base.MainFragment;
import com.example.mandarinlearning.ui.base.PermissionHandler;
import com.example.mandarinlearning.utils.ApplicationHelper;
import com.example.mandarinlearning.utils.NotificationHelper;

import java.util.List;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        actionBar = getSupportActionBar();
        actionBar.hide();
        ApplicationHelper.getInstance().init(getApplicationContext());
        Repository.getInstance().setWordDao(new WordDao(this));
        addFragment(binding.mainView.getId(), MainFragment.newInstance(), "main");
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
       List<Fragment>fms = getSupportFragmentManager().getFragments();
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



}

