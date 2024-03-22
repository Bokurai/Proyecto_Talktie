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

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignIn8 extends Fragment {
    StudentRegisterViewModel registerViewModel;
    NavController navController;

    EditText etSchool, etLocation,etDegree, etStartDateForm, etEndDateForm;

    AppCompatButton nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(requireActivity()).get(StudentRegisterViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in8, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSchool = view.findViewById(R.id.etSchool);
        etLocation = view.findViewById(R.id.etLocationSchool);
        etDegree = view.findViewById(R.id.etDegree);
        etStartDateForm = view.findViewById(R.id.etStartDateFormation);
        etEndDateForm = view.findViewById(R.id.etEndDateFormation);
        nextButton = view.findViewById(R.id.btnContinuetoNine);
        navController = Navigation.findNavController(view);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerViewModel.setCenter(etSchool.getText().toString());
                registerViewModel.setLocationSchoolFormation(etLocation.getText().toString());
                registerViewModel.setDegree(etDegree.getText().toString());
                Timestamp fecha_cambiada_start = editTextToTimestamp(etStartDateForm);
                registerViewModel.setStart_date_formation(fecha_cambiada_start);
                Timestamp fecha_cambiada_end = editTextToTimestamp(etEndDateForm);
                registerViewModel.setEnd_date_formation(fecha_cambiada_end);
                navController.navigate(R.id.signIn9);
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