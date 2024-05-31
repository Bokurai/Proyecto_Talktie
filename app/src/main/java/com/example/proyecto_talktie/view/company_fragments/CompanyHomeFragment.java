package com.example.proyecto_talktie.view.company_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
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
import com.example.proyecto_talktie.view.adapters.CompanyAdapter;
import com.example.proyecto_talktie.viewmodel.CompanyViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
/**
 * Fragment that represents the home screen for the company user, displaying their published job offers.
 */
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
        return inflater.inflate(R.layout.fragment_company_home, container, false);
    }
    /**
     * Sets up the views and data for the Company Home screen.
     * It initializes the necessary views, observes changes in the company's offers, and updates the UI accordingly.
     * Additionally, it handles the navigation to the "New Offer" screen when the publishButton is clicked.
     */
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