package com.example.proyecto_talktie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.company.ApplicantData;
import com.example.proyecto_talktie.models.student.Student;
import com.google.android.gms.tasks.OnSuccessListener;
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

        /**
         * Method that performs a search in the Offer collection to obtain the ID of the candidates for a specific offer.
         *  of the candidates for a specific offer.
         * @param offerId Id of the offer to search.
         */
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

        /**
         * Method that performs a search through the list of IDs passed by parameter in
         * the Students collection and retrieves its data.
         * @param ids List with the ID of the applicants.
         */
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

        /**
         * Method that performs a search in the Applicant subCollection to obtain the data of
         * a specific applicant.
         * @param offerId The ID of the offer.
         * @param applicantId The Applicant ID.
         * @return MutableLiveData with the applicant's information.
         */
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

        /**
         * Method that returns a list with the data of the students applied to an offer.
         * @return A list of students.
         */
        public MutableLiveData<List<Student>> getApplicantsData() {
            return applicants;
        }

        /**
         * Method that stores a specific student inside a MutableLiveData.
         * @param student Student ID to store.
         */
        public void select(Student student) {
            studentSelected.setValue(student);
        }

        /**
         * Method that returns a student stored in a MutableLiveData.
         * @return A MutableLiveData with a student.
         */
        public MutableLiveData<Student> selected() {return studentSelected;}


}
