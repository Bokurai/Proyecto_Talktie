package com.example.proyecto_talktie;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class OfferDetailsFragment extends Fragment {

    NavController navController;

    TextView offer_name, business_name,offer_date,job_category,contract_time,job_description;

    AppCompatButton apply_job;

    ImageView backArrow;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        OfferViewModel offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);

        offer_name = view.findViewById(R.id.txtOfferPosition);
        business_name = view.findViewById(R.id.txtBusinessName);
        offer_date = view.findViewById(R.id.txtOfferDate);
        job_category = view.findViewById(R.id.txtWorkField);
        contract_time = view.findViewById(R.id.txtContractTime);
        job_description = view.findViewById(R.id.txtJobDescription);
        apply_job = view.findViewById(R.id.btnapplyJob);
        backArrow = view.findViewById(R.id.backArrow);

        offerViewModel.seleccionado().observe(getViewLifecycleOwner(), new Observer<OfferObject>() {
            @Override
            public void onChanged(OfferObject offerObject) {
               offer_name.setText(offerObject.getName());
               business_name.setText(offerObject.getCompanyId());
               offer_date.setText((CharSequence) offerObject.getDate().toString());
               job_category.setText(offerObject.getJob_category());
               contract_time.setText(offerObject.getContract_time());
               job_description.setText(offerObject.getJob_description());
                apply_job.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("TAG", offerObject.getOfferId());
                        Bundle bundle = new Bundle();
                        bundle.putString("offerId", offerObject.getOfferId());
                        sendRequest2 fragment = new sendRequest2();
                        fragment.setArguments(bundle);
                        navController.navigate(R.id.action_sendRequest, bundle);
                    }
                });
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });
    }
    /*public void addAplicantToOffer(String offerId) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String applicantId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Offer")
                .whereEqualTo("offerId", offerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        DocumentReference offerRef = documentSnapshot.getReference();

                        List<String> applicantsIds = documentSnapshot.contains("applicantsId") ?
                                (List<String>) documentSnapshot.get("applicantsId") : new ArrayList<>();

                        if (!applicantsIds.contains(applicantId)) {
                            applicantsIds.add(applicantId);
                        }
                        offerRef.update("applicantsId", applicantsIds);
                    }
                });
    }*/

}