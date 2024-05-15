package com.example.proyecto_talktie;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

    class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder> {

        NavController navController;
        OfferViewModel offerViewModel;
        private FirebaseFirestore db;
        private List<OfferObject> offerObjectList;
        Context context;

        public OffersAdapter(List<OfferObject> offerObjectList, NavController navController, OfferViewModel offerViewModel, FirebaseFirestore db, Context context) {
            this.offerViewModel = offerViewModel;
            this.navController = navController;
            this.offerObjectList = offerObjectList;
            this.db = db;
            this.context = context;
        }

        public OffersAdapter(List<OfferObject> offerObjectList, FirebaseFirestore db) {
            this.offerObjectList = offerObjectList;
            this.db = db;
        }

        public OffersAdapter(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
        }

        @NonNull
        @Override
        public OffersAdapter.OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OffersAdapter.OffersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_offers,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull OffersAdapter.OffersViewHolder holder, int position) {
            OfferObject offerObject = offerObjectList.get(position);
            int colorLightGreen900 = ContextCompat.getColor(holder.itemView.getContext(), R.color.light_green_900);
            int coloramarillo = ContextCompat.getColor(holder.itemView.getContext(), R.color.amber_700);
            holder.name.setText(offerObject.getName());
            holder.companyName.setText(offerObject.getCompanyName());

            //Imagen compañia en la oferta
            String imageProfile = offerObject.getCompanyImageUrl();;
            if (!imageProfile.equals("null")){
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
                                    holder.stateOffer.setTextColor(Color.BLACK);
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
                if (offerObject.getTags().size() > 0) {
                    holder.tag1.setText(offerObject.getTags().get(0));
                } else {
                    holder.tag1.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
                }

                if (offerObject.getTags().size() > 1) {
                    holder.tag2.setText(offerObject.getTags().get(1));
                } else {
                    holder.tag2.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
                }

                if (offerObject.getTags().size() > 2) {
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

        /**
         *  Method updating the list of offers
         * @param offerObjectList list of offers
         */
        public void setOfferObjectList(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
            notifyDataSetChanged();
            Log.d("OffersAdapter", "Cantidad de ofertas recibidas: " + offerObjectList.size());
        }

        class OffersViewHolder extends RecyclerView.ViewHolder {

            TextView name, companyName, tag1, tag2, tag3,numApplicants,stateOffer;
            ImageView companyImage;


            public OffersViewHolder(@NonNull View itemView) {
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


