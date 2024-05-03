package com.example.proyecto_talktie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.View;
import com.example.proyecto_talktie.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

        // Obtener el usuario actualmente autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Inicializar el NavController
        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();

        // Configurar el BottomNavigationView con el NavController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // Verificar si hay un usuario autenticado
        if (currentUser == null) {
            // Si no hay un usuario autenticado, navegar al fragmento de inicio de sesión
            navController.navigate(R.id.selectRegister);
        } else {
            String userId = currentUser.getUid();
            FirebaseFirestore.getInstance().collection("User").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userT = documentSnapshot.getString("userT");
                            if (userT != null) {
                                switch (userT) {
                                    case "student":
                                        navController.navigate(R.id.home);
                                        break;
                                    case "business":
                                    case "school":
                                        break;
                                }
                            }
                        }
                    });

        }
    }


    public void hideNavBot() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void showNavBot() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}