package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CompanyHomeFragment extends Fragment {

    AppCompatButton publishButton;

    MainActivity mainActivity;
    NavController navController;
    private OfferViewModel offerViewModel;
    private RecyclerView recyclerView;
    private OffersAdapter adapter;

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
        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBotComp();
        offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);

        /**
         *  I get the livedata with the offers from the view-model to show it to the user
         */
        offerViewModel.getOffersLiveData().observe(getViewLifecycleOwner(), offerObjects -> {
            adapter.setOfferObjectList(offerObjects);
        });

        navController = Navigation.findNavController(requireView());

        adapter = new OffersAdapter(new ArrayList<>(), navController, offerViewModel);

        recyclerView = view.findViewById(R.id.offerRecyclerViewCompany);
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