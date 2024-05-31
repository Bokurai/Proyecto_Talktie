package com.example.proyecto_talktie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.company.Business;

public class CompanySearchViewModel extends AndroidViewModel {
    private MutableLiveData<Business> companySelected = new MutableLiveData<>();

    public CompanySearchViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Method that stores a specific Business inside a MutableLiveData.
     * @param business object to store
     */
    public void select(Business business) {
        companySelected.setValue(business);
    }

    /**
     * Method that returns a Business stored in a MutableLiveData.
     * @return A MutableLiveData with a Business object.
     */
    public MutableLiveData<Business> selected() {
        return companySelected;
    }
}
