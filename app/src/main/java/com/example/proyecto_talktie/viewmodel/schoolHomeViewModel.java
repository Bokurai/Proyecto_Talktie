package com.example.proyecto_talktie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.student.Student;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class schoolHomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Student>> students = new MutableLiveData<>();
    private MutableLiveData<Student> studentSelected = new MutableLiveData<>();
    private MutableLiveData<String> schoolName = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public schoolHomeViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Method that obtains the name of the school that is currently logged in.
     * @return A MutableLiveData with the name of the school.
     */
    public MutableLiveData<String> getNameSchool(){
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            db.collection("School").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String name = documentSnapshot.getString("name");
                            schoolName.setValue(name);
                        }
                    });
        } else {
            schoolName.setValue(null);
        }
        return schoolName;
    }

    /**
     * Method that stores a specific student inside a MutableLiveData.
     * @param student Student ID to store
     */
    public void select(Student student) {
        studentSelected.setValue(student);
    }

    /**
     * Method that returns a student stored in a MutableLiveData.
     * @return A MutableLiveData with a student.
     */
    public   MutableLiveData<Student> selected() {return studentSelected;}


}
