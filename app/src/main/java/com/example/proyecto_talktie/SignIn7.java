package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class SignIn7 extends Fragment {
    StudentRegisterViewModel registerViewModel;
    NavController navController;

    EditText etCountry, etStreetAddress,etCity,etPostalCode;

    AppCompatButton nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(requireActivity()).get(StudentRegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in7, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etCountry = view.findViewById(R.id.etCountry);
        etStreetAddress = view.findViewById(R.id.etStreetAddress);
        etCity = view.findViewById(R.id.etCity);
        etPostalCode = view.findViewById(R.id.etPostalCode);
        nextButton = view.findViewById(R.id.btnContinuetoEight);
        navController = Navigation.findNavController(view);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerViewModel.setCountry(etCountry.getText().toString());
                navController.navigate(R.id.signIn8);
            }
        });
    }
}