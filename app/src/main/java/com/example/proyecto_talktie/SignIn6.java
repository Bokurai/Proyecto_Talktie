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
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SignIn6 extends Fragment {

    StudentRegisterViewModel registerViewModel;
    NavController navController;

    EditText nameET, phoneET, emailET, dateET;

    AppCompatButton nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(requireActivity()).get(StudentRegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in6, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameET = view.findViewById(R.id.etNameSignOne);
        phoneET = view.findViewById(R.id.etPhoneNumber);
        emailET = view.findViewById(R.id.etEmailOne);
        dateET = view.findViewById(R.id.birth_date);
        nextButton = view.findViewById(R.id.btnContinueOne);
        navController = Navigation.findNavController(view);

        nameET.setEnabled(false);
        emailET.setEnabled(false);
        phoneET.setEnabled(false);
        nameET.setText(registerViewModel.getName().toString());
        emailET.setText(registerViewModel.getEmail().toString());
        phoneET.setText(registerViewModel.getPhoneNumber().toString());

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Timestamp fecha_cambiada = editTextToTimestamp(dateET);
                registerViewModel.setBirth_date(fecha_cambiada);
                navController.navigate(R.id.signIn7);
            }
        });
    }

    private Timestamp editTextToTimestamp(EditText dateEditText) {
        String dateString = dateEditText.getText().toString();

        if (TextUtils.isEmpty(dateString)) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateString);
            if (date != null) {
                return new Timestamp(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}