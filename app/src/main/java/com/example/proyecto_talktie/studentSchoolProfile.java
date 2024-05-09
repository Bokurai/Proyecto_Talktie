package com.example.proyecto_talktie;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class studentSchoolProfile extends Fragment {

    private ImageView photoProfile, imageReturn;
    private TextView name, email, phone, about;
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

        photoProfile = view.findViewById(R.id.profileImgStudentS);
        name = view.findViewById(R.id.txtStudentNameS);
        email = view.findViewById(R.id.emailS);
        phone = view.findViewById(R.id.phoneS);
        about = view.findViewById(R.id.txtDescriptionS);

        homeViewModel.selectd().observe(getViewLifecycleOwner(), new Observer<Student>() {
            @Override
            public void onChanged(Student student) {

                name.setText(student.getName());
                email.setText(student.getEmail());
                phone.setText(student.getPhone());
                about.setText(student.getAbout());

                Context context = getView().getContext();
                String imageProfile = student.getProfileImage() != null ? student.getProfileImage() : "null";
                if (!imageProfile.equals("null")) {
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