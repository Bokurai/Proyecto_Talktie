package com.example.proyecto_talktie;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

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
     * @return MutableLiveData with the list of offerts from the companies the user follows
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

                            getCompanyName(companyId, companyName -> {

                                FirebaseFirestore.getInstance().collection("Offer")
                                        .whereEqualTo("companyId", companyId)
                                        .orderBy("date", Query.Direction.DESCENDING)
                                        .limit(5)
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            queryDocumentSnapshots.forEach(offerSnapshot -> {

                                                String name = offerSnapshot.getString("name");
                                                String idCompany = offerSnapshot.getString("companyId");
                                                Date date = offerSnapshot.getDate("date");

                                                OfferObject offer = new OfferObject(name, idCompany);
                                                offer.setDate(date);
                                                offer.setCompanyName(companyName);

                                                allOffers.add(offer);
                                            });

                                            cont++;
                                            Log.d("OFFER", "tamaño cont:" + cont);

                                            if (cont == contMax) {
                                                //sort list of all offers by date
                                                Collections.sort(allOffers, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));

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

    public void getCompanyName(String companyId, Consumer<String> callback) {
        db.collection("Company").document(companyId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String companyName = documentSnapshot.getString("name");
                        callback.accept(companyName);
                    } else {
                        callback.accept("Unknown");
                    }
                }).addOnFailureListener(e -> {
                    callback.accept("Unknown");
                });
    }
}
