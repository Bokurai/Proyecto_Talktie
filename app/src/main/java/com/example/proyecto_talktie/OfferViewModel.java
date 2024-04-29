package com.example.proyecto_talktie;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class OfferViewModel extends AndroidViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<OfferObject>> offersLiveData = new MutableLiveData<>();
    private MutableLiveData<List<OfferObject>> offersCompany = new MutableLiveData<>();
    private MutableLiveData<OfferObject> offerSingle = new MutableLiveData<>();

    public OfferViewModel(@NonNull Application application) {
        super(application);
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

                        getCompanyName(offer.getCompanyId(), companyName -> {
                            offer.setCompanyName(companyName);
                            offers.add(offer);

                            if (offers.size() == queryDocumentSnapshots.size()) {
                                Log.d("TAG", "Cantidad de ofertas recuperadas: " + offers.size());
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

    //Método que obtiene las ofertas para cada compañía
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

                        getCompanyName(offer.getCompanyId(), companyName -> {
                            offer.setCompanyName(companyName);
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
    public void seleccionar(OfferObject offerObject){
        offerSingle.postValue(offerObject);
    }

    MutableLiveData<OfferObject> seleccionado(){
        return offerSingle;
    }

}
