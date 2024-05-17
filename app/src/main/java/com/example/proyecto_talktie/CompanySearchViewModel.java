package com.example.proyecto_talktie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CompanySearchViewModel extends AndroidViewModel {
    private MutableLiveData<Business> companySelected = new MutableLiveData<>();

    public CompanySearchViewModel(@NonNull Application application) {
        super(application);
    }
    public void select(Business business) {
        companySelected.setValue(business);
    }

    MutableLiveData<Business> selected() {
        return companySelected;
    }
}
