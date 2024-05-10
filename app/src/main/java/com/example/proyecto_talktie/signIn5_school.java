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

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class signIn5_school extends Fragment {

    SchoolRegisterViewModel registerViewModel;
    NavController navController;
    EditText etSummary, etFoundation, etSpecialities;
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
        return inflater.inflate(R.layout.fragment_sig_in5_school, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSummary = view.findViewById(R.id.etSummaryS);
        etFoundation = view.findViewById(R.id.etFoundationDateS);
        button = view.findViewById(R.id.btnContinue6School);
        navController = Navigation.findNavController(view);

        etFoundation.addTextChangedListener(new TextWatcher() {
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
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
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
                    etFoundation.setText(current);
                    etFoundation.setSelection(sel < current.length() ? sel : current.length());
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp fecha_cambiada = editTextToTimestamp(etFoundation);
                registerViewModel.setSummary(etSummary.getText().toString());
                registerViewModel.setFoundation_date(fecha_cambiada);
                registerViewModel.saveUserFirestore();
                registerViewModel.saveSchoolFirestore();
                navController.navigate(R.id.action_goSchoolHome);
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