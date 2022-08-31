package com.example.mandarinlearning.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.databinding.ActivityMainBinding;
import com.example.mandarinlearning.utils.Const;

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

        bind();
        initViewPager();
        // Toast.makeText(this, Const.Levels.NEWBIE.toString(), Toast.LENGTH_SHORT).show();
    }

    private void bind() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_dictionary:
                    binding.viewPager.setCurrentItem(Const.Screen.DICTIONARY_SCREEN);
                    break;
                case R.id.navigation_quiz:
                    binding.viewPager.setCurrentItem(Const.Screen.QUIZ_SCREEN);
                    break;
                case R.id.navigation_favorite:
                    binding.viewPager.setCurrentItem(Const.Screen.FAVORITE_SCREEN);
                    break;
                case R.id.navigation_account:
                    binding.viewPager.setCurrentItem(Const.Screen.ACCOUNT_SCREEN);
                    break;
            }
            return true;
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case Const.Screen.DICTIONARY_SCREEN:
                        binding.bottomNav.getMenu().findItem(R.id.navigation_dictionary).setChecked(true);
                        break;
                    case Const.Screen.QUIZ_SCREEN:
                        binding.bottomNav.getMenu().findItem(R.id.navigation_quiz).setChecked(true);
                        break;
                    case Const.Screen.FAVORITE_SCREEN:
                        binding.bottomNav.getMenu().findItem(R.id.navigation_favorite).setChecked(true);
                        break;
                    case Const.Screen.ACCOUNT_SCREEN:
                        binding.bottomNav.getMenu().findItem(R.id.navigation_account).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(viewPagerAdapter);
    }
}