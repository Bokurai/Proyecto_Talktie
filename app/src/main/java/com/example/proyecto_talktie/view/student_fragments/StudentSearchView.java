package com.example.proyecto_talktie.view.student_fragments;

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
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.company.Business;
import com.example.proyecto_talktie.viewmodel.StudentSearchViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class StudentSearchView extends Fragment {
    NavController navController;
    private TextView txtCancel;
    private RecyclerView recyclerView;
    private androidx.appcompat.widget.SearchView searchViewBar;
    private StudentSearchViewModel searchViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_search_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize the viewModel
        searchViewModel = new ViewModelProvider(requireActivity()).get(StudentSearchViewModel.class);

        navController = Navigation.findNavController(view);
        searchViewBar = view.findViewById(R.id.searchViewSearch);
        recyclerView = view.findViewById(R.id.searchRecyclerView);
        txtCancel = view.findViewById(R.id.txtCancel);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goHome);
            }
        });

        Query baseQuery = FirebaseFirestore.getInstance().collection("Company");

        //Initial adapter
      FirestoreRecyclerOptions<Business> options = new FirestoreRecyclerOptions.Builder<Business>()
                .setQuery(baseQuery, Business.class)
                        .setLifecycleOwner(this)
                                .build();


        //Adapter initial options
        final CompanyViewAdapter adapter = new CompanyViewAdapter(options);
        recyclerView.setAdapter(adapter);

        //Search bar listener
        searchViewBar.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Query searchQuery = baseQuery;

                if (!s.isEmpty()) {
                    //Perform a search by the entered term compared to the Company Name field
                    searchQuery = baseQuery.orderBy("name").startAt(s).endAt(s+"\uf8ff");
                }

                FirestoreRecyclerOptions<Business> newOptions = new FirestoreRecyclerOptions.Builder<Business>()
                        .setQuery(searchQuery, Business.class)
                        .setLifecycleOwner(StudentSearchView.this)
                        .build();

                //Adapter updated with search results
                adapter.updateOptions(newOptions);
                adapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    /**
     * Class representing the adapter of the companies
     */
    class CompanyViewAdapter extends FirestoreRecyclerAdapter<Business, CompanyViewAdapter.CompanyViewHolder> {

        public CompanyViewAdapter(@NonNull FirestoreRecyclerOptions<Business> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull CompanyViewHolder holder, int position, @NonNull Business model) {

            //Updates items with company data
            holder.company_name.setText(model.getName());
            holder.sector_company.setText(model.getSector());
            holder.location_company.setText(model.getAddress());


            //If the company image is null, the default image is used
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

            //Navigate to the company profile and store the company object
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
        public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CompanyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_company_view,parent,false));
        }

        /**
         * Class that represents the elements of the ViewHolder.
         */
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