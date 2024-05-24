package com.example.proyecto_talktie.view.school_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.school.Teacher;
import com.example.proyecto_talktie.models.student.Student;
import com.example.proyecto_talktie.viewmodel.TeacherViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * Fragment for adding a recommendation for a student by a teacher.
 */
public class addRecommendation extends Fragment {
    private ImageView backArrow, teacherImage, studentImage;
    private TextView nameTacher, nameStudent;
    private EditText recommendationText;
    private AppCompatButton cancel, save;
    private Student student;
    private String studentId;
    Context context;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backArrow = view.findViewById(R.id.img_return);
        teacherImage = view.findViewById(R.id.photo_teacher);
        studentImage = view.findViewById(R.id.photo_student);
        nameTacher = view.findViewById(R.id.txtName_teacher);
        nameStudent = view.findViewById(R.id.txtName_student);
        cancel = view.findViewById(R.id.btnCancel);
        save = view.findViewById(R.id.btnSave);
        recommendationText = view.findViewById(R.id.txtRecommendation);
        navController = Navigation.findNavController(view);

        context = getView().getContext();

        //Initialize the ViewModel
        TeacherViewModel teacherViewModel = new ViewModelProvider(requireActivity()).get(TeacherViewModel.class);

        //Gets the student stored in the viewModel
        student = teacherViewModel.getStudent();
        studentId = student.getStudentId();

        //Update the interface with the data
        nameStudent.setText(student.getName());
        String imageStudent = student.getProfileImage();

        //If the image is null, the default will be set
        if (imageStudent != null && !imageStudent.isEmpty()) {
            Uri uriImage = Uri.parse(imageStudent);
            Glide.with(context)
                    .load(uriImage)
                    .into(studentImage);
        } else {
            Glide.with(context)
                    .load(R.drawable.profile_image_defaut)
                    .into(studentImage);
        }

        //Navigate to the student profile
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goStudentSchoolProfile);
            }
        });

        /**
         * Observe the selected teacher from the ViewModel.
         * Update UI with selected teacher data.
         * Handle cancel button click to navigate back.
         * Handle save button click to save recommendation to Firestore and update student profile.
         */
        teacherViewModel.selected().observe(getViewLifecycleOwner(), new Observer<Teacher>() {
            @Override
            public void onChanged(Teacher teacher) {

                //Update teacher information
                nameTacher.setText(teacher.getName());
                String imageProfile = teacher.getProfileImage();

                //If the image is null, the default will be set
                if (imageProfile != null && !imageProfile.isEmpty()) {
                    Uri uriImage = Uri.parse(imageProfile);
                    Glide.with(context)
                            .load(uriImage)
                            .into(teacherImage);
                } else {
                    Glide.with(context)
                            .load(R.drawable.teacher_default)
                            .into(teacherImage);
                }

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navController.popBackStack();
                    }
                });

                //Button to save the recommendation made
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        //Gets the text of the recommendation
                        String recommendation = recommendationText.getText().toString().trim();
                        String teacherId = teacher.getTeacherId();

                        //Create the recommendation in the database
                        db.collection("Student")
                                .document(studentId)
                                .update("recommendations."+teacherId, recommendation)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        db.collection("Teacher").document(teacherId)
                                                .update("recommendedStudents", FieldValue.arrayUnion(studentId))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                db.collection("Student").document(studentId).get()
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                Student updatedStudent = documentSnapshot.toObject(Student.class);
                                                                                //Save the student updated
                                                                                teacherViewModel.setStudent(updatedStudent);
                                                                                Toast.makeText(getContext(), "Recommendation added to student profile", Toast.LENGTH_SHORT).show();
                                                                                navController.popBackStack();
                                                                            }});
                                                            }
                                                        })
                                                .addOnFailureListener(e -> {
                                                    Log.d("ERROR", "error to update recommendation", e);
                                                });
                                    }
                                });
                    }
                });
            }
        });
    }
}