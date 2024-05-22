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

    public void select(Business business) {
        companySelected.setValue(business);
    }

   public MutableLiveData<Business> selected() {
        return companySelected;
    }


}
