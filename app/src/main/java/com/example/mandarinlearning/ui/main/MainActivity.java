package com.example.mandarinlearning.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.databinding.ActivityMainBinding;
import com.example.mandarinlearning.ui.base.MainFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        actionBar = getSupportActionBar();
        actionBar.hide();
        Repository.getInstance().setWordDao(new WordDao(this));
        addFragment(binding.mainView.getId(), MainFragment.newInstance(), "main");
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
