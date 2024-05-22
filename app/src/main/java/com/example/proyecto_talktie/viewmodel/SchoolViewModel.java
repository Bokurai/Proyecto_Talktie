package com.example.proyecto_talktie.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.school.School;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SchoolViewModel extends AndroidViewModel {
    private MutableLiveData<School> schoolData = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SchoolViewModel(@NonNull Application application) {
        super(application);
    }

   public MutableLiveData<School> getSchoolData(String schoolId) {

        db.collection("School").document(schoolId)
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
    }





}
