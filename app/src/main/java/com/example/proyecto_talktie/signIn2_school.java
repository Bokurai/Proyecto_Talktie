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

import com.example.proyecto_talktie.viewmodel.SchoolRegisterViewModel;


public class signIn2_school extends Fragment {

    SchoolRegisterViewModel registerViewModel;
    NavController navController;
    EditText nameSchoolET, codeSchoolET, typeSchoolET;
    AppCompatButton nextButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerViewModel = new ViewModelProvider(requireActivity()).get(SchoolRegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in2_school, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameSchoolET = view.findViewById(R.id.etNameSchool);
        codeSchoolET = view.findViewById(R.id.etcodeSchool);
        typeSchoolET = view.findViewById(R.id.ettypeSchool);

        nextButton = view.findViewById(R.id.btnContinue3School);
        navController = Navigation.findNavController(view);

        nameSchoolET.setText(registerViewModel.getName().getValue());

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                    registerViewModel.setName(nameSchoolET.getText().toString());
                    registerViewModel.setSchoolCode(codeSchoolET.getText().toString());
                    registerViewModel.setSchoolType(typeSchoolET.getText().toString());
                    navController.navigate(R.id.action_goSignIn3School);

                }
            }
        });

        ImageView imageArrowLeft = view.findViewById(R.id.imageArrowleftS);
        imageArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn2_school);
            }
        });


    }

    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(typeSchoolET.getText().toString())) {
            typeSchoolET.setError("Required.");
            valid = false;
        } else {
            typeSchoolET.setError(null);
        }

        if (TextUtils.isEmpty(nameSchoolET.getText().toString())) {
            nameSchoolET.setError("Required.");
            valid = false;
        } else {
            nameSchoolET.setError(null);
        }

        if (TextUtils.isEmpty(codeSchoolET.getText().toString())) {
            codeSchoolET.setError("Required.");
            valid = false;
        } else {
            codeSchoolET.setError(null);
        }
        return valid;
    }
}