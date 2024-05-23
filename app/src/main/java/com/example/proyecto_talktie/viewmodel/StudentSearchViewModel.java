package com.example.proyecto_talktie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.company.Business;

public class StudentSearchViewModel extends AndroidViewModel {

    private MutableLiveData<Business> companySelected = new MutableLiveData<>();

    public StudentSearchViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Method that stores a specific company inside a MutableLiveData.
     * @param business company ID to store.
     */
    public void select(Business business) {
        companySelected.setValue(business);
    }

    /**
     * Method that returns a company stored in a MutableLiveData.
     * @return A MutableLiveData with a company.
     */
   public MutableLiveData<Business> selected() {
        return companySelected;
    }


}
