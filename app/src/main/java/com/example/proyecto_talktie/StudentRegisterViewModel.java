package com.example.proyecto_talktie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;

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
    private MutableLiveData<Date> birth_date = new MutableLiveData<>();
    private MutableLiveData<String> gender = new MutableLiveData<>();
    private MutableLiveData<Boolean> currently_working = new MutableLiveData<>();
    private MutableLiveData<String> studentId = new MutableLiveData<>();
    private MutableLiveData<String> center = new MutableLiveData<>();
    private MutableLiveData<String> company = new MutableLiveData<>();
    private MutableLiveData<Date> start_date_job = new MutableLiveData<>();
    private MutableLiveData<Date> end_date_job = new MutableLiveData<>();
    private MutableLiveData<Date> start_date_formation = new MutableLiveData<>();
    private MutableLiveData<Date> end_date_formation = new MutableLiveData<>();
    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<String> job_categories = new MutableLiveData<>();
    private MutableLiveData<String> degree = new MutableLiveData<>();



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

    public LiveData<Date> getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date.setValue(birth_date);
    }

    public LiveData<String> getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.setValue(gender);
    }

    public LiveData<Boolean> getCurrently_working() {
        return currently_working;
    }

    public void setCurrently_working(Boolean currently_working) {
        this.currently_working.setValue(currently_working);
    }

    public LiveData<String> getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId.setValue(studentId);
    }

    public LiveData<String> getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center.setValue(center);
    }

    public LiveData<String> getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company.setValue(company);
    }

    public LiveData<Date> getStart_date_job() {
        return start_date_job;
    }

    public void setStart_date_job(Date start_date_job) {
        this.start_date_job.setValue(start_date_job);
    }

    public LiveData<Date> getEnd_date_job() {
        return end_date_job;
    }

    public void setEnd_date_job(Date end_date_job) {
        this.end_date_job.setValue(end_date_job);
    }

    public LiveData<Date> getStart_date_formation() {
        return start_date_formation;
    }

    public void setStart_date_formation(Date start_date_formation) {
        this.start_date_formation.setValue(start_date_formation);
    }

    public LiveData<Date> getEnd_date_formation() {
        return end_date_formation;
    }

    public void setEnd_date_formation(Date end_date_formation) {
        this.end_date_formation.setValue(end_date_formation);
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location.setValue(location);
    }

    public LiveData<String> getJob_categories() {
        return job_categories;
    }

    public void setJob_categories(String job_categories) {
        this.job_categories.setValue(job_categories);
    }

    public LiveData<String> getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree.setValue(degree);
    }
}
