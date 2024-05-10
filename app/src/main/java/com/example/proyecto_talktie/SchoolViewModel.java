package com.example.proyecto_talktie;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SchoolViewModel extends AndroidViewModel {
    private MutableLiveData<School> schoolData = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public SchoolViewModel(@NonNull Application application) {
        super(application);
    }

   /* public MutableLiveData<School> getSchoolData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        db.collection("School").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        School school = documentSnapshot.toObject(School.class);
                        schoolData.setValue(school);
                    }
                }).addOnFailureListener(e -> {
                    Log.d("ERROR", "Error obtener información de la empresa");
                });

        return schoolData;
    }*/





}
