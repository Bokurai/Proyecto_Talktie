package com.example.proyecto_talktie;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class SchoolViewCompany extends Fragment {

    NavController navController;
    private RecyclerView recyclerView;
    SchoolSearchViewModel schoolSearchViewModel;

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
        Query baseQuery = FirebaseFirestore.getInstance().collection("School");

        FirestoreRecyclerOptions<School> options = new FirestoreRecyclerOptions.Builder<School>()
                .setQuery(baseQuery, School.class)
                .setLifecycleOwner(this)
                .build();

        final SchoolViewAdapter adapter = new SchoolViewAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    class SchoolViewAdapter extends FirestoreRecyclerAdapter<School, StudentSearchView.SchoolViewAdapter.SchoolView> {

        public SchoolViewAdapter(@NonNull FirestoreRecyclerOptions<School> options) {
            super(options);

        }

        @Override
        protected void onBindViewHolder(@NonNull schoolSearchView.SchoolViewAdapter.SchoolViewHolder holder, int position, @NonNull Business model) {

            holder.company_name.setText(model.getName());
            holder.sector_company.setText(model.getSector());
            holder.location_company.setText(model.getAddress());


            String imageProfile = model.getProfileImage();
            Context context = getView().getContext();
            if (imageProfile != null && !imageProfile.isEmpty()) {
                Uri uriImage = Uri.parse(imageProfile);
                Glide.with(context)
                        .load(uriImage)
                        .into(holder.companyImage);
            } else {
                Glide.with(context)
                        .load(R.drawable.build_image_default)
                        .into(holder.companyImage);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchViewModel.select(model);
                    navController.navigate(R.id.action_goCompanyProfile);
                }
            });

        }


        @NonNull
        @Override
        public StudentSearchView.CompanyViewAdapter.CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new StudentSearchView.CompanyViewAdapter.CompanyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_company_view,parent,false));
        }

        class CompanyViewHolder extends RecyclerView.ViewHolder {
            TextView company_name, sector_company,location_company;
            ImageView companyImage;

            public CompanyViewHolder(@NonNull View itemView) {
                super(itemView);

                company_name = itemView.findViewById(R.id.company_name);
                sector_company = itemView.findViewById(R.id.sector_company);
                location_company = itemView.findViewById(R.id.location_company);
                companyImage = itemView.findViewById(R.id.company_image);

            }
        }
    }
}