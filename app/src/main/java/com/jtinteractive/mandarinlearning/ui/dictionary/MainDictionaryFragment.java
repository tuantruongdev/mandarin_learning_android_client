package com.jtinteractive.mandarinlearning.ui.dictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jtinteractive.mandarinlearning.R;
import com.jtinteractive.mandarinlearning.databinding.FragmentDictionaryBinding;
import com.jtinteractive.mandarinlearning.databinding.FragmentMainDictionaryBinding;
import com.jtinteractive.mandarinlearning.ui.base.BaseFragment;

public class MainDictionaryFragment extends BaseFragment {
    private FragmentMainDictionaryBinding binding;
    public MainDictionaryFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainDictionaryBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceChildFragment(new DictionaryFragment(),"",false);
    }
}