package com.example.proyecto_talktie;

import static androidx.fragment.app.FragmentManager.TAG;

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

import com.google.firebase.firestore.FirebaseFirestore;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Obtener la ID de la compañía desde Firestore
        db.collection("Company").document("companyId")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String companyId = documentSnapshot.getString("companyId");

                        // Log para verificar que la ID de la compañía se obtiene correctamente
                        Log.d("Company ID", companyId);

                        // Obtener las ofertas de la compañía correspondiente
                        offerViewModel.getOffersCompany(companyId).observe(getViewLifecycleOwner(), offerObjects -> {
                            adapter.setOfferObjectList(offerObjects);
                            // Log para verificar el número de ofertas obtenidas
                            Log.d("Número de ofertas", String.valueOf(offerObjects.size()));
                        });
                    } else {
                        // Log para indicar que no se encontró el documento de la compañía
                        Log.d("No such document", "El documento de la compañía no existe");
                    }
                })
                .addOnFailureListener(e -> {
                    // Log para indicar un error al obtener el documento
                    Log.e("Error getting document", "Error al obtener el documento de la compañía", e);
                });




        navController = Navigation.findNavController(requireView());

        adapter = new OffersAdapter(new ArrayList<>(), navController, offerViewModel, db);

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