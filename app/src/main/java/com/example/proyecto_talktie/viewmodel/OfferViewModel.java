package com.example.proyecto_talktie.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.proyecto_talktie.models.company.OfferObject;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Collections;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.List;


public class OfferViewModel extends AndroidViewModel {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private MutableLiveData<List<OfferObject>> offersLiveData = new MutableLiveData<>();
    private MutableLiveData<List<OfferObject>> offersCompany = new MutableLiveData<>();
    private MutableLiveData<OfferObject> offerSingle = new MutableLiveData<>();
    private String category = "";

    public OfferViewModel(@NonNull Application application) {
        super(application);
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Method that goes through each offer in the list, sorts them by date in descending order and limits to 50 results.
     * @return MutableLiveData with offers
     */
    public MutableLiveData<List<OfferObject>> getOffersLiveData() {
        db.collection("Offer")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OfferObject> offers = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        OfferObject offer = document.toObject(OfferObject.class);

                        getCompanyInfo(offer.getCompanyId(), (companyName, companyImageUrl)  -> {
                            offer.setCompanyName(companyName);
                            offer.setCompanyImageUrl(companyImageUrl);
                            offers.add(offer);

                            if (offers.size() == queryDocumentSnapshots.size()) {
                                Log.d("TAG", "Cantidad de ofertas recuperadas: " + offers.size());

                                Collections.sort(offers, ((o1, o2) -> o2.getDate().compareTo(o1.getDate())));

                                offersLiveData.setValue(offers);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(), "Error loading the offer", Toast.LENGTH_SHORT).show();
                });

        return offersLiveData;
    }

    /**
     * Method that searches for the name and photograph of the company to which the offer belongs.
     * @param companyId ID of the company to search.
     * @param callback A BiConsumer with two String parameters.
     */
    public void  getCompanyInfo(String companyId, BiConsumer<String, String> callback) {
        db.collection("Company").document(companyId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String companyName = documentSnapshot.getString("name");
                        String companyImageUrl = documentSnapshot.getString("profileImage");
                        callback.accept(companyName, companyImageUrl);
                    } else {
                        callback.accept("Unknown", "null");
                    }
                }).addOnFailureListener(e -> {
                    callback.accept("Unknown","null");
                });
    }

    /**
     * Method that obtains a list of offers by company.
     * @param companyId Company ID to obtain offers.
     * @return MutableLiveData with the company's Offerings list.
     */
    public MutableLiveData<List<OfferObject>> getOffersCompany(String companyId) {
        db.collection("Offer")
                .whereEqualTo("companyId", companyId)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OfferObject> offers = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        OfferObject offer = document.toObject(OfferObject.class);

                        getCompanyInfo(offer.getCompanyId(), (companyName, companyImageUrl)-> {
                            offer.setCompanyName(companyName);
                            offer.setCompanyImageUrl(companyImageUrl);
                            offers.add(offer);

                            if (offers.size() == queryDocumentSnapshots.size()) {
                                Log.d("TAG", "Cantidad de ofertas recuperadas: " + offers.size());
                                offersCompany.setValue(offers);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(), "Error loading offers", Toast.LENGTH_SHORT).show();
                });
        return offersCompany;
    }

    /**
     * Method that obtains the offers separated by category
     * @param category A String with the name of the category.
     * @return A MutableLiveData with the list of offers in the category.
     */
    public MutableLiveData<List<OfferObject>> getOfferCategory(String category) {
        MutableLiveData<List<OfferObject>> offerCategory = new MutableLiveData<>();

        db.collection("Offer")
                .whereEqualTo("job_category", category)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OfferObject> offers = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        OfferObject offer = document.toObject(OfferObject.class);

                        getCompanyInfo(offer.getCompanyId(), (companyName, companyImageUrl) -> {
                            offer.setCompanyName(companyName);
                            offer.setCompanyImageUrl(companyImageUrl);
                            offers.add(offer);

                            if (offers.size() == queryDocumentSnapshots.size()) {
                                Log.d("TAG", "Cantidad de ofertas recuperadas: " + offers.size());

                                Collections.sort(offers, ((o1, o2) -> o2.getDate().compareTo(o1.getDate())));

                                offerCategory.setValue(offers);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(), "Error loading the offer", Toast.LENGTH_SHORT).show();
                    Log.d("ERRPR", "No se pueden cargar", e);
                });

        return offerCategory;
    }

    /**
     * Method that stores a specific offer inside a MutableLiveData.
     * @param offerObject offer ID to store.
     */
    public void seleccionar(OfferObject offerObject){
        offerSingle.postValue(offerObject);
    }

    /**
     * Method that returns a offer stored in a MutableLiveData.
     * @return A MutableLiveData with a offer.
     */
   public MutableLiveData<OfferObject> seleccionado(){
        return offerSingle;
    }

}
