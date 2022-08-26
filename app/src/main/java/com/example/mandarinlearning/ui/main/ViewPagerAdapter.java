package com.example.mandarinlearning.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mandarinlearning.ui.dictionary.DictionaryFragment;
import com.example.mandarinlearning.ui.favorite.FavoriteFragment;
import com.example.mandarinlearning.ui.quiz.QuizFragment;
import com.example.mandarinlearning.ui.user.UserFragment;
import com.example.mandarinlearning.utils.Const;

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
        switch (position) {
            case Const.Screen.QUIZ_SCREEN:
                return new QuizFragment();
            case Const.Screen.FAVORITE_SCREEN:
                return new FavoriteFragment();
            case Const.Screen.ACCOUNT_SCREEN:
                return new UserFragment();
            default:
                return new DictionaryFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
