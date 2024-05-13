package com.example.proyecto_talktie;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class NewOfferFragment extends Fragment {

    MainActivity mainActivity;
    NavController navController;
    Spinner jobcategory;
    EditText jobtitleET, contractimeET, descriptionET,tagOneET,tagTwoET,tagThreeET;
    AppCompatButton publishOfferBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_offer, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) requireActivity();
        mainActivity.hideNavBotComp();
        navController = Navigation.findNavController(view);
        jobtitleET = view.findViewById(R.id.jobtitleOfferET);
        contractimeET = view.findViewById(R.id.contractTimeET);
        jobcategory = view.findViewById(R.id.jobcatOfferSP);
        descriptionET = view.findViewById(R.id.jobdescofferET);
        tagOneET = view.findViewById(R.id.tagNewOfferOne);
        tagTwoET = view.findViewById(R.id.tagNewOfferTwo);
        tagThreeET = view.findViewById(R.id.tagNewOfferThree);
        publishOfferBtn = view.findViewById(R.id.btnPublishOffer);

        //Para configurar el spinner
        String[] spinnerOPs = {"IT"," Dev Fullstack","Marketing", "Health"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerOPs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobcategory.setAdapter(adapter);

        publishOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp timestamp = Timestamp.now();
                publishOffer(timestamp);
                Snackbar.make(requireView(), "Oferta para el puesto " + jobtitleET.getText().toString() + " creada.", Snackbar.LENGTH_LONG).show();
                navController.navigate(R.id.action_goCompanyHome);
            }
        });

    }

    public void publishOffer(Timestamp timestamp){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        String docUrl="";
        DocumentReference newOffer = db.collection("Offer").document();
        String offerId = newOffer.getId();

        Map<String, Object> offer = new HashMap<>();
        offer.put("offerId", offerId);
        offer.put("name", jobtitleET.getText().toString());
        offer.put("companyId", uid);
        offer.put("tags", Arrays.asList(tagOneET.getText().toString(), tagTwoET.getText().toString(), tagThreeET.getText().toString()));
        offer.put("documentUrl", docUrl);
        offer.put("contract_time", contractimeET.getText().toString());
        offer.put("job_category", jobcategory.getSelectedItem().toString());
        offer.put("description", descriptionET.getText().toString());
        offer.put("date", timestamp);
        newOffer.set(offer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Se guardó la oferta");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "No se pudo guardar la oferta", e);
                    }
                });
    }
}