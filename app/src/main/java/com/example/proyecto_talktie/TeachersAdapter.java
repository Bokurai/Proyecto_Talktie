package com.example.proyecto_talktie;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
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

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeacherViewHolder> {
    private List<Teacher> teacherList;
    private Context context;
    NavController navController;
    TeacherViewModel teacherViewModel;

    public TeachersAdapter(List<Teacher> teacherList, Context context, NavController navController, TeacherViewModel teacherViewModel) {
        this.teacherList = teacherList;
        this.context = context;
        this.navController = navController;
        this.teacherViewModel = teacherViewModel;
    }

    @NonNull
    @Override
    public TeachersAdapter.TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeachersAdapter.TeacherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_teacher, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeachersAdapter.TeacherViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);

        holder.name.setText(teacher.getName());

        boolean emailIsEmpty = isNullOrEmpty(teacher.getEmail());
        boolean positionIsEmpty = isNullOrEmpty(teacher.getPosition());

        if (emailIsEmpty || positionIsEmpty) {
            holder.separator.setVisibility(View.GONE);
        } else {
            holder.separator.setVisibility(View.VISIBLE);
        }

        if (!emailIsEmpty) {
            holder.email.setText(teacher.getEmail());
            holder.email.setVisibility(View.VISIBLE);
        } else {
            holder.email.setVisibility(View.GONE);
        }

        if (!positionIsEmpty) {
            holder.postiion.setText(teacher.getPosition());
            holder.postiion.setVisibility(View.VISIBLE);
        } else {
            holder.postiion.setVisibility(View.GONE);
        }

       String imageProfile = teacher.getProfileImage();
        if (imageProfile != null && !imageProfile.isEmpty()) {
            Uri uriImage = Uri.parse(imageProfile);
            Glide.with(context)
                    .load(uriImage)
                    .into(holder.imageProfile);
        } else {
            Glide.with(context)
                    .load(R.drawable.teacher_default)
                    .into(holder.imageProfile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherViewModel.select(teacher);
                navController.navigate(R.id.action_goTeacherProfile);
            }
        });

    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
        notifyDataSetChanged();
    }

    class TeacherViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProfile;
        TextView name, postiion, email, separator;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.teacher_image);
            name = itemView.findViewById(R.id.teacher_name);
            postiion = itemView.findViewById(R.id.position_teacher);
            email = itemView.findViewById(R.id.email_teacher);
            separator = itemView.findViewById(R.id.separatorT);

        }
    }

}
