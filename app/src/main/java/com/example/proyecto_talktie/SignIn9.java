package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.proyecto_talktie.viewmodel.StudentRegisterViewModel;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Fragment that is part of the signIn that handles user input for job details including company name, location, job title, and dates of employment.
 */
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
        return inflater.inflate(R.layout.fragment_sign_in9, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton = view.findViewById(R.id.btnContinuetoTen);
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
        etStartDateJob.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", "");
                    String cleanC = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    // Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        if(day > 31) day = 31;

                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }
                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));
                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    etStartDateJob.setText(current);
                    etStartDateJob.setSelection(sel < current.length() ? sel : current.length());
                }
            }
        });

        etEndDateJob.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", "");
                    String cleanC = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;
                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        if(day > 31) day = 31;

                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    etEndDateJob.setText(current);
                    etEndDateJob.setSelection(sel < current.length() ? sel : current.length());
                }
            }
        });
        ImageView imageArrowleft = view.findViewById(R.id.imageArrowleft);
        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn8);
            }
        });
    }

    /**
     * Converts the date string from an EditText to a Timestamp.
     * @param dateEditText The EditText containing the date string.
     * @return The corresponding Timestamp, or null if the date string is invalid.
     */
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