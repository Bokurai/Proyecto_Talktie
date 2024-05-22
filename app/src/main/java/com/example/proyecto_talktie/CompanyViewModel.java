package com.example.proyecto_talktie;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
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

    //Método que obtiene las ofertas para cada compañía
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
    public void seleccionar(OfferObject offerObject){
        offerSingle.postValue(offerObject);
    }

    MutableLiveData<OfferObject> seleccionado(){
        return offerSingle;
    }





}
