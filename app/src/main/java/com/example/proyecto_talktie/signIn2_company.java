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


public class signIn2_company extends Fragment {

    BusinessRegisterViewModel registerViewModel;
    NavController navController;

    EditText namecompanyET, codecompanyET, sectorcompanyET, typecompanyET;

    AppCompatButton nextButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(requireActivity()).get(BusinessRegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in2_company, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        namecompanyET = view.findViewById(R.id.etNameCompany);
        codecompanyET = view.findViewById(R.id.etcodecompany);
        sectorcompanyET = view.findViewById(R.id.etsectorcompany);
        typecompanyET = view.findViewById(R.id.ETtypecompany);
        nextButton = view.findViewById(R.id.btnContinue3Company);
        navController = Navigation.findNavController(view);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                    registerViewModel.setName(namecompanyET.getText().toString());
                    registerViewModel.setCompanycode(codecompanyET.getText().toString());
                    registerViewModel.setSector(sectorcompanyET.getText().toString());
                    registerViewModel.setCompanytype(typecompanyET.getText().toString());
                    navController.navigate(R.id.action_goSignIn3Company);
                }
            }
        });

        //flecha atras
        ImageView imageArrowleft = view.findViewById(R.id.imageArrowleft);
        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.SignIn1_company);
            }
        });
    }

    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(typecompanyET.getText().toString())) {
            typecompanyET.setError("Required.");
            valid = false;
        } else {
            typecompanyET.setError(null);
        }

        if (TextUtils.isEmpty(namecompanyET.getText().toString())) {
            namecompanyET.setError("Required.");
            valid = false;
        } else {
            namecompanyET.setError(null);
        }

        if (TextUtils.isEmpty(sectorcompanyET.getText().toString())) {
            sectorcompanyET.setError("Required.");
            valid = false;
        } else {
            sectorcompanyET.setError(null);
        }

        if (TextUtils.isEmpty(codecompanyET.getText().toString())) {
            codecompanyET.setError("Required.");
            valid = false;
        } else {
            codecompanyET.setError(null);
        }
        return valid;
    }

}