package com.example.proyecto_talktie;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OffersDetailsApplicantsFragment extends Fragment {
    NavController navController;
    TextView offer_name, offer_date,job_category,contract_time,job_description;
    CompanyViewModel companyViewModel;
    ImageView backArrow;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offers_details_applicants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        OfferViewModel offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        CompanyViewModel companyViewModel= new ViewModelProvider(requireActivity()).get(CompanyViewModel.class);

        offer_name = view.findViewById(R.id.txtOfferPosition);
        offer_date = view.findViewById(R.id.txtOfferDate);
        job_category = view.findViewById(R.id.txtWorkField);
        contract_time = view.findViewById(R.id.txtContractTime);
        job_description = view.findViewById(R.id.txtJobDescription);
        backArrow = view.findViewById(R.id.backArrow);
        companyViewModel.seleccionado().observe(getViewLifecycleOwner(), new Observer<OfferObject>() {
            @Override
            public void onChanged(OfferObject offerObject) {
                offer_name.setText(offerObject.getName());
                job_category.setText(offerObject.getJob_category());
                contract_time.setText(offerObject.getContract_time());
                job_description.setText(offerObject.getJob_description());

                SimpleDateFormat format = new SimpleDateFormat("HH:MM  dd/mm/yyyy");
                Date date = offerObject.getDate();String formattedDate = format.format(date);
                offer_date.setText(formattedDate);

            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

    }
}