package com.example.proyecto_talktie.view.school_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;


public class EditRecommendation extends Fragment {
    private ImageView backArrow, studentImage, teacherImage;
    private TextView studentName, teacherName, txtRecommendation;
    private EditText editTextRecommendation;
    private String newRecommendation = "";
    private String studentId, teacherId;
    private Student student;
    private Context context;
    private TeacherViewModel teacherViewModel;
    private AppCompatButton btnDelete, btnEdit, btnSave, btnCancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edite_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        backArrow = view.findViewById(R.id.img_return);
        studentImage = view.findViewById(R.id.photo_student);
        studentName = view.findViewById(R.id.txtName_student);

        teacherImage = view.findViewById(R.id.photo_teacher);
        teacherName = view.findViewById(R.id.txtName_teacher);
        txtRecommendation = view.findViewById(R.id.txtRecommendation);

        btnDelete = view.findViewById(R.id.btnDetele);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave);

        context = getView().getContext();

        teacherViewModel = new ViewModelProvider(requireActivity()).get(TeacherViewModel.class);

        student = teacherViewModel.getStudent();
        studentId = student.getStudentId();

        studentName.setText(student.getName());
        String imageStudent = student.getProfileImage();

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

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        teacherViewModel.selected().observe(getViewLifecycleOwner(), new Observer<Teacher>() {
            @Override
            public void onChanged(Teacher teacher) {

                teacherId = teacher.getTeacherId();

                String recommendation = student.getRecommendations().get(teacherId);
                txtRecommendation.setText(recommendation);

                teacherName.setText(teacher.getName());
                String imageProfile = teacher.getProfileImage();

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



                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to remove the recommendation?")
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        deleteRecommendation(studentId, teacherId);
                                        Toast.makeText(getContext(), "Recommendation deleted from student profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnDelete.setVisibility(View.GONE);
                        btnEdit.setVisibility(View.GONE);

                        btnSave.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.VISIBLE);

                        if (editTextRecommendation == null) {
                            editTextRecommendation = new EditText(requireActivity());
                        }

                        editTextRecommendation.setText(txtRecommendation.getText());

                        //remplazar el texview con el editText
                        ViewGroup parent = (ViewGroup) txtRecommendation.getParent();
                        int index = parent.indexOfChild(txtRecommendation);
                        parent.removeView(txtRecommendation);
                        parent.addView(editTextRecommendation, index);

                        editTextRecommendation.requestFocus();
                        editTextRecommendation.selectAll();

                        //actualizar newAbout
                        newRecommendation = txtRecommendation.getText().toString().trim();

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //volver a un textView
                        ViewGroup parent = (ViewGroup) editTextRecommendation.getParent();
                        int index = parent.indexOfChild(editTextRecommendation);
                        parent.removeView(editTextRecommendation);
                        parent.addView(txtRecommendation, index);

                        btnDelete.setVisibility(View.VISIBLE);
                        btnEdit.setVisibility(View.VISIBLE);

                        btnSave.setVisibility(View.GONE);
                        btnCancel.setVisibility(View.GONE);

                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String updateRecommendation = editTextRecommendation.getText().toString().trim();

                        if (!updateRecommendation.isEmpty()) {
                            if (!updateRecommendation.equals(newRecommendation)) {
                                newRecommendation = updateRecommendation;

                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                db.collection("Student").document(studentId)
                                        .update("recommendations."+teacherId, newRecommendation)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("UPDATE", "recomendación actualizada");
                                            }
                                        });
                                //volver a un textView
                                ViewGroup parent = (ViewGroup) editTextRecommendation.getParent();
                                int index = parent.indexOfChild(editTextRecommendation);
                                parent.removeView(editTextRecommendation);
                                parent.addView(txtRecommendation, index);

                                btnDelete.setVisibility(View.VISIBLE);
                                btnEdit.setVisibility(View.VISIBLE);

                                btnSave.setVisibility(View.GONE);
                                btnCancel.setVisibility(View.GONE);


                                txtRecommendation.setText(newRecommendation);

                            }
                        }

                    }
                });

            }
        });






    }

    public void deleteRecommendation(String studentId, String teacherId) {

        DocumentReference studentRef = db.collection("Student").document(studentId);
        DocumentReference teacherRef = db.collection("Teacher").document(teacherId);

        studentRef.get()
                .addOnSuccessListener(studentSnapshot -> {
                    Map<String, String> recommendations = (Map<String, String>) studentSnapshot.get("recommendations");
                    if (recommendations != null) {
                        recommendations.remove(teacherId);
                    }

                    studentRef.update("recommendations", recommendations)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    teacherRef.get()
                                            .addOnSuccessListener(teacherSnapshot -> {
                                                List<String> recommendedStudents = (List<String>) teacherSnapshot.get("recommendedStudents");
                                                recommendedStudents.remove(studentId);

                                                teacherRef.update("recommendedStudents", recommendedStudents)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("ACTUALIZACIÓN", "Recomendación eliminada");
                                                                navController.popBackStack();
                                                            }
                                                        });
                                            });
                                }
                            });
                });

    }

}