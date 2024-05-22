package com.example.proyecto_talktie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto_talktie.models.school.School;

public class SchoolSearchViewModel extends AndroidViewModel {
    private MutableLiveData<School> schoolSelected = new MutableLiveData<>();

    public SchoolSearchViewModel(@NonNull Application application) {
        super(application);
    }
    public void select(School school) {
        schoolSelected.setValue(school);
    }

    public MutableLiveData<School> selected() {
        return schoolSelected;
    }

}
