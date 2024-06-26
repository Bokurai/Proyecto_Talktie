package com.example.proyecto_talktie.view.school_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_talktie.MainActivity;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.student.Student;
import com.example.proyecto_talktie.view.adapters.studentSchoolAdapter;
import com.example.proyecto_talktie.viewmodel.schoolHomeViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Fragment representing the Home screen (to recommend students) of school
 */
public class schoolHome extends Fragment {

   MainActivity mainActivity;
   NavController navController;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
   private RecyclerView recyclerView;
   private SearchView searchViewBar;
   private schoolHomeViewModel homeViewModel;
   private String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(schoolHomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(requireActivity()).get(schoolHomeViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_school_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBotSchool();

        navController = Navigation.findNavController(view);

        //Initialize the ViewModel
        searchViewBar = view.findViewById(R.id.searchViewSearchSchool);
        recyclerView = view.findViewById(R.id.searchRecyclerViewSchool);

        /**
         * Observe the name of the school
         */
        homeViewModel.getNameSchool().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                name = s;

                Log.d("TAG", "El nombre del instituto es" + name);

                Query baseQuery = FirebaseFirestore.getInstance().collection("Student")
                        .whereEqualTo("center", name);

                //Initial adapter
                FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>()
                        .setQuery(baseQuery, Student.class)
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .build();

                //Adapter initial options
                studentSchoolAdapter adapter = new studentSchoolAdapter(options, navController, homeViewModel);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

                //Search bar listener
                searchViewBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Query searchQuery = baseQuery;

                        if (!s.isEmpty()) {
                            searchQuery = baseQuery.orderBy("name").startAt(s).endAt(s+"\uf8ff");
                        }

                        FirestoreRecyclerOptions<Student> newOptions = new FirestoreRecyclerOptions.Builder<Student>()
                                .setQuery(searchQuery, Student.class)
                                .setLifecycleOwner(getViewLifecycleOwner())
                                .build();

                        adapter.updateOptions(newOptions);
                        adapter.notifyDataSetChanged();

                        return true;
                    }
                });
            }
        });
    }

}