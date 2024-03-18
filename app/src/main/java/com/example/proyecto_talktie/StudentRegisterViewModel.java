package com.example.proyecto_talktie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class StudentRegisterViewModel extends AndroidViewModel {
    public StudentRegisterViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<String> id = new MutableLiveData<>();
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> city = new MutableLiveData<>();
    private MutableLiveData<String> zipcode = new MutableLiveData<>();
    private MutableLiveData<String> homeAddress = new MutableLiveData<>();
    private MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private MutableLiveData<Integer> profileImage = new MutableLiveData<>();
    private MutableLiveData<String> website = new MutableLiveData<>();

    // Métodos para actualizar los datos del usuario
    public void setId(String id) {
        this.id.setValue(id);
    }

    public LiveData<String> getId() {
        return id;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public LiveData<String> getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public LiveData<String> getPassword() {
        return password;
    }

    public void setCity(String city) {
        this.city.setValue(city);
    }

    public LiveData<String> getCity() {
        return city;
    }

    public void setZipcode(String zipcode) {
        this.zipcode.setValue(zipcode);
    }

    public LiveData<String> getZipcode() {
        return zipcode;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress.setValue(homeAddress);
    }

    public LiveData<String> getHomeAddress() {
        return homeAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    public LiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage.setValue(profileImage);
    }

    public LiveData<Integer> getProfileImage() {
        return profileImage;
    }

    public void setWebsite(String website) {
        this.website.setValue(website);
    }

    public LiveData<String> getWebsite() {
        return website;
    }
}
