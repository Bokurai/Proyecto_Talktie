package com.example.proyecto_talktie;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeacherViewModel extends AndroidViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<Teacher> teacherSingle = new MutableLiveData<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userId = mAuth.getUid();
    private String studentId = "";

    private MutableLiveData<List<Teacher>> teachersLiveData = new MutableLiveData<>();

    public TeacherViewModel(@NonNull Application application) {
        super(application);
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public MutableLiveData<List<Teacher>> getTeachers() {
        db.collection("Teacher")
                .whereEqualTo("schoolId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Teacher> teachers = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Teacher teacher = document.toObject(Teacher.class);
                        teachers.add(teacher);
                    }

                    if (teachers.size() == queryDocumentSnapshots.size()) {
                        Collections.sort(teachers, ((o1, o2) -> o2.getName().compareTo(o1.getName())));

                        teachersLiveData.setValue(teachers);
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(), "Error loading teachers", Toast.LENGTH_SHORT).show();
                });
        return teachersLiveData;
    }

    public void select(Teacher teacher){
        teacherSingle.postValue(teacher);
    }

    MutableLiveData<Teacher> selected() {
        return teacherSingle;
    }

}
