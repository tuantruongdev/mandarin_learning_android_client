package com.example.mandarinlearning.ui.dictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.databinding.FragmentDictionaryBinding;
import com.example.mandarinlearning.databinding.FragmentMainDictionaryBinding;
import com.example.mandarinlearning.ui.base.BaseFragment;
import com.example.mandarinlearning.ui.dictionary.DictionaryFragment;

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