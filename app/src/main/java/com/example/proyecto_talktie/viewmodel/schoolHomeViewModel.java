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

    public void getTeacherList() {

    }


    public void select(Student student) {
        studentSelected.setValue(student);
    }

  public   MutableLiveData<Student> selectd() {return studentSelected;}


}
