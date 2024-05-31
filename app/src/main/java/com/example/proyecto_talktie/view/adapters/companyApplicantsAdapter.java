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
import com.example.proyecto_talktie.viewmodel.ApplicantsViewModel;

import java.util.List;
/**
 * Adapter class for displaying a list of student applicants in a RecyclerView.
 * It binds student data to the ViewHolder and handles item click events.
 */
public class companyApplicantsAdapter extends RecyclerView.Adapter<companyApplicantsAdapter.studentApplicantViewHolder> {
    private List<Student> studentList;
    ApplicantsViewModel applicantsViewModel;
    NavController navController;
    /**
     * Constructor for the adapter.
     * @param studentList The list of student applicants to display.
     * @param applicantsViewModel The ViewModel for handling applicant data.
     * @param navController The NavController for navigating to the student profile fragment.
     */
    public companyApplicantsAdapter(List<Student> studentList, ApplicantsViewModel applicantsViewModel, NavController navController) {
        this.studentList = studentList;
        this.applicantsViewModel = applicantsViewModel;
        this.navController = navController;
    }
    /**
     * Method called to create a new ViewHolder for student applicants.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public studentApplicantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new studentApplicantViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_student_school, parent, false));    }
    /**
     * Method called to bind student data to the ViewHolder.
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull studentApplicantViewHolder holder, int position) {
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
                applicantsViewModel.select(student);
                navController.navigate(R.id.action_goStudentProfileCompany);
            }
        });
    }
    /**
     * Method called to get the number of student applicants to display.
     * @return The number of student applicants.
     */
    @Override
    public int getItemCount() {
        return studentList.size();
    }
    /**
     * Method updating the list of student applicants.
     * @param studentList The list of student applicants to display.
     */
    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
        notifyDataSetChanged();
    }
    /**
     * ViewHolder class that holds references to UI elements for each student applicant item in the RecyclerView.
     */
    public class studentApplicantViewHolder extends RecyclerView.ViewHolder {
        TextView nameStudent, degree;
        ImageView photoStudent;
        /**
         * Constructor for the ViewHolder.
         * @param itemView The view associated with the ViewHolder.
         */
        public studentApplicantViewHolder(@NonNull View itemView) {
            super(itemView);
            nameStudent = itemView.findViewById(R.id.student_name);
            degree = itemView.findViewById(R.id.degree);
            photoStudent = itemView.findViewById(R.id.student_image);
        }
    }
}
