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

public class BusinessRegisterViewModel extends AndroidViewModel {
    public BusinessRegisterViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<String> profileImageUri = new MutableLiveData<>();
    private MutableLiveData<String> id = new MutableLiveData<>();
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> city = new MutableLiveData<>();
    private MutableLiveData<String> zipcode = new MutableLiveData<>();
    private MutableLiveData<String> homeAddress = new MutableLiveData<>();
    private MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private MutableLiveData<String> website = new MutableLiveData<>();
    private MutableLiveData<String> companycode = new MutableLiveData<>();
    private MutableLiveData<String> sector = new MutableLiveData<>();
    private MutableLiveData<String> country = new MutableLiveData<>();
    private MutableLiveData<String> companytype = new MutableLiveData<>();
    private MutableLiveData<String> summary = new MutableLiveData<>();
    private MutableLiveData<Timestamp> foundation_date = new MutableLiveData<>();
    private MutableLiveData<String> specialities = new MutableLiveData<>();



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

    public void setCompanycode(String companycode) {
        this.companycode.setValue(companycode);
    }

    public LiveData<String> getCompanycode() {
        return companycode;
    }

    public LiveData<String> getSector(){
        return sector;
    }

    public void setSector(String sector) {
        this.sector.setValue(sector);
    }

    public void setCompanytype(String companytype) {
        this.companycode.setValue(companytype);
    }

    public LiveData<String> getCompanytype() {
        return companytype;
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

    public LiveData<String> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String specialities) {
        this.specialities.setValue(specialities);
    }



    public void saveUserFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        DocumentReference newUser = db.collection("User").document(uid);
        String uT = "business";

        Map<String, Object> user = new HashMap<>();
        user.put("id", firebaseAuth.getUid());
        user.put("name", name.getValue());
        user.put("email", email.getValue());
        user.put("city", city.getValue());
        user.put("zipcode", zipcode.getValue());
        user.put("homeAddress", homeAddress.getValue());
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


    public void saveStudentFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        DocumentReference newStudent = db.collection("Business").document(uid);

        Map<String, Object> business = new HashMap<>();
        business.put("studentId", firebaseAuth.getUid());
        business.put("name", name.getValue());
        business.put("email", email.getValue());
        business.put("city", city.getValue());
        business.put("zipcode", zipcode.getValue());
        business.put("homeAddress", homeAddress.getValue());
        business.put("phone", phoneNumber.getValue());
        business.put("profileImage", "");
        business.put("website", website.getValue());
        business.put("foundation_date", birth_date.getValue());
        business.put("gender", gender.getValue());
        business.put("currently_working", currently_working.getValue());
        business.put("center", center.getValue());
        business.put("company", company.getValue());
        business.put("startJob", start_date_job.getValue());
        business.put("endJob", end_date_job.getValue());
        business.put("starFormation", start_date_formation.getValue());
        business.put("endFormation", end_date_formation.getValue());
        business.put("location", locationSchoolFormation.getValue());
        business.put("jobLocation", locationJobStudent.getValue());
        business.put("jobCategories", job_categories.getValue());
        business.put("degree", degree.getValue());
        business.put("country", country.getValue());
        business.put("jobTitle", jobTitle.getValue());

        newStudent.set(business)
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
}
