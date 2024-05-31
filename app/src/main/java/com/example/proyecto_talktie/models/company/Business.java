package com.example.proyecto_talktie.models.company;

import com.example.proyecto_talktie.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Class that represents the User type named Business/Company, it inherits its variables.
 */
public class Business extends User {

    /*
     * Id of the company.
     */
    String companyId;

    /*
     * Sector of the company.
     */
    String sector;

    /*
     * Type of the company.
     */
    String typeCompany;

    /*
     * Summary of the company.
     */
    String summary;

    /*
     * Foundation date of the company.
     */
    Date foundation_date;

    /*
     * Fields that the company specializes on.
     */
    String specialties;

    /*
     * Main headquaerters of the company.
     */
    String headquarters;

    /*
     * Job offers that the company has.
     */
    List<String> offers;

    /*
     * Profile image of the company
     */
    String profileImage;

    /*
     * A list of students that follow the company.
     */
   public Map<String, Boolean> followers = new HashMap<>();


    /*
     * Empty constructor required by Firebase.
     */
    public Business(){
        super();
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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
