package com.example.proyecto_talktie;

import java.util.Date;
import java.util.List;

public class OfferObject {

    String offerId;
    String name;
    String companyId;
    List<String> tags;
    Date date;
    //De aquí se puede extraer el número de aplicantes a la oferta
    List<String> applicantsId;

    //Si el id del usuario esta en la lista de aplicante se vuelve true;
    boolean isApplied;

    // Información propia de los detalles de la oferta
    String contract_time;
    String job_category;
    String job_description;

    public OfferObject() {
    }

    public OfferObject(String name, String companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getApplicantsId() {
        return applicantsId;
    }

    public void setApplicantsId(List<String> applicantsId) {
        this.applicantsId = applicantsId;
    }

    public boolean isApplied() {
        return isApplied;
    }

    public void setApplied(boolean applied) {
        isApplied = applied;
    }
}
