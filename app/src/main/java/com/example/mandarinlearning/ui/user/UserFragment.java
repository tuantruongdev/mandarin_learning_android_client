package com.example.mandarinlearning.ui.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.ui.login.LoginActivity;
import com.example.mandarinlearning.ui.main.MainActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {
    TextView user;
    Button login, logout;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = view.findViewById(R.id.user);
        login = view.findViewById(R.id.login);
        logout = view.findViewById(R.id.logout);

        bind();
        reloadUser();
    }
    @Override
    public void onResume() {
        super.onResume();
        reloadUser();
    }

    private void bind() {
        login.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            reloadUser();
        });
    }


    private void reloadUser() {
        FirebaseApp.initializeApp(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            user.setText("youre not logged in");
            return;
        }
        user.setText(currentUser.getEmail());
    }

}