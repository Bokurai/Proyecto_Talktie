package com.example.proyecto_talktie.view.adapters;

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
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.school.Teacher;
import com.example.proyecto_talktie.viewmodel.TeacherViewModel;

import java.util.List;
/**
 * Adapter to display a list of teachers in a RecyclerView.
 * This adapter handles the binding of teacher data to the corresponding views in each item of the RecyclerView.
 * It uses the TeacherViewHolder to hold the views for each item, and manages the interaction with the teacher list, context,
 * navigation controller, and ViewModel.
 */

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeacherViewHolder> {
    private List<Teacher> teacherList;
    private Context context;
    NavController navController;
    TeacherViewModel teacherViewModel;
    /**
     * Constructor for the adapter.
     * @param teacherList     List of teachers to be displayed.
     * @param context         Context of the application.
     * @param navController   Navigation controller to handle navigation.
     * @param teacherViewModel ViewModel to share data between fragments.
     */

    public TeachersAdapter(List<Teacher> teacherList, Context context, NavController navController, TeacherViewModel teacherViewModel) {
        this.teacherList = teacherList;
        this.context = context;
        this.navController = navController;
        this.teacherViewModel = teacherViewModel;
    }
    /**
     * Method called to create a new ViewHolder.
     */
    @NonNull
    @Override
    public TeachersAdapter.TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeachersAdapter.TeacherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_teacher, parent, false));
    }
    /**
     * Method called to bind the teacher data to the views in the ViewHolder.
     * @param holder   ViewHolder to bind with data.
     * @param position Position of the item in the RecyclerView.
     */
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
    /**
     * Utility method to check if a string is null or empty.
     * @param string The string to check.
     * @return True if the string is null or empty, false otherwise.
     */
    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Method to get the number of items in the RecyclerView.
     * @return The number of items in the teacher list.
     */
    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    /**
     * Method to update the list of teachers.
     * @param teacherList The new list of teachers.
     */
    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for the items in the RecyclerView.
     */
    class TeacherViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView name, postiion, email, separator;

        /**
         * Constructor for the ViewHolder.
         * @param itemView The view of the item.
         */
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
