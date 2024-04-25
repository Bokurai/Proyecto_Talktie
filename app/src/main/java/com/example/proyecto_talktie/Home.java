package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;


public class Home extends Fragment {

    MainActivity mainActivity;
    NavController navController;
    LinearLayout search_bar;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ifUserFromGoogle(user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBot();

        search_bar = view.findViewById(R.id.search_bar);

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goStudentSearchView);
            }
        });


    }


    /**
     * Method that questions if an user has used the Google Login and it redirects them to the register session
     * @param user
     */
    private void ifUserFromGoogle(FirebaseUser user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       String uidUserGoogle = user.getUid();
        db.collection("User")
                .document(uidUserGoogle)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(!task.getResult().exists()){
                            mainActivity.hideNavBot();
                            navController.navigate(R.id.action_goSelectRegister);
                        }
                    }
                });
    }
}