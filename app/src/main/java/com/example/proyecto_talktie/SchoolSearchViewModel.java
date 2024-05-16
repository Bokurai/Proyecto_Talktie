package com.example.proyecto_talktie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SchoolSearchViewModel extends AndroidViewModel {
    private MutableLiveData<School> schoolSelected = new MutableLiveData<>();

    public SchoolSearchViewModel(@NonNull Application application) {
        super(application);
    }
    public void select(School school) {
        schoolSelected.setValue(school);
    }

    MutableLiveData<School> selected() {
        return schoolSelected;
    }

}
