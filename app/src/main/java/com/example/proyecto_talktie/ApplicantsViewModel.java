package com.example.proyecto_talktie;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

    public class ApplicantsViewModel extends AndroidViewModel {
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private MutableLiveData<Student> studentSelected = new MutableLiveData<>();
        private MutableLiveData<ApplicantData> applicantDataMutableLiveData = new MutableLiveData<>();
        String offerId = "";
        MutableLiveData<List<Student>> applicants = new MutableLiveData<>();

        public ApplicantsViewModel(@NonNull Application application) {
            super(application);
        }

        public String getOfferId() {
            return offerId;
        }

        public void setOfferId(String offerId) {
            this.offerId = offerId;
        }

        public void getApplicantsIds(String offerId) {
            db.collection("Offer").document(offerId).collection("Applicants").get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            List<String> applicantsIds = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String id = document.getId();
                                applicantsIds.add(id);
                            }
                            getApplicants(applicantsIds);
                        }
                    });
        }

        public void getApplicants(List<String> ids) {
            if (ids.size() == 0) {
                applicants.setValue(new ArrayList<>());
            } else {
                List<Student> students = new ArrayList<>();
                for (String id : ids) {
                    db.collection("Student").document(id).get()
                            .addOnCompleteListener(task -> {
                                Student student = task.getResult().toObject(Student.class);
                                students.add(student);

                                if (students.size() == ids.size()) {
                                    applicants.setValue(students);
                                }
                            });

                }
            }
        }

        public MutableLiveData<ApplicantData> dataApplicant(String offerId, String applicantId) {
            db.collection("Offer").document(offerId).collection("Applicants").document(applicantId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ApplicantData data = documentSnapshot.toObject(ApplicantData.class);
                            applicantDataMutableLiveData.setValue(data);
                        }
                    });
            return applicantDataMutableLiveData;
        }

        public MutableLiveData<List<Student>> getApplicantsData() {
            return applicants;
        }

        public void select(Student student) {
            studentSelected.setValue(student);
        }

        MutableLiveData<Student> selectd() {return studentSelected;}


}
