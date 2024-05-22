package com.example.proyecto_talktie.view.school_fragments;

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
import com.example.proyecto_talktie.models.company.Business;
import com.example.proyecto_talktie.viewmodel.CompanySearchViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;


public class CompanyViewSchool extends Fragment {

    NavController navController;
    private RecyclerView recyclerView;
    CompanySearchViewModel companySearchViewModel;
    MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_view_school, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        companySearchViewModel = new ViewModelProvider(requireActivity()).get(CompanySearchViewModel.class);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.schoolcompRecyclerView);

        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBotSchool();
        Query baseQuery = FirebaseFirestore.getInstance().collection("Company");

        FirestoreRecyclerOptions<Business> options = new FirestoreRecyclerOptions.Builder<Business>()
                .setQuery(baseQuery, Business.class)
                .setLifecycleOwner(this)
                .build();

        final CompanyViewAdapterProfile adapterProfile = new CompanyViewAdapterProfile(options);
        recyclerView.setAdapter(adapterProfile);
    }

    class CompanyViewAdapterProfile extends FirestoreRecyclerAdapter<Business, CompanyViewAdapterProfile.CompanyViewHolder> {

        public CompanyViewAdapterProfile(@NonNull FirestoreRecyclerOptions<Business> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull CompanyViewAdapterProfile.CompanyViewHolder holder, int position, @NonNull Business model) {
            holder.company_name.setText(model.getName());
            holder.location_company.setText(model.getAddress());
            holder.sector_company.setText(model.getSector());

            String imageProfile = model.getProfileImage();
            Context context = getView().getContext();
            if (imageProfile != null && !imageProfile.isEmpty()) {
                Uri uriImage = Uri.parse(imageProfile);
                Glide.with(context)
                        .load(uriImage)
                        .into(holder.company_image);
            } else {
                Glide.with(context)
                        .load(R.drawable.build_image_default)
                        .into(holder.company_image);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    companySearchViewModel.select(model);
                    navController.navigate(R.id.companyProfileFromSchool);
                }
            });
        }

        @NonNull
        @Override
        public CompanyViewAdapterProfile.CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CompanyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_company_view, parent,false));
        }

        class CompanyViewHolder extends RecyclerView.ViewHolder{

            TextView company_name, location_company, sector_company;

            CircleImageView company_image;

            public  CompanyViewHolder(@NonNull View itemView){
                super(itemView);
                company_name = itemView.findViewById(R.id.company_name);
                location_company = itemView.findViewById(R.id.location_company);
                sector_company = itemView.findViewById(R.id.sector_company);
                company_image = itemView.findViewById(R.id.company_image);
            }
        }
    }
}