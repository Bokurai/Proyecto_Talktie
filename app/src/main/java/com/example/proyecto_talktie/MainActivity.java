package com.example.proyecto_talktie;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.proyecto_talktie.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.view.MenuItem;
import com.example.proyecto_talktie.databinding.FragmentLoginBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ActivityMainBinding binding; //para navigation button
    NavController navController;
    BottomNavigationView bottomNavigationView, bottomNavigationViewCompany, bottomNavSchool ;

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
        NavigationUI.setupWithNavController(binding.bottomNavViewCompany, navController);
        NavigationUI.setupWithNavController(binding.bottomNavViewSchool, navController);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationViewCompany = findViewById(R.id.bottom_nav_view_company);
        bottomNavSchool = findViewById(R.id.bottom_nav_view_school);

        //Botones Student
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Navegar al fragmento de inicio
                    navController.navigate(R.id.action_goHome);
                    return true;
                } else if (itemId == R.id.offer) {
                    // Navegar al fragmento de ofertas
                    navController.navigate(R.id.action_goOffer);
                    return true;
                } else if (itemId == R.id.messagesHome) {
                    // Navegar al fragmento de mensajes
                    navController.navigate(R.id.action_to_MessagesHome);
                    return true;
                } else if (itemId == R.id.profileStudent) {
                    // Navegar al fragmento de perfil
                    navController.navigate(R.id.action_goProfileStudent);
                    return true;
                }
                return false;
            }
        });

        //Botones Company
        bottomNavigationViewCompany.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.companyHomeFragment) {
                    // Navegar al fragmento de inicio
                    navController.navigate(R.id.action_goCompanyHome);
                    return true;
                } else if (itemId == R.id.SchoolViewCompany) {
                    // Navegar al fragmento de ofertas
                    navController.navigate(R.id.action_goCompanySchool);
                    return true;
                } else if (itemId == R.id.companyMessages) {
                    // Navegar al fragmento de mensajes
                    navController.navigate(R.id.action_goCompanyMessages);
                    return true;
                } else if (itemId == R.id.profileCompanySelf) {
                    // Navegar al fragmento de perfil
                    navController.navigate(R.id.action_goCompanyProfileSelf);
                    return true;
                }
                return false;
            }
        });

        //Botones School
        bottomNavSchool.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.recommends) {
                    // Navegar al fragmento de inicio
                    navController.navigate(R.id.action_goSchoolHome);
                    return true;
                } else if (itemId == R.id.companyViewSchool) {
                    // Navegar al fragmento de ofertas
                    navController.navigate(R.id.action_goCompanyViewSchool);
                    return true;
                } else if (itemId == R.id.messagesSchool) {
                    // Navegar al fragmento de mensajes
                    navController.navigate(R.id.action_goSchoolMessages);
                    return true;
                } else if (itemId == R.id.profileSchool) {
                    // Navegar al fragmento de perfil
                    navController.navigate(R.id.action_goSchoolProfile);
                    return true;
                }
                return false;
            }
        });

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
                                        Snackbar.make(findViewById(android.R.id.content), "Signed up in the app using the email " + currentUser.getEmail(), Snackbar.LENGTH_LONG).show();
                                        break;
                                    case "company":
                                        navController.navigate(R.id.action_goCompanyHome);
                                        Snackbar.make(findViewById(android.R.id.content), "Signed up in the app using the email " + currentUser.getEmail(), Snackbar.LENGTH_LONG).show();
                                        break;
                                    case "school":
                                        navController.navigate(R.id.action_goSchoolHome);
                                        Snackbar.make(findViewById(android.R.id.content), "Signed up in the app using the email " + currentUser.getEmail(), Snackbar.LENGTH_LONG).show();
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

    public void hideNavBotSchool() {
        bottomNavSchool.setVisibility(View.GONE);
    }
    public void showNavBotSchool() {
        bottomNavSchool.setVisibility(View.VISIBLE);
    }

    public void hideNavBotComp() {
        bottomNavigationViewCompany.setVisibility(View.GONE);
    }

    public void showNavBotComp() {
        bottomNavigationViewCompany.setVisibility(View.VISIBLE);
    }
}