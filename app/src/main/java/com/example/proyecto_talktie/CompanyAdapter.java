package com.example.proyecto_talktie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>{
    private List<OfferObject> offerObjectList;

    public CompanyAdapter(List<OfferObject> offerObjectList) {
        this.offerObjectList = offerObjectList;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyAdapter.CompanyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_offer_company, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        OfferObject offer = offerObjectList.get(position);
        holder.name.setText(offer.getName());
        holder.numApplicants.setText(String.valueOf(offer.getNumApplicants()));
    }

    @Override
    public int getItemCount() {
        return offerObjectList.size();
    }

    public void setOfferObjectList(List<OfferObject> offerObjectList) {
        this.offerObjectList = offerObjectList;
        notifyDataSetChanged();
    }

    class CompanyViewHolder extends RecyclerView.ViewHolder{

         TextView name, numApplicants;

         public CompanyViewHolder(@NonNull View itemView) {
             super(itemView);
             name = itemView.findViewById(R.id.offerName);
             numApplicants = itemView.findViewById(R.id.numApplicants);
         }

     }
}
