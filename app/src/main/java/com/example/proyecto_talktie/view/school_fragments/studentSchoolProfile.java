package com.example.proyecto_talktie.view.school_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.student.Student;
import com.example.proyecto_talktie.view.adapters.AddRecommendationAdapter;
import com.example.proyecto_talktie.viewmodel.TeacherViewModel;
import com.example.proyecto_talktie.viewmodel.schoolHomeViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class studentSchoolProfile extends Fragment {
    private RecyclerView recyclerView;
    private TeacherViewModel teacherViewModel;
    private AddRecommendationAdapter adapter;
    private CardView cardAbout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView photoProfile, imageReturn;
    private TextView name, email, phone, about, separator;
    private schoolHomeViewModel homeViewModel;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(schoolHomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_school_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageReturn = view.findViewById(R.id.img_returnS);
        navController = Navigation.findNavController(view);

        recyclerView = view.findViewById(R.id.listTRecyclerView);

        photoProfile = view.findViewById(R.id.profileImgStudentS);
        name = view.findViewById(R.id.txtStudentNameS);
        email = view.findViewById(R.id.emailS);
        phone = view.findViewById(R.id.phoneS);
        about = view.findViewById(R.id.txtDescriptionS);
        separator = view.findViewById(R.id.separator);
        cardAbout = view.findViewById(R.id.cardAbout);

        homeViewModel.selected().observe(getViewLifecycleOwner(), new Observer<Student>() {
            @Override
            public void onChanged(Student student) {

                teacherViewModel = new ViewModelProvider(requireActivity()).get(TeacherViewModel.class);

                db.collection("Student").document(student.getStudentId()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Student updatedStudent = documentSnapshot.toObject(Student.class);
                                teacherViewModel.setStudent(updatedStudent);
                            }});

                adapter = new AddRecommendationAdapter(new ArrayList<>(), getContext(), navController, teacherViewModel, student.getStudentId());

                teacherViewModel.getTeachers().observe(getViewLifecycleOwner(), teachers -> {
                    adapter.setTeacherList(teachers);
                });

                recyclerView.setAdapter(adapter);


                name.setText(student.getName());

                if (student.getAbout() != null && !student.getAbout().isEmpty()) {
                    cardAbout.setVisibility(View.VISIBLE);
                    about.setText(student.getAbout());
                }

                if (student.getEmail() == null) {
                    email.setVisibility(View.GONE);

                } else {
                   email.setText(student.getEmail());
                }

                if (student.getPhone() == null) {
                    phone.setVisibility(View.GONE);
                } else {
                    phone.setText(student.getPhone());
                }

                if (TextUtils.isEmpty(student.getEmail()) || TextUtils.isEmpty(student.getPhone())) {
                    separator.setVisibility(View.GONE);
                }

                Context context = getView().getContext();
                String imageProfile = student.getProfileImage();
                if (imageProfile != null && !imageProfile.isEmpty()) {
                    Uri uriImage = Uri.parse(imageProfile);
                    Glide.with(context)
                            .load(uriImage)
                            .into(photoProfile);
                } else {
                    Glide.with(context)
                            .load(R.drawable.profile_image_defaut)
                            .into(photoProfile);
                }


                imageReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navController.popBackStack();
                    }
                });

            }
        });

    }
}