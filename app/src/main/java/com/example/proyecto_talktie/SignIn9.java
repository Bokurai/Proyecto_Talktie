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
import android.widget.RadioButton;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SignIn9 extends Fragment {
    StudentRegisterViewModel registerViewModel;
    NavController navController;

    EditText etCompanyName, etLocation,etJobTitle, etStartDateJob, etEndDateJob;

    RadioButton radioButton;

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
        return inflater.inflate(R.layout.fragment_sign_in9, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton = view.findViewById(R.id.btnContinuetoTen);
        radioButton = view.findViewById(R.id.rbIamcurrently);
        etJobTitle = view.findViewById(R.id.etJobTitle);
        etCompanyName = view.findViewById(R.id.etCompanyName);
        etStartDateJob = view.findViewById(R.id.etStartDateJob);
        etEndDateJob = view.findViewById(R.id.etEndDateJob);
        etLocation = view.findViewById(R.id.etJobLocation);
        navController = Navigation.findNavController(view);

        if(radioButton.isChecked()){
            etEndDateJob.setEnabled(false);
        }


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerViewModel.setCompany(etCompanyName.getText().toString());
                registerViewModel.setJobTitle(etJobTitle.getText().toString());
                registerViewModel.setLocationJobStudent(etLocation.getText().toString());
                Timestamp fecha_cambiada_start = editTextToTimestamp(etStartDateJob);
                registerViewModel.setStart_date_job(fecha_cambiada_start);
                Timestamp fecha_cambiada_end = editTextToTimestamp(etEndDateJob);
                registerViewModel.setEnd_date_job(fecha_cambiada_end);
                navController.navigate(R.id.signIn10);
            }
        });

        //felcha atras
        ImageView imageArrowleft = view.findViewById(R.id.imageArrowleft);
        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn8);
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