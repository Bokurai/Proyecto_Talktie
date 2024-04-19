package com.example.proyecto_talktie;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.proyecto_talktie.databinding.FragmentSignIn1Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignIn1 extends Fragment {

    FragmentSignIn1Binding binding;
    private Button registerButton, buttonImageProfile;
    NavController navController;
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, nameEditText, mobileEditText;
    private CircleImageView profileImageView;
    int SELECT_PICTURE = 200;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference,imageReference;
    UploadTask uploadTask;
    Uri uri;
    StudentRegisterViewModel registerViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        registerViewModel = new ViewModelProvider(requireActivity()).get(StudentRegisterViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentSignIn1Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = view.findViewById(R.id.etEmail);
        passwordEditText = view.findViewById(R.id.etPassword);
        nameEditText = view.findViewById(R.id.etName);
        mobileEditText = view.findViewById(R.id.etMobile);
        profileImageView = view.findViewById(R.id.imageProfile);
        registerButton = view.findViewById(R.id.btnSingIn);
        buttonImageProfile = view.findViewById(R.id.btnSelectProfileRegister);
        navController = Navigation.findNavController(view);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuenta();
            }
        });

        buttonImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGalleryImageRegister();
            }
        });

    }


    private void selectGalleryImageRegister() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    profileImageView.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private void crearCuenta() {
        if (!validarFormulario()) {
            return;
        }

        registerButton.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerViewModel.setEmail(emailEditText.getText().toString());
                            registerViewModel.setName(nameEditText.getText().toString());
                            registerViewModel.setPassword(passwordEditText.getText().toString());
                            registerViewModel.setPhoneNumber(mobileEditText.getText().toString());
                            registerViewModel.setProfileImage(profileImageView.getImageAlpha());
                            actualizarUI(mAuth.getCurrentUser());
                        } else {
                            Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                        }
                        registerButton.setEnabled(true);
                    }
                });

    }

    private void actualizarUI(FirebaseUser currentUser) {
        if(currentUser != null){
            navController.navigate(R.id.signIn6);
        }
    }

    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }
        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            nameEditText.setError("Required.");
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        if (TextUtils.isEmpty(mobileEditText.getText().toString())) {
            mobileEditText.setError("Required.");
            valid = false;
        } else {
            mobileEditText.setError(null);
        }
        return valid;
    }
}