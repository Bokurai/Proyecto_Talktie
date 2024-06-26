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
import android.widget.ImageView;

import com.example.proyecto_talktie.viewmodel.StudentRegisterViewModel;

/**
 *Fragment that is part of the signIn that handles user input for job preferences and saves the user data to Firestore.
 */
public class SignIn10 extends Fragment {
    StudentRegisterViewModel registerViewModel;
    AppCompatButton nextButton;
    NavController navController;
    EditText jobPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(requireActivity()).get(StudentRegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in10, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jobPreferences = view.findViewById(R.id.etJobPreferences);
        nextButton = view.findViewById(R.id.btnDone);
        navController = Navigation.findNavController(view);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerViewModel.setJob_categories(jobPreferences.getText().toString());
                registerViewModel.saveUserFirestore();
                registerViewModel.saveStudentFirestore();
                navController.navigate(R.id.home);
            }
        });
        ImageView imageArrowleft = view.findViewById(R.id.imageArrowleft);
        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn9);
            }
        });
    }
}