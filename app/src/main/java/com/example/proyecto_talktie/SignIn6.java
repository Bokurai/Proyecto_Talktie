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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SignIn6 extends Fragment {

    StudentRegisterViewModel registerViewModel;
    NavController navController;

    EditText nameET, phoneET, emailET, dateET;

    AppCompatButton nextButton;
    Bundle savedState; //guardar info campos

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
        nameET.setText(registerViewModel.getName().getValue());
        emailET.setText(registerViewModel.getEmail().getValue());
        phoneET.setText(registerViewModel.getPhoneNumber().getValue());

        if (savedState != null) {
            // Restaurar el estado de los campos del fragmento
            nameET.setText(savedState.getString("name"));
            phoneET.setText(savedState.getString("phone"));
            emailET.setText(savedState.getString("email"));
            dateET.setText(savedState.getString("date"));
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                Timestamp fecha_cambiada = editTextToTimestamp(dateET);
                    registerViewModel.setBirth_date(fecha_cambiada);
                    navController.navigate(R.id.signIn7);
                }
            }
        });

        //flecha atras
        ImageView imageArrowleft = view.findViewById(R.id.imageArrowleft);
        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn1);
            }
        });
    }
    //metodo par guardar la info de los campos
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el estado de los campos del fragmento
        outState.putString("name", nameET.getText().toString());
        outState.putString("phone", phoneET.getText().toString());
        outState.putString("email", emailET.getText().toString());
        outState.putString("date", dateET.getText().toString());
        savedState = outState;
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
    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(dateET.getText().toString())) {
            dateET.setError("Required.");
            valid = false;
        } else {
            dateET.setError(null);
        }
        return valid;
    }

}