package com.example.proyecto_talktie.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.school.Teacher;
import com.example.proyecto_talktie.models.student.Student;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private Student student = new Student();

    private MutableLiveData<List<Teacher>> teachersLiveData = new MutableLiveData<>();

    public TeacherViewModel(@NonNull Application application) {
        super(application);
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public MutableLiveData<List<Student>> getRecommendationTeachers(String teacherId) {
        MutableLiveData<List<Student>> listStudents = new MutableLiveData<>();

        db.collection("Teacher").document(teacherId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> recommendedStudents = (List<String>) documentSnapshot.get("recommendedStudents");

                        if (recommendedStudents != null && !recommendedStudents.isEmpty()) {

                            List<Student> students = new ArrayList<>();
                            for (String studentId : recommendedStudents) {
                                db.collection("Student").document(studentId).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Student student = documentSnapshot.toObject(Student.class);
                                                students.add(student);

                                                if (students.size() == recommendedStudents.size()) {
                                                    Collections.sort(students, ((o1, o2) -> o2.getName().compareTo(o1.getName())));

                                                    listStudents.setValue(students);
                                                }
                                            }
                                        });
                            }
                        }

                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("TAG", "Error al cargar profesor", e);
                });
        return listStudents;
    }

    public void select(Teacher teacher){
        teacherSingle.postValue(teacher);
    }

   public MutableLiveData<Teacher> selected() {
        return teacherSingle;
    }

}
