package com.example.proyecto_talktie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.example.proyecto_talktie.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.Nullable;

/**
 * Fragment class for handling user login functionality.
 * It supports email/password login and Google Sign-In.
 */
public class Login extends Fragment {
    private FirebaseAuth mAuth;
    NavController navController;
    private LinearLayout signInForm;
    private SignInButton googleSignInButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private EditText emailEditText, passwordEditText;
    private TextView  iamcompany, iamschool;
    private Button emailSignInButton, registerButton;
    public static final String EXTRA_FORCE_ACCOUNT_CHOOSER = "force_account_chooser";
    MainActivity mainActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        emailSignInButton = view.findViewById(R.id.btnLogIn);
        signInForm = view.findViewById(R.id.linearLogin);
        registerButton = view.findViewById(R.id.btnCreateAccountOne);
        navController = Navigation.findNavController(view);
        googleSignInButton = view.findViewById(R.id.googleSignInButton);
        iamcompany = view.findViewById(R.id.imcompany);
        iamschool = view.findViewById(R.id.imschool);
        signInForm = view.findViewById(R.id.linearLogin);

        iamcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_gocompanyLogin);
            }
        });

        iamschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goSLoginSchool);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signIn1);
            }
        });
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
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
            public void onClick(View view) {
                accederConGoogle();
            }
        });
        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accederConEmail();
            }
        });
        googleSignInButton = view.findViewById(R.id.googleSignInButton);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
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
            public void onClick(View view) {
                accederConGoogle();
            }
        });
        mainActivity = (MainActivity) requireActivity();
        mainActivity.hideNavBot();
        mainActivity.hideNavBotComp();
        mainActivity.hideNavBotSchool();

    }
    /**
     * Method to manage the email and login password.
     */
    private void accederConEmail() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(requireView(), "Por favor, completa todos los campos", Snackbar.LENGTH_LONG).show();
            return;
        }

        signInForm.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                verificarTipoUsuario(user);
                            }
                        } else {
                            Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                            signInForm.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * Method that verifies the type of the logged-in user.
     * @param user The currently logged-in user.
     */
    private void verificarTipoUsuario(FirebaseUser user) {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("User").document(user.getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String userType = documentSnapshot.getString("userT");
                    if ("student".equals(userType)) {
                        actualizarUI(user);
                    } else {
                        Snackbar.make(requireView(), "Only student login, please use one of the other login types listed in the bottom of the screen", Snackbar.LENGTH_LONG).show();
                        mAuth.signOut();
                        signInForm.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
    /**
     * Updates the UI after a successful login.
     * @param currentUser The currently logged-in user.
     */
    private void actualizarUI(FirebaseUser currentUser) {
        if(currentUser != null){
            navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.home);

        }
    }
    /**
     *Method that initiates the process of logging in to Google.
     */
    private void accederConGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            googleSignInClient.revokeAccess().addOnCompleteListener(task2 -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                signInIntent.putExtra(EXTRA_FORCE_ACCOUNT_CHOOSER, true);
                activityResultLauncher.launch(signInIntent);
            });
        });
    }

    /**
     * Method that handles Firebase authentication with a Google account.
     * @param acct The Google account used for authentication.
     */
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