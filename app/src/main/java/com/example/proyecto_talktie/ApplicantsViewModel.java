package com.example.proyecto_talktie;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

    public class ApplicantsViewModel extends AndroidViewModel {
        private final MutableLiveData<List<Student>> applicants = new MutableLiveData<>();
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();

        public ApplicantsViewModel(@NonNull Application application, MutableLiveData<String> name, MutableLiveData<String> degree) {
            super(application);

            this.name = name;
            this.degree = degree;
        }

        public ApplicantsViewModel(@NonNull Application application) {
            super(application);
        }

            public LiveData<List<Student>> getApplicantsForOffer(String offerId) {

                CollectionReference applicantsRef = db.collection("Offer").document(offerId).collection("Applicants");
                applicantsRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("ApplicantsViewModel", "El usuario está aplicado a la oferta");
                                } else {
                                    Log.d("ApplicantsViewModel", "El usuario no está aplicado a la oferta");
                                }
                            } else {
                                Log.e("ApplicantsViewModel", "Error al verificar si el usuario está en la subcolección de aplicantes: ", task.getException());
                            }
                        });
            return applicants;
        }

    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> degree = new MutableLiveData<>();

    public void setName(String name) {
        this.name.setValue(name);
    }

    public LiveData<String> getName() {
        return name;
    }
    public LiveData<String> getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree.setValue(degree);
    }

}
