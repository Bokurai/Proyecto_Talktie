package com.example.proyecto_talktie.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.company.OfferObject;
import com.example.proyecto_talktie.models.student.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class CompanyViewModel extends AndroidViewModel {
    private MutableLiveData<List<OfferObject>> offersCompany = new MutableLiveData<>();
    private MutableLiveData<OfferObject> offerSingle = new MutableLiveData<>();
    MutableLiveData<List<String>> applicantsIds = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Student>> applicantsDetails = new MutableLiveData<>();


    public CompanyViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Method that performs a search in the Offer collection to obtain the IDs of the selected company
     *  of the candidates for a specific offer.
     * @param companyId Id of the company to search.
     */
    public MutableLiveData<List<OfferObject>> getOffersCompany(String companyId) {
        db.collection("Offer")
                .whereEqualTo("companyId", companyId)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OfferObject> offers = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        OfferObject offer = document.toObject(OfferObject.class);
                        offers.add(offer);
                        if (offers.size() == queryDocumentSnapshots.size()) {
                            Log.d("TAG", "Cantidad de ofertas recuperadas: " + offers.size());
                            offersCompany.setValue(offers);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(), "Error loading offers", Toast.LENGTH_SHORT).show();
                });
        return offersCompany;
    }

    /**
     * Method that stores a specific Offer inside a MutableLiveData.
     * @param offerObject to store
     */
    public void seleccionar(OfferObject offerObject){
        offerSingle.postValue(offerObject);
    }

    /**
     * Method that returns a Offer stored in a MutableLiveData.
     * @return A MutableLiveData with a Offer.
     */
    public MutableLiveData<OfferObject> seleccionado(){
        return offerSingle;
    }





}
