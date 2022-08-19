package com.example.mandarinlearning.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mandarinlearning.ui.dictionary.DictionaryFragment;
import com.example.mandarinlearning.ui.favorite.FavoriteFragment;
import com.example.mandarinlearning.ui.quiz.QuizFragment;
import com.example.mandarinlearning.ui.user.UserFragment;

/**
 * Created by macos on 12,August,2022
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new QuizFragment();
            case 2:
                return new FavoriteFragment();
            case 3:
                return new UserFragment();
            default: return new DictionaryFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
