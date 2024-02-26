package com.example.proyecto_talktie;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.proyecto_talktie.databinding.FragmentForgetPasswordBinding;


public class ForgetPassword extends Fragment {

  FragmentForgetPasswordBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)).getRoot();
    }
}