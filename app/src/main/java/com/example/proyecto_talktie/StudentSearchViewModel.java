package com.example.proyecto_talktie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class StudentSearchViewModel extends AndroidViewModel {
    private boolean isFollowing = false;

    private MutableLiveData<Business> companySelected = new MutableLiveData<>();

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public StudentSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void select(Business business) {
        companySelected.setValue(business);
    }

    MutableLiveData<Business> selected() {
        return companySelected;
    }


}
