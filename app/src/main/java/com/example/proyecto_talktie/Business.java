package com.example.proyecto_talktie;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Business extends User{

    String companyId;
    String sector;
   String typeCompany;
    String summary;
    Date foundation_date;
    String specialties;
    String headquarters;
    List<String> offers;

    Map<String, Boolean> followers = new HashMap<>();


    //constructor vacío requerido por Firebase
    public Business(){
        super();
    }

    public Map<String, Boolean> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, Boolean> followers) {
        this.followers = followers;
    }

    public List<String> getOffers() {
        return offers;
    }


    public void setOffers(List<String> offers) {
        this.offers = offers;
    }


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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
