package com.example.proyecto_talktie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Business extends User{

    String companyId;
    String sector;
   String typeCompany;
    String summary;
    Date foundation_date;
    String specialties;
    String headquarters;

    List<String> followers;
    List<String> offers;

    //constructor vacío requerido por Firebase
    public Business(){
        super();
    }

    public List<String> getOffers() {
        return offers;
    }

    public void setOffers(List<String> offers) {
        this.offers = offers;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getTypeCompany() {
        return typeCompany;
    }

    public void setTypeCompany(String typeCompany) {
        this.typeCompany = typeCompany;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getFoundation_date() {
        return foundation_date;
    }

    public void setFoundation_date(Date foundation_date) {
        this.foundation_date = foundation_date;
    }

    public String getSpecialties() {
        return specialties;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }
}
