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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeacherViewHolder> {
    private List<Teacher> teacherList;
    private Context context;

    public TeachersAdapter(List<Teacher> teacherList, Context context) {
        this.teacherList = teacherList;
        this.context = context;
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

        if (teacher.getEmail() == null) {
            holder.email.setVisibility(View.GONE);

        } else {
            holder.email.setText(teacher.getEmail());
        }

        if (teacher.getPosition() == null) {
            holder.postiion.setVisibility(View.GONE);
        } else {
            holder.postiion.setText(teacher.getPosition());
        }

        if (TextUtils.isEmpty(teacher.getEmail()) || TextUtils.isEmpty(teacher.getPosition())) {
            holder.separator.setVisibility(View.GONE);
        }

       String imageProfile = teacher.getProfileImage();
        if (!imageProfile.equals("null")) {
            Uri uriImage = Uri.parse(imageProfile);
            Glide.with(context)
                    .load(uriImage)
                    .into(holder.imageProfile);
        } else {
            Glide.with(context)
                    .load(R.drawable.teacher_default)
                    .into(holder.imageProfile);
        }
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
