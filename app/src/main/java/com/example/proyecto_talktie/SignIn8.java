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

import com.example.proyecto_talktie.viewmodel.StudentRegisterViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
                if (validarFormulario()) {
                    validateSchool();
                    registerViewModel.setCenter(etSchool.getText().toString());
                    registerViewModel.setLocationSchoolFormation(etLocation.getText().toString());
                    registerViewModel.setDegree(etDegree.getText().toString());
                    Timestamp fecha_cambiada_start = editTextToTimestamp(etStartDateForm);
                    registerViewModel.setStart_date_formation(fecha_cambiada_start);
                    Timestamp fecha_cambiada_end = editTextToTimestamp(etEndDateForm);
                    registerViewModel.setEnd_date_formation(fecha_cambiada_end);
                    navController.navigate(R.id.signIn9);
                }
            }
        });

        // Agregar un TextWatcher al EditText de fecha (para que se vea en formato fecha)
        etStartDateForm.addTextChangedListener(new TextWatcher() {
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
                    etStartDateForm.setText(current);
                    etStartDateForm.setSelection(sel < current.length() ? sel : current.length());
                }
            }
        });

        etEndDateForm.addTextChangedListener(new TextWatcher() {
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
                    etEndDateForm.setText(current);
                    etEndDateForm.setSelection(sel < current.length() ? sel : current.length());
                }
            }
        });

        //felcha atras
        ImageView imageArrowleft = view.findViewById(R.id.imageArrowleft);
        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(etSchool.getText().toString())) {
            etSchool.setError("Required.");
            valid = false;
        } else {
            etSchool.setError(null);
        }
        if (TextUtils.isEmpty(etDegree.getText().toString())) {
            etDegree.setError("Required.");
            valid = false;
        } else {
            etDegree.setError(null);
        }
        return valid;
    }

    private void validateSchool() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String mapUid = "rIh64FpBBfR1Wmu1zyEy";
        DocumentReference schoolListVerification = db.collection("SchoolListVerification").document(mapUid);

        schoolListVerification.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Map<String, String> schoolnamesMap = (Map<String, String>) documentSnapshot.get("school_names");
                if (schoolnamesMap != null && etSchool != null && etSchool.getText() != null) {
                    String schoolName= etSchool.getText().toString();
                    if (!schoolnamesMap.containsValue(schoolName)) {
                        etSchool.setError("Not an existent school.");
                    }
                }
            }
        }).addOnFailureListener(e -> {
        });
    }


}