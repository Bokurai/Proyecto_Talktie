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
import com.example.proyecto_talktie.models.student.Student;
import com.example.proyecto_talktie.viewmodel.schoolHomeViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
/**
 * Adapter to display a list of students in a RecyclerView, using FirestoreRecyclerAdapter.
 */

public class studentSchoolAdapter extends FirestoreRecyclerAdapter<Student, studentSchoolAdapter.studentViewHolder> {
    NavController navController;
    schoolHomeViewModel homeViewModel;

    /**
     * Constructor for the adapter.
     * @param options       Options for the FirestoreRecyclerAdapter.
     * @param navController Navigation controller to handle navigation.
     * @param homeViewModel ViewModel to share data between fragments.
     */
    public studentSchoolAdapter(@NonNull FirestoreRecyclerOptions<Student> options, NavController navController, schoolHomeViewModel homeViewModel) {
        super(options);
        this.navController = navController;
        this.homeViewModel = homeViewModel;

    }
    /**
     * Method called to bind the student data to the views in the ViewHolder.
     * @param holder   ViewHolder to bind with data.
     * @param position Position of the item in the RecyclerView.
     * @param model    Student object containing the student data.
     */
    @Override
    protected void onBindViewHolder(@NonNull studentViewHolder holder, int position, @NonNull Student model) {

        holder.nameStudent.setText(model.getName());
        holder.degree.setText(model.getDegree());

        Context context1 = holder.itemView.getContext();

        String imageProfile = model.getProfileImage();
        if (imageProfile != null & !imageProfile.isEmpty()) {
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

    /**
     * Method called to create a new ViewHolder.
     * @param parent   The ViewGroup into which the new view will be added.
     * @param viewType The type of the new view.
     * @return A new ViewHolder for the RecyclerView.
     */
    @NonNull
    @Override
    public studentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new studentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_student_school, parent, false));
    }
    /**
     * ViewHolder for the items in the RecyclerView.
     */
    class studentViewHolder extends RecyclerView.ViewHolder {
        TextView nameStudent, degree;
        ImageView photoStudent;

        /**
         * Constructor for the ViewHolder.
         * @param itemView The view of the item.
         */
        public studentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameStudent = itemView.findViewById(R.id.student_name);
            degree = itemView.findViewById(R.id.degree);
            photoStudent = itemView.findViewById(R.id.student_image);

        }
    }
}
