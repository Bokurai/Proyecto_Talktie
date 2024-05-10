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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.google.firebase.firestore.FirebaseFirestore;


public class schoolLogin extends Fragment {

    private FirebaseAuth mAuth;
    NavController navController;
    private LinearLayout signInForm;
    private SignInButton googleSignInButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private EditText emailEditText, passwordEditText;
    private Button emailSignInButton, registerButton;
    public static final String EXTRA_FORCE_ACCOUNT_CHOOSER = "force_account_chooser";
    MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_school_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = view.findViewById(R.id.emailEditTextSchool);
        passwordEditText = view.findViewById(R.id.passwordEditTextSchool);
        emailSignInButton = view.findViewById(R.id.btnLogInSchool);
        signInForm = view.findViewById(R.id.linearLoginSchool);
        registerButton = view.findViewById(R.id.btnCreateAccountOneSchool);
        navController = Navigation.findNavController(view);
        googleSignInButton = view.findViewById(R.id.googleSignInButtonSchool);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn1_School);
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
// There are no request codes
                            Intent data = result.getData();
                            try {
                                firebaseAuthWithGoogle(GoogleSignIn.getSignedInAccountFromIntent(data
                                ).getResult(ApiException.class));
                            } catch (ApiException e) {
                                Log.e("ABCD", "signInResult:failed code=" +
                                        e.getStatusCode());
                            }
                        }
                    }
                });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederConGoogle();
            }
        });

        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederConEmail();
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
// There are no request codes
                            Intent data = result.getData();
                            try {
                                firebaseAuthWithGoogle(GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class));
                            } catch (ApiException e) {
                                Log.e("ABCD", "signInResult:failed code=" +
                                        e.getStatusCode());
                            }
                        }
                    }
                });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederConGoogle();
            }
        });

        mainActivity = (MainActivity) requireActivity();
        mainActivity.hideNavBot();
        mainActivity.hideNavBotSchool();

    }

    private void accederConEmail() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(requireView(), "Por favor, completa todos los campos", Snackbar.LENGTH_LONG).show();
            return;
        }

        signInForm.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            actualizarUI(mAuth.getCurrentUser());
                        } else {
                            Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                        }
                        signInForm.setVisibility(View.VISIBLE);

                    }
                });
    }

    private void actualizarUI(FirebaseUser currentUser) {
        if(currentUser != null){
            navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.schoolHome);

        }
    }

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

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        if (acct == null) {
            Log.e("firebaseAuthWithGoogle", "GoogleSignInAccount is null");
            return;
        }

        String userEmail = acct.getEmail();

        FirebaseFirestore.getInstance().collection("User")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            mAuth.signInWithCredential(GoogleAuthProvider.getCredential(acct.getIdToken(), null))
                                    .addOnCompleteListener(requireActivity(), authTask -> {
                                        if (authTask.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            actualizarUI(user);
                                        } else {
                                            Snackbar.make(requireView(), "Error: " + authTask.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Snackbar.make(requireView(), "You have to sign up first", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
}