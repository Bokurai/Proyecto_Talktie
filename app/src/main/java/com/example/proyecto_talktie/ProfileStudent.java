package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ProfileStudent extends Fragment {
    //Initialization of the student's view-model
    private StudentViewModel studentViewModel;
    private RecyclerView recyclerView;
    private RecommendAdapter adapter;
    TextView textAbout;
    String studentId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textAbout = view.findViewById(R.id.txtDescription);
        recyclerView = view.findViewById(R.id.recommendRecyclerView);

        //I must change by an id that I extract from the current user
        studentId = "stu1dam2a";

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        /**
         * I get the livedata about from the view-model to show it to the user
         */
        studentViewModel.getAbout(studentId).observe(getViewLifecycleOwner(), aboutMe -> {
            textAbout.setText(aboutMe);
        });

        studentViewModel.getRecommendationLiveData(studentId).observe(getViewLifecycleOwner(), recommendations -> {
            //Create and set the adapter with the new recommendations
            adapter = new RecommendAdapter(recommendations);
            recyclerView.setAdapter(adapter);
        });


    }

    class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendationViewHolder> {
        private List<Recommendation> recommendationList;

        public RecommendAdapter(List<Recommendation> recommendationList) {
            this.recommendationList = recommendationList;
        }

        @NonNull
        @Override
        public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecommendationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommends, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
            Recommendation recommendation = recommendationList.get(position);

            //change image and teacher name by a search through their id.
            holder.nameTeacher.setText(recommendation.getIdTeacher());
            holder.imageTeacher.setImageResource(R.drawable.img_pngtreeavatar);

            holder.textRecommendation.setText(recommendation.getRecommendationText());

        }

        @Override
        public int getItemCount() {
            //Number of recommendations to display
            return recommendationList.size();
        }

        /**
         * Class extracting from the view holder for later use in the adapter
         */
        class RecommendationViewHolder extends RecyclerView.ViewHolder {
            ImageView imageTeacher;
            TextView nameTeacher, textRecommendation;
            RecommendationViewHolder(@NonNull View itemView) {
                super(itemView);

                imageTeacher = itemView.findViewById(R.id.imageTeacherRecommend);
                nameTeacher = itemView.findViewById(R.id.nameTeacherRecommend);
                textRecommendation = itemView.findViewById(R.id.textTeacherRecommend);
            }
        }
    }

}