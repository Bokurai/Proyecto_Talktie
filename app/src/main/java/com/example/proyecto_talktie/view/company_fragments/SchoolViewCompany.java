package com.example.proyecto_talktie.view.company_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.MainActivity;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.school.School;
import com.example.proyecto_talktie.viewmodel.SchoolSearchViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;


public class SchoolViewCompany extends Fragment {

    NavController navController;
    private RecyclerView recyclerView;
    SchoolSearchViewModel schoolSearchViewModel;

    MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_view_company, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        schoolSearchViewModel = new ViewModelProvider(requireActivity()).get(SchoolSearchViewModel.class);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.schoolRecyclerView);

        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBotComp();
        Query baseQuery = FirebaseFirestore.getInstance().collection("School");

        FirestoreRecyclerOptions<School> options = new FirestoreRecyclerOptions.Builder<School>()
                .setQuery(baseQuery, School.class)
                .setLifecycleOwner(this)
                .build();

        final SchoolViewAdapter adapter = new SchoolViewAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    class SchoolViewAdapter extends FirestoreRecyclerAdapter<School, SchoolViewAdapter.SchoolViewHolder>{

       public SchoolViewAdapter(@NonNull FirestoreRecyclerOptions<School> options){
           super(options);
       }

        @Override
        protected void onBindViewHolder(@NonNull SchoolViewAdapter.SchoolViewHolder holder, int position, @NonNull School model) {
            holder.school_name.setText(model.getName());
            holder.school_type.setText(model.getTypeSchool());

            String imageProfile = model.getProfileImage();
            Context context = getView().getContext();
            if (imageProfile != null && !imageProfile.isEmpty()) {
                Uri uriImage = Uri.parse(imageProfile);
                Glide.with(context)
                        .load(uriImage)
                        .into(holder.school_image);
            } else {
                Glide.with(context)
                        .load(R.drawable.school_default_image)
                        .into(holder.school_image);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    schoolSearchViewModel.select(model);
                    navController.navigate(R.id.schoolProfileFromCompany);
                }
            });
        }



        @NonNull
        @Override
        public SchoolViewAdapter.SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SchoolViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_school_view, parent, false));
        }

        class SchoolViewHolder extends  RecyclerView.ViewHolder{
            TextView school_name, school_type;
            CircleImageView school_image;
            public SchoolViewHolder(@NonNull View itemView){
                super(itemView);

                school_name = itemView.findViewById(R.id.school_name);
                school_type = itemView.findViewById(R.id.school_type);
                school_image = itemView.findViewById(R.id.school_image);
            }
        }
    }
}