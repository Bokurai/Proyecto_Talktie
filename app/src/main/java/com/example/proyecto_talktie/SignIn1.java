package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_talktie.databinding.FragmentLoginBinding;
import com.example.proyecto_talktie.databinding.FragmentSignIn1Binding;
import com.google.firebase.auth.FirebaseAuth;


public class SignIn1 extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FragmentSignIn1Binding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentSignIn1Binding.inflate(inflater, container, false)).getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.etEmail.setEnabled(false);
        binding.etEmail.setText(mAuth.getCurrentUser().getEmail());
    }
}