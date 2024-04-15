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


public class OfferViewModel extends AndroidViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<OfferObject>> offersLiveData = new MutableLiveData<>();

    private MutableLiveData<OfferObject> selectedOffer = new MutableLiveData<>();

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
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e);
                        return;
                    }

                    List<OfferObject> offers = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        OfferObject offer = document.toObject(OfferObject.class);
                        offers.add(offer);
                    }
                    Log.d("TAG", "Cantidad de ofertas recuperadas: " + offers.size());
                    offersLiveData.setValue(offers);
                });

        return offersLiveData;
    }


    void selectOffer(OfferObject offerObject){
        selectedOffer.setValue(offerObject);
    }

    MutableLiveData<OfferObject> getSelectedOffer(){
        return selectedOffer;
    }
}
