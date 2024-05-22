package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.proyecto_talktie.viewmodel.BusinessRegisterViewModel;


public class signIn3_company extends Fragment {

    BusinessRegisterViewModel registerViewModel;
    NavController navController;
    EditText etPhone, etEmail, etWebsite;
    AppCompatButton button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(requireActivity()).get(BusinessRegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in3_company, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etPhone = view.findViewById(R.id.ETphoneCompany);
        etEmail = view.findViewById(R.id.ETEmailCompany);
        etWebsite = view.findViewById(R.id.etwebsite);
        button = view.findViewById(R.id.btnContinue3Company);
        navController = Navigation.findNavController(view);

        etEmail.setText(registerViewModel.getEmail().getValue());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                    registerViewModel.setPhoneNumber(etPhone.getText().toString());
                    registerViewModel.setWebsite(etWebsite.getText().toString());
                    navController.navigate(R.id.action_goSignIn4Company);
                }
            }
        });

        //flecha atras
        ImageView imageArrowleft = view.findViewById(R.id.imageArrowleft);
        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.SignIn2_company);
            }
        });
    }

    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            etPhone.setError("Required.");
            valid = false;
        } else {
            etPhone.setError(null);
        }

        if (TextUtils.isEmpty(etWebsite.getText().toString())) {
            etWebsite.setError("Required.");
            valid = false;
        } else {
            etWebsite.setError(null);
        }
        return valid;
    }
}