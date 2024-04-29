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

public class HomeViewModel extends AndroidViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<OfferObject>> offersCompany = new MutableLiveData<>();

    private int cont = 0;
    private int contMax = 0;

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    //Metódo para obtener la lista de empresas seguidas por el usuario y sus publicaciones
    public MutableLiveData<List<OfferObject>> getOffersComapanies(String idUser) {
        db.collection("Student").document(idUser).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        List<OfferObject> allOffers = new ArrayList<>();

                        if (documentSnapshot.exists()) {
                            //Lista de seguidos = id empresas seguidas
                            List<String> companiesFollowed = (List<String>) documentSnapshot.get("followed");

                            if (companiesFollowed != null) {

                                contMax = companiesFollowed.size();
                                Log.d("OFFER", "tamaño Max:" + contMax);

                                //Consultar las publicaciones
                                for (String companyId : companiesFollowed) {
                                    FirebaseFirestore.getInstance().collection("Offer")
                                            .whereEqualTo("companyId", companyId)
                                            .orderBy("date", Query.Direction.DESCENDING)
                                            .limit(5)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (DocumentSnapshot offerSnapshot : queryDocumentSnapshots) {
                                                        //nombre de la oferta
                                                        String name = offerSnapshot.getString("name");
                                                        String companyId = offerSnapshot.getString("companyId");
                                                        Date date = offerSnapshot.getDate("date");

                                                        OfferObject offer = new OfferObject(name, companyId);
                                                        offer.setDate(date);

                                                        allOffers.add(offer);
                                                    }

                                                    cont++;
                                                    Log.d("OFFER", "tamaño cont:" + cont);

                                                    if (cont == contMax ) {
                                                        //ordenar lista de todas las ofertas por fechas
                                                        Collections.sort(allOffers, new Comparator<OfferObject>() {
                                                            @Override
                                                            public int compare(OfferObject o1, OfferObject o2) {
                                                                return o2.getDate().compareTo(o1.getDate());
                                                            }
                                                        });

                                                        offersCompany.setValue(allOffers);
                                                        cont = 0;
                                                    }

                                                }
                                            });
                                }
                            }
                        }


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("OFFER", "Hay un problema", e);
                    }
                });
        return offersCompany;
    }
}
