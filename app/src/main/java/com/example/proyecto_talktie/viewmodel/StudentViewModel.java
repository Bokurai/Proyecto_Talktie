package com.example.proyecto_talktie.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.school.Recommendation;
import com.example.proyecto_talktie.models.school.Teacher;
import com.example.proyecto_talktie.models.student.Student;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentViewModel extends AndroidViewModel {
    private MutableLiveData<List<Recommendation>> recommendationsLiveData = new MutableLiveData<>();
    private MutableLiveData<String> aboutStudent = new MutableLiveData<>();
    private MutableLiveData<Student> studentData = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public StudentViewModel(@NonNull Application application) {
        super(application);

    }

    /**
     * Method that extracts the student's recommendations from the collection and stores them in a mutableLiveData.
     * @param studentId Current student ID
     * @return MutableLiveData of recommendations
     */
    public MutableLiveData<List<Recommendation>> getRecommendationLiveData(String studentId) {
        CollectionReference studentRef = db.collection("Student");
        Query query = studentRef.whereEqualTo("studentId", studentId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Recommendation> recommendationList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, String> recommendationsMap = (Map<String, String>) document.get("recommendations");
                    if (recommendationsMap != null) {
                        for(Map.Entry<String, String> entry : recommendationsMap.entrySet()) {
                            String idTeacher = entry.getKey();
                            String recommendationText = entry.getValue();

                            getTeacherData(idTeacher, recommendationText, recommendationList);
                            Log.d("Recomendaciones", "Recomendación obtenida - ID del profesor: " + idTeacher + ", Texto de la recomendación: " + recommendationText);
                        }
                    }
                }
            } else {
                Toast.makeText(getApplication(), "Error loading recommendations", Toast.LENGTH_SHORT).show();
            }

        });
        return recommendationsLiveData;
    }

    /**
     * A method for obtaining information from a teacher who is associated with a recommendation.
     * @param teacherId ID of the teacher to search.
     * @param recommendationText Text with the teacher's recommendation.
     * @param recommendationList List of recommendations.
     */
    public void getTeacherData(String teacherId, String recommendationText, List<Recommendation> recommendationList) {
        db.collection("Teacher").document(teacherId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Teacher teacher = documentSnapshot.toObject(Teacher.class);
                        Recommendation recommendation = new Recommendation(recommendationText, teacher);

                        recommendationList.add(recommendation);
                        recommendationsLiveData.setValue(recommendationList);

                    }
                });
    }

    /**
     * Method that is responsible for obtaining information from a student to display it in the profile.
     * @param studentId ID of the student to search.
     * @return A MutableLiveData with the object Student.
     */
    public MutableLiveData<Student> getStudentData(String studentId) {

        db.collection("Student").document(studentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("name");
                        String about = documentSnapshot.getString("about");
                        String imageprofileURL = documentSnapshot.getString("profileImage");

                        Student student = new Student();
                        student.setName(name);
                        student.setAbout(about);
                        student.setProfileImage(imageprofileURL);

                        studentData.setValue(student);
                    }
                });

        return studentData;
    }



}
