package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.checkerframework.checker.units.qual.A;

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

        //Adapatador inicial
      FirestoreRecyclerOptions<Business> options = new FirestoreRecyclerOptions.Builder<Business>()
                .setQuery(baseQuery, Business.class)
                        .setLifecycleOwner(this)
                                .build();


        //Adapatador opciones iniciales
        final CompanyViewAdapter adapter = new CompanyViewAdapter(options);
        recyclerView.setAdapter(adapter);

        //Listener de la barra de búsqueda
        searchViewBar.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Query searchQuery = baseQuery;

                if (!s.isEmpty()) {
                  // searchQuery = baseQuery.whereEqualTo("name", s);
                    searchQuery = baseQuery.orderBy("name").startAt(s).endAt(s+"\uf8ff");
                }

                FirestoreRecyclerOptions<Business> newOptions = new FirestoreRecyclerOptions.Builder<Business>()
                        .setQuery(searchQuery, Business.class)
                        .setLifecycleOwner(StudentSearchView.this)
                        .build();

                adapter.updateOptions(newOptions);
                adapter.notifyDataSetChanged();

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
            holder.location_company.setText(model.getAddress());

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