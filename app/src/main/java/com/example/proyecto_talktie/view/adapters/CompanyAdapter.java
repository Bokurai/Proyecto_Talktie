package com.example.proyecto_talktie.view.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_talktie.models.company.OfferObject;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.viewmodel.CompanyViewModel;
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
/**
 * Adapter class for displaying a list of company offers in a RecyclerView.
 * It binds offer data to the ViewHolder and handles item click events.
 */

 public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>{
    private List<OfferObject> offerObjectList;
    private FirebaseFirestore db;
     CompanyViewModel viewModel;
     NavController navController;
    /**
     * Constructor for the adapter.
     * @param offerObjectList The list of company offers to display.
     * @param viewModel The ViewModel for handling offer data.
     * @param navController The NavController for navigating to the offer details fragment.
     */
    public CompanyAdapter(List<OfferObject> offerObjectList,CompanyViewModel viewModel, NavController navController) {
        this.offerObjectList = offerObjectList;
        db = FirebaseFirestore.getInstance();
        this.viewModel=viewModel;
        this.navController=navController;
    }
    /**
     * Method called to create a new ViewHolder for company offers.
     */
    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyAdapter.CompanyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_offer_company, parent, false));
    }
    /**
     * Method is called for each element in the RecyclerView and is responsible for binding the OfferObject data to the provided ViewHolder views.
     * Queries the Firestore database to check if the current user has requested this offer.
     * and updates the user interface accordingly. The number of requesters is also updated in real time by
     * by obtaining the size of the "Applicants" sub-collection.
     */
    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        OfferObject offerObject = offerObjectList.get(position);
        holder.name.setText(offerObject.getName());
        holder.numApplicants.setText(String.valueOf(offerObject.getNumApplicants()));

        CollectionReference applicantsRef = db.collection("Offer").document(offerObject.getOfferId()).collection("Applicants");
        // Check if the current user has applied to this offer
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

        // Get the total number of applicants for the current offer
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.seleccionar(offerObject);
                navController.navigate(R.id.action_goOffersDetailsApplicants);
            }
        });
    }
    /**
     * Method called to get the number of company offers to display.
     * @return The number of company offers.
     */
    @Override
    public int getItemCount() {
        return offerObjectList.size();
    }
    /**
     * Method updating the list of company offers.
     * @param offerObjectList The list of company offers to display.
     */
    public void setOfferObjectList(List<OfferObject> offerObjectList) {
        this.offerObjectList = offerObjectList;
        notifyDataSetChanged();
    }
    /**
     * ViewHolder class that holds references to UI elements for each company offer item in the RecyclerView.
     */
    class CompanyViewHolder extends RecyclerView.ViewHolder{
         TextView name, numApplicants;
         public CompanyViewHolder(@NonNull View itemView) {
             super(itemView);
             name = itemView.findViewById(R.id.offerName);
             numApplicants = itemView.findViewById(R.id.numApplicants);
         }

     }
}
