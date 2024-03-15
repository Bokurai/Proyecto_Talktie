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

import java.util.Map;

public class ProfileStudent extends Fragment {
    private StudentViewModel studentViewModel;
    private RecyclerView recyclerView;

    String studentId;
    private RecommendAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recommendRecyclerView);

        studentViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // El usuario está autenticado, puedes acceder a sus datos
                studentId = user.getUid();
                // etc.
            } else {
                // El usuario no está autenticado
            }
        });


        studentViewModel = new ViewModelProvider(requireActivity()).get(StudentViewModel.class);
        studentViewModel.getStudentRecommendationLD(studentId).observe(getViewLifecycleOwner(), recommendations -> {
            // Actualizar el adaptador con las nuevas recomendaciones
            adapter.notifyDataSetChanged();
        });

    }

    class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendationViewHolder> {
        private Map<String, String> recommendations;

        public RecommendAdapter(Map<String, String> recommendations) {
            this.recommendations = recommendations;
        }

        @NonNull
        @Override
        public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecommendationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommends, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
            //Logica para mostrar las recomendaciones


        }

        @Override
        public int getItemCount() {
            return recommendations.size();
        }

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