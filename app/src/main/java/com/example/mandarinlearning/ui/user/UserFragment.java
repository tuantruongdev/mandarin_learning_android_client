package com.example.mandarinlearning.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.remote.service.SyncIntentService;
import com.example.mandarinlearning.databinding.FragmentUserBinding;
import com.example.mandarinlearning.ui.login.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {
    private FragmentUserBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind();
        reloadUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadUser();
    }

    private void bind() {
        binding.login.setOnClickListener(v -> {
            LoginActivity.starter(getContext());
        });
        binding.logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            reloadUser();
        });
        binding.save.setOnClickListener(v -> backup(true));
        binding.load.setOnClickListener(v -> backup(false));
    }

    private void backup(boolean type) {
        if (currentUser == null) {
            // Starter
            LoginActivity.starter(getContext());
            return;
        }
        SyncIntentService.starter(getContext(), type);
    }

    private void reloadUser() {
        FirebaseApp.initializeApp(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            binding.user.setText(getText(R.string.not_logged_in));
            binding.logout.setVisibility(View.GONE);
            binding.login.setVisibility(View.VISIBLE);
            return;
        }
        binding.user.setText(currentUser.getEmail());
        binding.logout.setVisibility(View.VISIBLE);
        binding.login.setVisibility(View.GONE);
    }

}