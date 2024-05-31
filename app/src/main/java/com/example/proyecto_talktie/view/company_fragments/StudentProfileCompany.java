package com.example.proyecto_talktie.view.company_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.company.ApplicantData;
import com.example.proyecto_talktie.models.school.Recommendation;
import com.example.proyecto_talktie.models.student.Student;
import com.example.proyecto_talktie.viewmodel.ApplicantsViewModel;
import com.example.proyecto_talktie.viewmodel.StudentViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Fragment responsible for displaying the profile of a student from the company's perspective.
 * It retrieves student data from Firestore and displays it, including name, contact information,
 * about section, cover letter, experience, resume, and recommendations.
 */
public class StudentProfileCompany extends Fragment {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    NavController navController;
    ImageView backArrow, imageStudent;
    TextView nameStudent, emailStudent, phoneStudent, aboutStudent, coverStudent, experience, resume, txtRecommendations, separator;
    LinearLayout linearCover, linearExperience, linearResume;
    CardView cardAbout;
    RecyclerView recyclerRecommendation;
    ApplicantsViewModel applicantsViewModel;
    private RecommendAdapter adapter;
    private StudentViewModel studentViewModel;
    /**
     * Method called to create the view associated with the fragment.
     * Initializes the RecyclerView adapter for recommendations.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile_company, container, false);
        adapter = new RecommendAdapter(new ArrayList<>());
        recyclerRecommendation = view.findViewById(R.id.listSRecyclerView);
        recyclerRecommendation.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }
    /**
     * Method called when the view associated with the fragment has been created.
     * Handles UI elements, retrieves student data from Firestore, and updates the UI accordingly.
     * @param view The inflated view of the fragment.
     * @param savedInstanceState The saved instance state of the fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        backArrow = view.findViewById(R.id.img_returnS);
        imageStudent = view.findViewById(R.id.profileImgStudentS);
        nameStudent = view.findViewById(R.id.txtStudentNameS);
        emailStudent = view.findViewById(R.id.emailS);
        phoneStudent = view.findViewById(R.id.phoneS);
        separator = view.findViewById(R.id.separator);

        aboutStudent = view.findViewById(R.id.txtDescriptionS);
        cardAbout = view.findViewById(R.id.cardAbout);
        coverStudent = view.findViewById(R.id.txtCoverS);
        linearCover = view.findViewById(R.id.linearCover);
        experience = view.findViewById(R.id.txtExperienceS);
        linearExperience = view.findViewById(R.id.linearExperience);
        resume = view.findViewById(R.id.txtResumeS);
        linearResume = view.findViewById(R.id.linearResume);
        txtRecommendations = view.findViewById(R.id.textRecommend);

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        applicantsViewModel = new ViewModelProvider(requireActivity()).get(ApplicantsViewModel.class);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        applicantsViewModel.selected().observe(getViewLifecycleOwner(), new Observer<Student>() {
            @Override
            public void onChanged(Student student) {

                String studentId = student.getStudentId();
                nameStudent.setText(student.getName());
               String offerId = applicantsViewModel.getOfferId();

               applicantsViewModel.dataApplicant(offerId, studentId).observe(getViewLifecycleOwner(), new Observer<ApplicantData>() {
                   @Override
                   public void onChanged(ApplicantData applicantData) {

                       if (applicantData.getLetter() != null && !applicantData.getLetter().isEmpty()) {
                           linearCover.setVisibility(View.VISIBLE);
                           coverStudent.setText(applicantData.getLetter());
                       } else {
                           linearCover.setVisibility(View.GONE);
                       }

                       if (applicantData.getExperience() != null && !applicantData.getExperience().isEmpty()) {
                           linearExperience.setVisibility(View.VISIBLE);
                           experience.setText(applicantData.getExperience());
                       } else {
                           linearExperience.setVisibility(View.GONE);
                       }

                       if (applicantData.getDocumentUrl() != null && !applicantData.getDocumentUrl().isEmpty()) {
                           linearResume.setVisibility(View.VISIBLE);
                           resume.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   String urlResume = applicantData.getDocumentUrl();

                                   StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(urlResume);

                                   try {
                                       File localFile = File.createTempFile(student.getName(), ".pdf");
                                       storageReference.getFile(localFile)
                                               .addOnSuccessListener(taskSnapshot -> {
                                                   Toast.makeText(getContext(), localFile.getName()+" successfully downloaded", Toast.LENGTH_SHORT).show();
                                               })
                                               .addOnFailureListener(new OnFailureListener() {
                                                   @Override
                                                   public void onFailure(@NonNull Exception e) {
                                                       Toast.makeText(getContext(), "Error downloading file", Toast.LENGTH_SHORT).show();
                                                   }
                                               });
                                   } catch (IOException e) {
                                       throw new RuntimeException(e);
                                   }

                               }
                           });

                       } else {
                           linearResume.setVisibility(View.GONE);
                       }
                   }
               });

                studentViewModel.getRecommendationLiveData(studentId).observe(getViewLifecycleOwner(), recommendations -> {
                    Log.d("Recomendaciones", "Acceder livedata recomendaciones " + studentId);
                    if (recommendations != null) {
                        txtRecommendations.setVisibility(View.VISIBLE);
                    }
                    //Update data on exist adapter
                    adapter.setRecommendationList(recommendations);
                });

                if (student.getAbout() != null && !student.getAbout().isEmpty()) {
                    cardAbout.setVisibility(View.VISIBLE);
                    aboutStudent.setText(student.getAbout());
                }

                if (student.getEmail() == null) {
                    emailStudent.setVisibility(View.GONE);

                } else {
                    emailStudent.setText(student.getEmail());
                }

                if (student.getPhone() == null) {
                    phoneStudent.setVisibility(View.GONE);
                } else {
                    phoneStudent.setText(student.getPhone());
                }

                if (TextUtils.isEmpty(student.getEmail()) || TextUtils.isEmpty(student.getPhone())) {
                    separator.setVisibility(View.GONE);
                }

                Context context = getView().getContext();
                String imageProfile = student.getProfileImage();
                if (imageProfile != null && !imageProfile.isEmpty()) {
                    Uri uriImage = Uri.parse(imageProfile);
                    Glide.with(context)
                            .load(uriImage)
                            .into(imageStudent);
                } else {
                    Glide.with(context)
                            .load(R.drawable.profile_image_defaut)
                            .into(imageStudent);
                }
            }
        });

    }
    /**
     * Adapter class for the RecyclerView that displays recommendations.
     * It binds recommendation data to the ViewHolder and handles UI updates.
     */
    class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendationViewHolder> {
        private List<Recommendation> recommendationList;

        public RecommendAdapter(List<Recommendation> recommendationList) {
            this.recommendationList = recommendationList;
        }
        /**
         * Method called to create a new ViewHolder for recommendations.
         */
        @NonNull
        @Override
        public RecommendAdapter.RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecommendAdapter.RecommendationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommendations, parent, false));
        }
        /**
         * Method called to bind recommendation data to the ViewHolder.
         * @param holder The ViewHolder to bind the data to.
         * @param position The position of the item in the RecyclerView.
         */
        @Override
        public void onBindViewHolder(@NonNull RecommendAdapter.RecommendationViewHolder holder, int position) {
            Recommendation recommendation = recommendationList.get(position);
            //change image and teacher name by a search through their id.
            holder.nameTeacher.setText(recommendation.getTeacher().getName());
            holder.textRecommendation.setText(recommendation.getRecommendationText());

            String profileImage = recommendation.getTeacher().getProfileImage();
            //  String imageProfileUrl = recommendation.getProfileImage();
            Context context = getView().getContext();
            if (profileImage != null && !profileImage.isEmpty()) {
                Uri uriImagep = Uri.parse(profileImage);

                Glide.with(context)
                        .load(uriImagep)
                        .into(holder.imageTeacher);
            } else {
                Glide.with(context)
                        .load(R.drawable.teacher_default)
                        .into(holder.imageTeacher);
            }

            boolean emailIsEmpty = isNullOrEmpty(recommendation.getTeacher().getEmail());
            boolean positionIsEmpty = isNullOrEmpty(recommendation.getTeacher().getPosition());

            if (emailIsEmpty || positionIsEmpty) {
                holder.separator.setVisibility(View.GONE);
            } else {
                holder.separator.setVisibility(View.VISIBLE);
            }

            if (!emailIsEmpty) {
                holder.email.setText(recommendation.getTeacher().getEmail());
                holder.email.setVisibility(View.VISIBLE);
            } else {
                holder.email.setVisibility(View.GONE);
            }

            if (!positionIsEmpty) {
                holder.position.setText(recommendation.getTeacher().getPosition());
                holder.position.setVisibility(View.VISIBLE);
            } else {
                holder.position.setVisibility(View.GONE);
            }
        }
        private boolean isNullOrEmpty(String string) {
            return string == null || string.isEmpty();
        }

        /**
         * Method called to get the number of recommendations to display.
         * @return The number of recommendations.
         */
        @Override
        public int getItemCount() {
            //Number of recommendations to display
            return recommendationList.size();
        }

        /**
         * Method updating the list of recommendations
         * @param recommendations list of recommendations
         */
        public void setRecommendationList(List<Recommendation> recommendations) {
            this.recommendationList = recommendations;
            notifyDataSetChanged();
        }

        /**
         * ViewHolder class that holds references to UI elements for each recommendation item in the RecyclerView.
         */
        class RecommendationViewHolder extends RecyclerView.ViewHolder {
            ImageView imageTeacher;
            TextView nameTeacher, textRecommendation, position, email, separator;
            RecommendationViewHolder(@NonNull View itemView) {
                super(itemView);

                imageTeacher = itemView.findViewById(R.id.imageTeacherRecommend);
                nameTeacher = itemView.findViewById(R.id.nameTeacherRecommend);
                textRecommendation = itemView.findViewById(R.id.textTeacherRecommend);
                position = itemView.findViewById(R.id.position_teacher);
                email = itemView.findViewById(R.id.email_teacher);
                separator = itemView.findViewById(R.id.separatorT);
            }
        }
    }

}