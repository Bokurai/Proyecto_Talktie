package com.example.proyecto_talktie;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class studentSchoolAdapter extends FirestoreRecyclerAdapter<Student, studentSchoolAdapter.studentViewHolder> {
    NavController navController;
    schoolHomeViewModel homeViewModel;


    public studentSchoolAdapter(@NonNull FirestoreRecyclerOptions<Student> options, NavController navController, schoolHomeViewModel homeViewModel) {
        super(options);
        this.navController = navController;
        this.homeViewModel = homeViewModel;

    }

    @Override
    protected void onBindViewHolder(@NonNull studentViewHolder holder, int position, @NonNull Student model) {

        holder.nameStudent.setText(model.getName());
        holder.degree.setText(model.getDegree());

        Context context1 = holder.itemView.getContext();

        String imageProfile = model.getProfileImage() != null ? model.getProfileImage() : "null";
        if (!imageProfile.equals("null")) {
            Uri uriImage = Uri.parse(imageProfile);
            Glide.with(context1)
                    .load(uriImage)
                    .into(holder.photoStudent);
        } else {
            Glide.with(context1)
                    .load(R.drawable.profile_image_defaut)
                    .into(holder.photoStudent);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.select(model);
                navController.navigate(R.id.action_goStudentSchoolProfile);
            }
        });


    }

    @NonNull
    @Override
    public studentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new studentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_student_school, parent, false));
    }

    class studentViewHolder extends RecyclerView.ViewHolder {

        TextView nameStudent, degree;
        ImageView photoStudent;

        public studentViewHolder(@NonNull View itemView) {
            super(itemView);

            nameStudent = itemView.findViewById(R.id.student_name);
            degree = itemView.findViewById(R.id.degree);
            photoStudent = itemView.findViewById(R.id.student_image);

        }
    }
}
