package com.example.proyecto_talktie.view.student_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.company.OfferObject;
import com.example.proyecto_talktie.viewmodel.OfferViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * fragment that displays the details of a job offer.
 */
public class OfferDetailsFragment extends Fragment {

    NavController navController;

    TextView offer_name, business_name, offer_date, job_category, contract_time, job_description;

    AppCompatButton apply_job;

    ImageView backArrow, companyImage;

    String offerId;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
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

        companyImage = view.findViewById(R.id.companyLogoJD);
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
                business_name.setText(offerObject.getCompanyName());
                job_category.setText(offerObject.getJob_category());
                contract_time.setText(offerObject.getContract_time());
                job_description.setText(offerObject.getJob_description());

                SimpleDateFormat format = new SimpleDateFormat("HH:MM  dd/mm/yyyy");
                Date date = offerObject.getDate();
                String formattedDate = format.format(date);
                offer_date.setText(formattedDate);

                String imageProfile = offerObject.getCompanyImageUrl();

                Context context = getView().getContext();
                if (imageProfile != null && !imageProfile.isEmpty()) {
                    Uri uriImage = Uri.parse(imageProfile);
                    Glide.with(context)
                            .load(uriImage)
                            .into(companyImage);

                } else {
                    Glide.with(context)
                            .load(R.drawable.build_image_default)
                            .into(companyImage);
                }

                offerId = offerObject.getOfferId();
                changeButtonifApplied(offerId);


                apply_job.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (apply_job.getText().toString().equals("APPLY NOW")) {
                            Log.d("TAG", offerObject.getOfferId());
                            Bundle bundle = new Bundle();
                            bundle.putString("offerId", offerObject.getOfferId());
                            sendRequest2 fragment = new sendRequest2();
                            fragment.setArguments(bundle);
                            navController.navigate(R.id.action_sendRequest, bundle);
                        } else {
                            unApplyFromJob(offerId);
                        }
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

    public void changeButtonifApplied(String offerId) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("Offer").document(offerId).collection("Applicants")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                    apply_job.setText(R.string.unapply);
                            }
                        }
                    });
        }


    }

    public void unApplyFromJob(String offerId){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("Offer").document(offerId).collection("Applicants")
                    .document(userId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            apply_job.setText(R.string.lbl_apply_now);
                        }
                    });
        }
    }
}