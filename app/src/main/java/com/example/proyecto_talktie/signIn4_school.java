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

import com.example.proyecto_talktie.viewmodel.SchoolRegisterViewModel;

/**
 * Fragment that is part of the signIn that handles user input for address details including country, street address, city, and postal code.
 */
public class signIn4_school extends Fragment {

    SchoolRegisterViewModel registerViewModel;
    NavController navController;
    TextView etCountry, etAddress, etCity, etZipcode;
    AppCompatButton button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerViewModel = new ViewModelProvider(requireActivity()).get(SchoolRegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in4_school, container, false);
    }
    /**
     * This method initializes various UI components and sets up click listeners for the continue buttons.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCountry = view.findViewById(R.id.etCountrySchool);
        etAddress = view.findViewById(R.id.etStreetAddressSchool);
        etCity = view.findViewById(R.id.etCitySchool);
        button = view.findViewById(R.id.btnContinue5School);
        navController = Navigation.findNavController(view);
        etZipcode = view.findViewById(R.id.etzipcodeSchool);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                    registerViewModel.setCountry(etCountry.getText().toString());
                    registerViewModel.setCity(etCity.getText().toString());
                    registerViewModel.setStreetAddress(etAddress.getText().toString());
                    registerViewModel.setZipcode(etZipcode.getText().toString());
                    navController.navigate(R.id.action_goSignIn5School);
                }
            }
        });

        ImageView imageArrowLeft = view.findViewById(R.id.imageArrowleftS);

        imageArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn3_school);
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