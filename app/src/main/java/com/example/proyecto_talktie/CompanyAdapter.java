package com.example.proyecto_talktie;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

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

 public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>{
    private List<OfferObject> offerObjectList;
    private FirebaseFirestore db;

    public CompanyAdapter(List<OfferObject> offerObjectList) {
        this.offerObjectList = offerObjectList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyAdapter.CompanyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_offer_company, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        OfferObject offerObject = offerObjectList.get(position);
        holder.name.setText(offerObject.getName());
        holder.numApplicants.setText(String.valueOf(offerObject.getNumApplicants()));


        CollectionReference applicantsRef = db.collection("Offer").document(offerObject.getOfferId()).collection("Applicants");
        applicantsRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
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
