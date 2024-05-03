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
import android.widget.ImageView;
import android.widget.TextView;


public class signIn4_company extends Fragment {
    BusinessRegisterViewModel registerViewModel;
    NavController navController;
    TextView etCountry, etAddress, etCity, etZipcode;
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
        return inflater.inflate(R.layout.fragment_sign_in4_company, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etCountry = view.findViewById(R.id.etCountryCompany);
        etAddress = view.findViewById(R.id.etStreetAddressCompany);
        etCity = view.findViewById(R.id.etCityCompany);
        button = view.findViewById(R.id.btnContinue5Company);
        navController = Navigation.findNavController(view);
        etZipcode = view.findViewById(R.id.etzipcode);

        etAddress.setText(registerViewModel.getEmail().toString());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                    registerViewModel.setCountry(etCountry.getText().toString());
                    registerViewModel.setCity(etCity.getText().toString());
                    registerViewModel.setHeadquarters(etAddress.getText().toString());
                    registerViewModel.setZipcode(etZipcode.getText().toString());
                    navController.navigate(R.id.signIn5_company);
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

        if (TextUtils.isEmpty(etCountry.getText().toString())) {
            etCountry.setError("Required.");
            valid = false;
        } else {
            etCountry.setError(null);
        }

        if (TextUtils.isEmpty(etCity.getText().toString())) {
            etCity.setError("Required.");
            valid = false;
        } else {
            etCity.setError(null);
        }

        if (TextUtils.isEmpty(etAddress.getText().toString())) {
            etAddress.setError("Required.");
            valid = false;
        } else {
            etAddress.setError(null);
        }
        return valid;
    }
}