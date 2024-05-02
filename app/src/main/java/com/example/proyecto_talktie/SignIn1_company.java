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

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proyecto_talktie.databinding.FragmentSignIn1Binding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignIn1_company extends Fragment {

    FragmentSignIn1Binding binding;
    private Button registerButton;
    NavController navController;
    private FirebaseAuth mAuth;
    ActivityResultLauncher activityResultLauncher;
    private TextView haveaccount;
    SignInButton signUpButton;
    private EditText emailEditText, passwordEditText, nameEditText;


    //IMPORTANTE Falta añadir el view model

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = view.findViewById(R.id.etEmail);
        passwordEditText = view.findViewById(R.id.etPassword);
        nameEditText = view.findViewById(R.id.etName);
        registerButton = view.findViewById(R.id.btnSingIn);
        registerButton = view.findViewById(R.id.googleSignUpButton);
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
}