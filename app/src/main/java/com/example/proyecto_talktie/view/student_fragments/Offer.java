package com.example.proyecto_talktie.view.student_fragments;

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
import android.widget.LinearLayout;

import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.view.adapters.OffersAdapter;
import com.example.proyecto_talktie.viewmodel.OfferViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

/**
 * Fragment showing the screen with the list of all available offers
 */
public class Offer extends Fragment {
    private OfferViewModel offerViewModel;
    private RecyclerView recyclerView;
    private LinearLayout ti, marketing, health;
    NavController navController;
    private OffersAdapter adapter;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(requireView());

        ti = view.findViewById(R.id.linearBotonTI);
        marketing = view.findViewById(R.id.linearBotonMarketing);
        health = view.findViewById(R.id.linearButtonHealth);

        //Initializes the ViewModel and the adapter
        offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        adapter = new OffersAdapter(new ArrayList<>(), navController, offerViewModel, db, getContext());

        /**
         *  I get the livedata with the offers from the view-model to show it to the user
         */
        offerViewModel.getOffersLiveData().observe(getViewLifecycleOwner(), offerObjects -> {
            adapter.setOfferObjectList(offerObjects);
        });

        recyclerView = view.findViewById(R.id.offerRecyclerView);
        recyclerView.setAdapter(adapter);

        // Navigate to each corresponding category
        ti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goCategoryOffer);
                offerViewModel.setCategory("IT");
            }
        });

        marketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goCategoryOffer);
                offerViewModel.setCategory("Marketing");
            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goCategoryOffer);
                offerViewModel.setCategory("Health");
            }
        });
    }


}