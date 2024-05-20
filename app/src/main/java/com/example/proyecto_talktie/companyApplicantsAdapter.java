package com.example.proyecto_talktie;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class companyApplicantsAdapter extends RecyclerView.Adapter<companyApplicantsAdapter.studentApplicantViewHolder> {
    private List<Student> studentList;
    schoolHomeViewModel schoolHomeViewModel;
    NavController navController;

    public companyApplicantsAdapter(List<Student> studentList, schoolHomeViewModel homeViewModel, NavController navController) {
        this.studentList = studentList;
        this.schoolHomeViewModel = homeViewModel;
        this.navController = navController;
    }

    @NonNull
    @Override
    public studentApplicantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new studentApplicantViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_student_school, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull studentApplicantViewHolder holder, int position) {
        // OfferObject offerObject = offerObjectList.get(position);
        Student student= studentList.get(position);
        holder.nameStudent.setText(student.getName());
        holder.degree.setText(student.getDegree());
        Context context1 = holder.itemView.getContext();


        String imageProfile = student.getProfileImage() ;
        if (imageProfile!=null && !imageProfile.isEmpty()) {
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
                schoolHomeViewModel.select(student);
                navController.navigate(R.id.action_goStudentSchoolProfile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
        notifyDataSetChanged();
    }

    public class studentApplicantViewHolder extends RecyclerView.ViewHolder {
        TextView nameStudent, degree;
        ImageView photoStudent;


        public studentApplicantViewHolder(@NonNull View itemView) {
            super(itemView);
            nameStudent = itemView.findViewById(R.id.student_name);
            degree = itemView.findViewById(R.id.degree);
            photoStudent = itemView.findViewById(R.id.student_image);
        }
    }
}
