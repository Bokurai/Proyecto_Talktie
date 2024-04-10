package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class StudentSearchView extends Fragment {
    private RecyclerView recyclerView;
    private androidx.appcompat.widget.SearchView searchViewBar;
    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
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
        searchViewBar = view.findViewById(R.id.searchViewSearch);
        recyclerView = view.findViewById(R.id.searchRecyclerView);

        Query query = db.collection("Company");

        FirestoreRecyclerOptions<Business> options = new FirestoreRecyclerOptions.Builder<Business>()
                .setQuery(query, Business.class)
                        .setLifecycleOwner(this)
                                .build();

        //recyclerView.setAdapter(new CompanyViewAdapter(options));

        final CompanyViewAdapter adapter = new CompanyViewAdapter(options);
        recyclerView.setAdapter(adapter);

        searchViewBar.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Query searchQuery;
                if (newText.isEmpty()) {
                    // Si el texto de búsqueda está vacío, mostrar todas las empresas
                    searchQuery = db.collection("Company");
                } else {
                    // Filtrar empresas por nombre que coincida con el texto de búsqueda
                    searchQuery = db.collection("Company").whereEqualTo("name", newText);
                }

                FirestoreRecyclerOptions<Business> newOptions = new FirestoreRecyclerOptions.Builder<Business>()
                        .setQuery(searchQuery, Business.class)
                        .setLifecycleOwner(StudentSearchView.this)
                        .build();
                adapter.updateOptions(newOptions);
                return true;
            }
        });


    }

    class CompanyViewAdapter extends FirestoreRecyclerAdapter<Business, CompanyViewAdapter.CompanyViewHolder> {

        public CompanyViewAdapter(@NonNull FirestoreRecyclerOptions<Business> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull CompanyViewHolder holder, int position, @NonNull Business model) {
            holder.company_name.setText(model.getName());
            holder.sector_company.setText(model.getSector());

        }

        @NonNull
        @Override
        public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CompanyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_company_view, parent, false));
        }

        class CompanyViewHolder extends RecyclerView.ViewHolder {
            //Faltan campo image
            TextView company_name, sector_company,location_company;

            public CompanyViewHolder(@NonNull View itemView) {
                super(itemView);

                company_name = itemView.findViewById(R.id.company_name);
                sector_company = itemView.findViewById(R.id.sector_company);
                location_company = itemView.findViewById(R.id.location_company);

            }
        }
    }
}