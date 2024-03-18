package com.example.proyecto_talktie;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentViewModel extends AndroidViewModel {
    private MutableLiveData<List<Recommendation>> recommendationsLiveData = new MutableLiveData<>();
    private MutableLiveData<String> aboutStudent = new MutableLiveData<>();

    public StudentViewModel(@NonNull Application application) {
        super(application);

    }

    /**
     * Method that extracts the student's recommendations from the collection and stores them in a mutableLiveData.
     * @param studentId Current student ID
     * @return MutableLiveData of recommendations
     */
    public MutableLiveData<List<Recommendation>> getRecommendationLiveData(String studentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                            Recommendation recommendation = new Recommendation(idTeacher, recommendationText);
                            recommendationList.add(recommendation);
                        }
                    }
                }
                recommendationsLiveData.setValue(recommendationList);
            } else {
                Toast.makeText(getApplication(), "Error loading recommendations", Toast.LENGTH_SHORT).show();
            }
        });
        return recommendationsLiveData;
    }

    /**
     * Method that extracts from the student collection the student's description and stores it in a mutableLiveData.
     * @param studentId Current student ID
     * @return MutableLiveData of description
     */
    public MutableLiveData<String> getAbout(String studentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference studentRef = db.collection("Student");
        Query query = studentRef.whereEqualTo("studentId", studentId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);
                    String about = document.getString("about");
                    if (about != null) {
                        aboutStudent.setValue(about);
                    }
                }
            } else {
                Toast.makeText(getApplication(), "Error loading on user", Toast.LENGTH_SHORT).show();
            }
        });
        return aboutStudent;
    }

}
