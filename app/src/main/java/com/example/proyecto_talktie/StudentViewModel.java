package com.example.proyecto_talktie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Map;

public class StudentViewModel extends AndroidViewModel {

    MutableLiveData<Student> studentLiveData = new MutableLiveData<>();

    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> currentUserLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, String>> studentRecommendationLD = new MutableLiveData<>();

    ListenerRegistration studentListenerRegistration;
    public StudentViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        currentUserLiveData.setValue(mAuth.getCurrentUser());

    }

    LiveData<Map<String, String>> getStudentRecommendationLD(String studentId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference studentRef = db.collection("Student").document(studentId);

        studentListenerRegistration = studentRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                // Manejar el error
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                Map<String, String> recommendations = (Map<String, String>) documentSnapshot.get("recommendations");
                studentRecommendationLD.setValue(recommendations);
            }

        });

        return studentRecommendationLD;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (studentListenerRegistration != null) {
            studentListenerRegistration.remove();
        }
    }

    public LiveData<FirebaseUser> getCurrentUserLiveData() {
        return currentUserLiveData;
    }

    public void showUserData() {

    }

    public void showRecommendations() {


    }


}
