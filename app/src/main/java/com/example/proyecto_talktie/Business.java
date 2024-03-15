package com.example.proyecto_talktie;

import java.util.Date;
import java.util.List;

public class Business extends User{

    String sector;
   List <String> company_type;
    String summary;
    Date foundation_date;
    String specialties;

    //constructor vacío requerido por Firebase
    public Business(){
        super();
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public List<String> getCompany_type() {
        return company_type;
    }

    public void setCompany_type(List<String> company_type) {
        this.company_type = company_type;
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
