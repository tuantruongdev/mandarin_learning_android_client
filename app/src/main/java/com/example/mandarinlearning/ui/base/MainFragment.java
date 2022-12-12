package com.example.mandarinlearning.ui.base;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.databinding.FragmentMainBinding;
import com.example.mandarinlearning.ui.dictionary.MainDictionaryFragment;
import com.example.mandarinlearning.ui.favorite.FavoriteFragment;
import com.example.mandarinlearning.ui.quiz.QuizFragment;
import com.example.mandarinlearning.ui.user.UserFragment;
import com.example.mandarinlearning.utils.Const;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends BaseFragment {
    private FragmentMainBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind();
        initViewPager();
    }

    private void bind() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            Log.d(TAG, "bind: " + item.getItemId());
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
//        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.d(TAG, "bind2: "+position);
//                switch (position) {
//                    case Const.Screen.DICTIONARY_SCREEN:
//                        binding.bottomNav.getMenu().findItem(R.id.navigation_dictionary).setChecked(true);
//                        break;
//                    case Const.Screen.QUIZ_SCREEN:
//                        binding.bottomNav.getMenu().findItem(R.id.navigation_quiz).setChecked(true);
//                        break;
//                    case Const.Screen.FAVORITE_SCREEN:
//                        binding.bottomNav.getMenu().findItem(R.id.navigation_favorite).setChecked(true);
//                        break;
//                    case Const.Screen.ACCOUNT_SCREEN:
//                        binding.bottomNav.getMenu().findItem(R.id.navigation_account).setChecked(true);
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
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
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void initViewPager() {
        ViewPager2Adapter viewPagerAdapter = new ViewPager2Adapter(this);
        binding.viewPager.setAdapter(viewPagerAdapter);
    }

    public void onCallBack(boolean enable) {
        binding.viewPager.setUserInputEnabled(enable);
    }

    private class ViewPager2Adapter extends FragmentStateAdapter {

        public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public ViewPager2Adapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public ViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case Const.Screen.QUIZ_SCREEN:
                    return new QuizFragment();
                case Const.Screen.FAVORITE_SCREEN:
                    return new FavoriteFragment();
                case Const.Screen.ACCOUNT_SCREEN:
                    return new UserFragment();
                default:
                    return new MainDictionaryFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

}