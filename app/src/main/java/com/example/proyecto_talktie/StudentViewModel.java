package com.example.proyecto_talktie;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

                            /**Recommendation recommendation = new Recommendation(idTeacher, recommendationText);
                            recommendationList.add(recommendation);**/
                            Log.d("Recomendaciones", "Recomendación obtenida - ID del profesor: " + idTeacher + ", Texto de la recomendación: " + recommendationText);
                        }
                    }
                }
                //recommendationsLiveData.setValue(recommendationList);
            } else {
                Toast.makeText(getApplication(), "Error loading recommendations", Toast.LENGTH_SHORT).show();
            }

        });
        return recommendationsLiveData;
    }


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
