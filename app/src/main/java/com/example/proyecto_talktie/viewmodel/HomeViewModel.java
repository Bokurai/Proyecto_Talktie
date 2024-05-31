package com.example.proyecto_talktie.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.company.OfferObject;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class HomeViewModel extends AndroidViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<OfferObject>> offersCompany = new MutableLiveData<>();

    private int cont = 0;
    private int contMax = 0;

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Method that obtains the list of all the offers of the companies that the user follows.
     * @param idUser Current user ID
     * @return MutableLiveData with the list of offers from the companies the user follows
     */
    public MutableLiveData<List<OfferObject>> getOffersComapanies(String idUser) {
        db.collection("Student").document(idUser).get()
                .addOnSuccessListener(documentSnapshot -> {
                    //List of ids of companies followed by the user
                    List<String> companiesFollowed = documentSnapshot.exists() ? (List<String>) documentSnapshot.get("followed") : null;

                  if (companiesFollowed != null) {
                        //auxiliary counter
                        contMax = companiesFollowed.size();
                        List<OfferObject> allOffers = new ArrayList<>();
                        companiesFollowed.forEach(companyId -> {

                            getCompanyInfo(companyId, (companyName, companyImageUrl) -> {

                                FirebaseFirestore.getInstance().collection("Offer")
                                        .whereEqualTo("companyId", companyId)
                                        .orderBy("date", Query.Direction.DESCENDING)
                                        .limit(5)
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            queryDocumentSnapshots.forEach(offerSnapshot -> {

                                                OfferObject offer = offerSnapshot.toObject(OfferObject.class);

                                                offer.setCompanyName(companyName);
                                                offer.setCompanyImageUrl(companyImageUrl);

                                                allOffers.add(offer);
                                            });

                                            cont++;
                                            Log.d("OFFER", "tamaño cont:" + cont);

                                            if (cont == contMax) {
                                                //sort list of all offers by date
                                                allOffers.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));

                                                offersCompany.setValue(allOffers);
                                                cont = 0;

                                            }
                                        });
                            });
                        });
                    }
                }).addOnFailureListener(e -> Log.d("OFFER", "Error al cargar la lista de empresas seguidas", e));
        return offersCompany;
    }

    /**
     * Method that searches for the name and photograph of the company to which the offer belongs.
     * @param companyId ID of the company to search.
     * @param callback A BiConsumer with two String parameters.
     */
    public void getCompanyInfo(String companyId, BiConsumer<String, String> callback) {
        db.collection("Company").document(companyId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String companyName = documentSnapshot.getString("name");
                        String companyImageUrl = documentSnapshot.getString("profileImage") != null ? documentSnapshot.getString("profileImage") :"null";
                        callback.accept(companyName, companyImageUrl);
                    } else {
                        callback.accept("Unknown", "null");
                    }
                }).addOnFailureListener(e -> {
                    callback.accept("Unknown", "null");
                });
    }
}
