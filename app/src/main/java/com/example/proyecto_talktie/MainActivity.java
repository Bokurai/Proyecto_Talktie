package com.example.proyecto_talktie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto_talktie.databinding.ActivityMainBinding;
import com.example.proyecto_talktie.databinding.FragmentLoginBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ActivityMainBinding binding; //para navigation button
    NavController navController;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());

        mAuth = FirebaseAuth.getInstance();

        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);

    }
    public void hideNavBot() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void showNavBot() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }





    /*
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(MainActivity.this, Login.class));
        }else {
            startActivity(new Intent(MainActivity.this, SelectRegister.class));
        }
    }*/
}