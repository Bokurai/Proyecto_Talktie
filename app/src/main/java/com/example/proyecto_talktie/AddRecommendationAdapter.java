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

public class AddRecommendationAdapter extends RecyclerView.Adapter<AddRecommendationAdapter.AddRecommendationViewHolder> {
    private List<Teacher> teacherList;
    private Context context;
    NavController navController;
    TeacherViewModel teacherViewModel;

    public AddRecommendationAdapter(List<Teacher> teacherList, Context context, NavController navController, TeacherViewModel teacherViewModel) {
        this.teacherList = teacherList;
        this.context = context;
        this.navController = navController;
        this.teacherViewModel = teacherViewModel;
    }

    @NonNull
    @Override
    public AddRecommendationAdapter.AddRecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddRecommendationAdapter.AddRecommendationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_teacher_add_recommend, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddRecommendationAdapter.AddRecommendationViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);

        holder.nameTeacher.setText(teacher.getName());

        String imageProfile = teacher.getProfileImage();
        if (imageProfile != null && !imageProfile.isEmpty()) {
            Uri uriImage = Uri.parse(imageProfile);
            Glide.with(context)
                    .load(uriImage)
                    .into(holder.imageTeacher);
        } else {
            Glide.with(context)
                    .load(R.drawable.teacher_default)
                    .into(holder.imageTeacher);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherViewModel.select(teacher);
                navController.navigate(R.id.action_goAddRecommendation);
            }
        });

    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
        notifyDataSetChanged();
    }

    class AddRecommendationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTeacher;
        TextView nameTeacher;

        public AddRecommendationViewHolder(@NonNull View itemView) {
            super(itemView);

            imageTeacher = itemView.findViewById(R.id.teacher_image);
            nameTeacher = itemView.findViewById(R.id.teacher_name);

        }
    }

}
