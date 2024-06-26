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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Fragment that is part of the signIn that handles the input of the user's personal data, including name, phone number, email and date of birth.
 */

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
    /**
     * Called to have the fragment instantiate its user interface view. This method inflates the layout for the fragment.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in6, container, false);
    }
    /**
     * This method initializes various UI components and sets up click listeners for the continue buttons.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameET = view.findViewById(R.id.etNameSignOne);
        phoneET = view.findViewById(R.id.etPhoneNumber);
        emailET = view.findViewById(R.id.etEmailOne);
        dateET = view.findViewById(R.id.birth_date);
        nextButton = view.findViewById(R.id.btnContinueOne);
        navController = Navigation.findNavController(view);

        emailET.setEnabled(false);
        nameET.setEnabled(false);
        nameET.setText(registerViewModel.getName().getValue());
        emailET.setText(registerViewModel.getEmail().getValue());
        phoneET.setText(registerViewModel.getPhoneNumber().getValue());

        // Add a TextWatcher to the date EditText (to display in date format)
        dateET.addTextChangedListener(new TextWatcher() {
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
                    dateET.setText(current);
                    dateET.setSelection(sel < current.length() ? sel : current.length());
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                Timestamp fecha_cambiada = editTextToTimestamp(dateET);
                    registerViewModel.setBirth_date(fecha_cambiada);
                    registerViewModel.setName(nameET.getText().toString());
                    registerViewModel.setPhoneNumber(phoneET.getText().toString());
                    navController.navigate(R.id.signIn7);
                }
            }
        });
        ImageView imageArrowleft = view.findViewById(R.id.imageArrowleft);
        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn1);
            }
        });
    }
    /**
     * Converts the date entered in the EditText to a Timestamp.
     * @param dateEditText The EditText containing the date.
     * @return The Timestamp corresponding to the entered date.
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
    /**
     * Validates the user input form.
     * @return True if the form is valid, false otherwise.
     */
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