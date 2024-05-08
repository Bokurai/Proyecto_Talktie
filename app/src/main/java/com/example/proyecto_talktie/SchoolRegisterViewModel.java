package com.example.proyecto_talktie;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SchoolRegisterViewModel extends AndroidViewModel {

    public SchoolRegisterViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<String> profileImageUri = new MutableLiveData<>();
    private MutableLiveData<String> id = new MutableLiveData<>();
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> schoolCode = new MutableLiveData<>();
    private MutableLiveData<String> schoolType = new MutableLiveData<>();
    private MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private MutableLiveData<String> website = new MutableLiveData<>();
    private MutableLiveData<String> country = new MutableLiveData<>();
    private MutableLiveData<String> city = new MutableLiveData<>();
    private MutableLiveData<String> zipcode = new MutableLiveData<>();
    private MutableLiveData<String> streetAddress = new MutableLiveData<>();
    private MutableLiveData<String> summary = new MutableLiveData<>();
    private MutableLiveData<Timestamp> foundation_date = new MutableLiveData<>();


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

    public void setStreetAddress(String streetAddress) {
        this.streetAddress.setValue(streetAddress);
    }

    public LiveData<String> getStreetAddres() {
        return streetAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    public LiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setProfileImageUri(String uri) {
        profileImageUri.setValue(uri);
    }

    public LiveData<String> getProfileImageUri() {
        return profileImageUri;
    }

    public void setWebsite(String website) {
        this.website.setValue(website);
    }

    public LiveData<String> getWebsite() {
        return website;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode.setValue(schoolCode);
    }

    public LiveData<String> getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType.setValue(schoolType);
    }

    public LiveData<String> getSchoolType() {
        return schoolType;
    }

    public LiveData<String> getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country.setValue(country);
    }

    public LiveData<String> getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary.setValue(summary);
    }

    public LiveData<Timestamp> getFoundationDate() {
        return foundation_date;
    }

    public void setFoundation_date(Timestamp foundation_date) {
        this.foundation_date.setValue(foundation_date);
    }


    public void saveUserFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        DocumentReference newUser = db.collection("User").document(uid);
        String uT = "school";

        Map<String, Object> user = new HashMap<>();
        user.put("id", firebaseAuth.getUid());
        user.put("name", name.getValue());
        user.put("email", email.getValue());
        user.put("city", city.getValue());
        user.put("zipcode", zipcode.getValue());
        user.put("headquarters", streetAddress.getValue());
        user.put("phone", phoneNumber.getValue());
        user.put("profileImage", "");
        user.put("website", website.getValue());
        user.put("userT", uT);

        newUser.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Usuario guardado exitosamente en Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error al guardar usuario en Firestore", e);
                    }
                });

    }

    public void saveSchoolFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        DocumentReference newSchool = db.collection("School").document(uid);

        Map<String, Object> school = new HashMap<>();
        school.put("schoolId", firebaseAuth.getUid());
        school.put("name", name.getValue());
        school.put("email", email.getValue());
        school.put("city", city.getValue());
        school.put("zipcode", zipcode.getValue());
        school.put("headquarters", streetAddress.getValue());
        school.put("phone", phoneNumber.getValue());
        school.put("profileImage", "");
        school.put("website", website.getValue());
        school.put("foundation_date", foundation_date.getValue());
        school.put("school_code", schoolCode.getValue());
        school.put("typeSchool", schoolType.getValue());
        school.put("country", country.getValue());
        school.put("summary", summary.getValue());

       newSchool.set(school)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                       Log.d(TAG, "Usuario guardado exitosamente en Firestore");
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.w(TAG, "Error al guardar usuario en Firestore", e);
                   }
               });

    }
}
