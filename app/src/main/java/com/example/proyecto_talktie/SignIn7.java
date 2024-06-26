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

import com.example.proyecto_talktie.viewmodel.StudentRegisterViewModel;

/**
 * Fragment that is part of the signIn that handles user input for address details including country, street address, city, and postal code.
 */
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
    /**
     * This method initializes various UI components and sets up click listeners for the continue buttons.
     */
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
                if (validarFormulario()) {
                    registerViewModel.setCountry(etCountry.getText().toString());
                    registerViewModel.setCity(etCity.getText().toString());
                    registerViewModel.setZipcode(etPostalCode.getText().toString());
                    registerViewModel.setHomeAddress(etStreetAddress.getText().toString());
                    navController.navigate(R.id.signIn8);
                }
            }
        });
        ImageView imageArrowleft = view.findViewById(R.id.imageArrowleft);
        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn6);
            }
        });
    }
    /**
     * Validates the user input form.
     * @return True if the form is valid, false otherwise.
     */
    private boolean validarFormulario() {
        boolean valid = true;
        if (TextUtils.isEmpty(etCountry.getText().toString())) {
            etCountry.setError("Required.");
            valid = false;
        } else {
            etCountry.setError(null);
        }
        return valid;
    }
}