package com.example.proyecto_talktie;

import static androidx.fragment.app.FragmentManager.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CompanyHomeFragment extends Fragment {

    AppCompatButton publishButton;

    MainActivity mainActivity;
    NavController navController;
    private CompanyViewModel companyViewModel;
    private RecyclerView recyclerView;
    private CompanyAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userId= mAuth.getUid();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG", "user id es:"+ userId);
        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBotComp();
        companyViewModel = new ViewModelProvider(requireActivity()).get(CompanyViewModel.class);
        navController = Navigation.findNavController(requireView());

        adapter = new CompanyAdapter(new ArrayList<>(), companyViewModel, navController);
        recyclerView = view.findViewById(R.id.offerRecyclerViewCompany);

        companyViewModel.getOffersCompany(userId).observe(getViewLifecycleOwner(), offerObjects -> {
            adapter.setOfferObjectList(offerObjects);
        });
        recyclerView.setAdapter(adapter);
        publishButton = view.findViewById(R.id.btnPublish);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goNewOffer);
            }
        });
    }
}