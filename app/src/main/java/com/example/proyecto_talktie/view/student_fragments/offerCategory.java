package com.example.proyecto_talktie.view.student_fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class offerCategory extends Fragment {
    private TextView nameCategory;
    private OfferViewModel offerViewModel;
    private RecyclerView recyclerView;
    private NavController navController;
    private OffersCategory adapter;
    private String category = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer_category, container, false);

        adapter = new OffersCategory(new ArrayList<>());

        recyclerView = view.findViewById(R.id.categoryRecyclerView);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        nameCategory = view.findViewById(R.id.nameCategories);

        offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);

        category = offerViewModel.getCategory();

        nameCategory.setText(category);


        offerViewModel.getOfferCategory(category).observe(getViewLifecycleOwner(), offerObjects -> {
            adapter.setOfferObjectList(offerObjects);
        });

    }

    class OffersCategory extends RecyclerView.Adapter<OffersCategory.OfferViewHolder> {
        private List<OfferObject> offerObjectList;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

        public OffersCategory(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
        }

        @NonNull
        @Override
        public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OfferViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_offers,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
            OfferObject offerObject = offerObjectList.get(position);

            int colorLightGreen900 = ContextCompat.getColor(holder.itemView.getContext(), R.color.light_green_900);
            int coloramarillo = ContextCompat.getColor(holder.itemView.getContext(), R.color.amber_700);

            holder.name.setText(offerObject.getName());
            holder.companyName.setText(offerObject.getCompanyName());

            String imageProfile = offerObject.getCompanyImageUrl();
            Context context = getView().getContext();
            if (imageProfile != null && !imageProfile.isEmpty()){
                Uri uriImage = Uri.parse(imageProfile);
                Glide.with(context)
                        .load(uriImage)
                        .into(holder.companyImage);

            } else {
                Glide.with(context)
                        .load(R.drawable.build_image_default)
                        .into(holder.companyImage);
            }

            CollectionReference applicantsRef = db.collection("Offer").document(offerObject.getOfferId()).collection("Applicants");
            applicantsRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    holder.stateOffer.setTextColor(Color.WHITE);
                                    holder.stateOffer.setBackgroundColor(colorLightGreen900);
                                    holder.stateOffer.setText("Applied");
                                } else {
                                    holder.stateOffer.setTextColor(Color.WHITE);
                                    holder.stateOffer.setBackgroundColor(coloramarillo);
                                    holder.stateOffer.setText("Apply");
                                }
                            } else {
                                Log.e("OffersAdapter", "Error al verificar si el usuario está en la subcolección de aplicantes: ", task.getException());
                            }
                        }
                    });

            applicantsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    int numApplicants = queryDocumentSnapshots.size();
                    holder.numApplicants.setText(String.valueOf(numApplicants));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("OffersAdapter", "Error al obtener la cantidad de aplicantes: " + e.getMessage());
                }
            });


            if(offerObject.getTags() != null) {
                if (!offerObject.getTags().get(0).isEmpty()) {
                    holder.tag1.setText(offerObject.getTags().get(0));
                } else {
                    holder.tag1.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
                }

                if (!offerObject.getTags().get(1).isEmpty()) {
                    holder.tag2.setText(offerObject.getTags().get(1));
                } else {
                    holder.tag2.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
                }

                if (!offerObject.getTags().get(2).isEmpty()) {
                    holder.tag3.setText(offerObject.getTags().get(2));
                } else {
                    holder.tag3.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
                }
            } else {
                holder.tag1.setVisibility(View.GONE);
                holder.tag2.setVisibility(View.GONE);
                holder.tag3.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    offerViewModel.seleccionar(offerObject);
                    Log.d("TAG", "Oferta seleccionada " + offerObject.getName());
                    navController.navigate(R.id.action_goOfferDetailsFragment);
                }
            });


        }

        @Override
        public int getItemCount() {
            return offerObjectList.size();
        }

        public void setOfferObjectList(List<OfferObject> offerObjectList) {
            this.offerObjectList.clear();
            this.offerObjectList.addAll(offerObjectList);
            notifyDataSetChanged();
        }



        class OfferViewHolder extends RecyclerView.ViewHolder {
            TextView name, companyName, tag1, tag2, tag3, numApplicants,stateOffer;
            ImageView companyImage;

            public OfferViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.offerName);
                companyName = itemView.findViewById(R.id.companyName);
                tag1 = itemView.findViewById(R.id.btnTagOne);
                tag2 = itemView.findViewById(R.id.btnTagTwo);
                tag3 = itemView.findViewById(R.id.btnTagThree);
                numApplicants = itemView.findViewById(R.id.numApplicants);
                stateOffer=itemView.findViewById(R.id.stateOffer);
                companyImage = itemView.findViewById(R.id.imageCompany);
            }
        }
    }
}