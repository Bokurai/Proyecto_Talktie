package com.example.proyecto_talktie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proyecto_talktie.databinding.FragmentSignIn1Binding;
import com.example.proyecto_talktie.viewmodel.StudentRegisterViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * A Fragment that handles the user registration process, including both email/password and Google sign-in methods.
 */

public class SignIn1 extends Fragment {
    FragmentSignIn1Binding binding;
    private Button registerButton;
    public static final String EXTRA_FORCE_ACCOUNT_CHOOSER = "force_account_chooser";
    NavController navController;
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, nameEditText, mobileEditText;
    private TextView haveaccount;
    StudentRegisterViewModel registerViewModel;
    SignInButton signUpButton;
    ActivityResultLauncher activityResultLauncher;

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
    /**
     * This method initializes various UI components and sets up click listeners for the sign-up buttons.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = view.findViewById(R.id.etEmail);
        passwordEditText = view.findViewById(R.id.etPassword);
        nameEditText = view.findViewById(R.id.etName);
        mobileEditText = view.findViewById(R.id.etMobile);
        registerButton = view.findViewById(R.id.btnSingIn);
        signUpButton = view.findViewById(R.id.googleSignUpButton);
        haveaccount = view.findViewById(R.id.havecount);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            try {
                                crearCuentaGoogle(GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class));
                            } catch (ApiException e) {
                                Log.e("ABCD", "signInResult:failed code=" +
                                        e.getStatusCode());
                            }
                        }
                    }
                });
        navController = Navigation.findNavController(view);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuentaMailPassword();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accederConGoogle();
            }
        });
        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_goLogin);
            }
        });
    }
    /**
     * Initiates the Google sign-in process.
     */
    private void accederConGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInIntent.putExtra(EXTRA_FORCE_ACCOUNT_CHOOSER, true);
        activityResultLauncher.launch(signInIntent);
    }

    /**
     * Method that creates a new account using Google credentials.
     * @param acct The GoogleSignInAccount object containing the user's Google Account information.
     */
    private void crearCuentaGoogle(GoogleSignInAccount acct){
        if(acct == null) return;

        registerButton.setEnabled(false);

        mAuth.signInWithCredential(GoogleAuthProvider.getCredential(acct.getIdToken(), null))
                .addOnCompleteListener(requireActivity(), new
                        OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.e("ABCD", "signInWithCredential:success");
                                    registerViewModel.setEmail(acct.getEmail());
                                    registerViewModel.setName(acct.getDisplayName());
                                    actualizarUI(mAuth.getCurrentUser());
                                } else {
                                    Log.e("ABCD", "signInWithCredential:failure",
                                            task.getException());
                                }
                            }
                        });
    }
    /**
     * Method that creates a new account using email and password.
     */
    private void crearCuentaMailPassword() {
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
                            actualizarUI(mAuth.getCurrentUser());
                        } else {
                            Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                        }
                        registerButton.setEnabled(true);
                    }
                });
    }
    /**
     * Updates the UI after a successful sign-in.
     * @param currentUser The currently signed-in FirebaseUser.
     */
    private void actualizarUI(FirebaseUser currentUser) {
        if(currentUser != null){
            navController.navigate(R.id.signIn6);
        }
    }
    /**
     * Validates the user input form.
     * @return True if the form is valid, false otherwise.
     */

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