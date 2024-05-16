package com.example.proyecto_talktie;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;

public class AddRecommendationAdapter extends RecyclerView.Adapter<AddRecommendationAdapter.AddRecommendationViewHolder> {
    private List<Teacher> teacherList;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    NavController navController;
    TeacherViewModel teacherViewModel;
    String studentId;

    public AddRecommendationAdapter(List<Teacher> teacherList, Context context, NavController navController, TeacherViewModel teacherViewModel, String studentId) {
        this.teacherList = teacherList;
        this.context = context;
        this.navController = navController;
        this.teacherViewModel = teacherViewModel;
        this.studentId = studentId;
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

                db.collection("Teacher").document(teacher.getTeacherId()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                List<String> recommendedStudents = (List<String>) documentSnapshot.get("recommendedStudents");
                                if (recommendedStudents != null && recommendedStudents.contains(studentId)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("You have already written a recommendation to this student.\n\nWould you like to edit it?")
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    teacherViewModel.select(teacher);
                                                    navController.navigate(R.id.action_goEditRecommendation);
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                } else {
                                    teacherViewModel.select(teacher);
                                    navController.navigate(R.id.action_goAddRecommendation);
                                }
                            }
                        });
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
