package com.example.proyecto_talktie;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OffersAdapters {

    class OffersAdapter extends RecyclerView.Adapter<Offer.OffersAdapter.OffersViewHolder> {
        private List<OfferObject> offerObjectList;

        public OffersAdapter(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
        }

        @NonNull
        @Override
        public Offer.OffersAdapter.OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Offer.OffersAdapter.OffersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_offers,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull Offer.OffersAdapter.OffersViewHolder holder, int position) {

            OfferObject offerObject = offerObjectList.get(position);

            holder.name.setText(offerObject.getName());
            holder.companyName.setText(offerObject.getCompanyName());

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

            //Faltan campos
            TextView name, companyName, tag1, tag2, tag3;

            public OffersViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.offerName);
                companyName = itemView.findViewById(R.id.companyName);
                tag1 = itemView.findViewById(R.id.btnTagOne);
                tag2 = itemView.findViewById(R.id.btnTagTwo);
                tag3 = itemView.findViewById(R.id.btnTagThree);
            }
        }
    }
}
