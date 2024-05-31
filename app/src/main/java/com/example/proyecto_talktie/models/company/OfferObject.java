package com.example.proyecto_talktie.models.company;

import java.util.Date;
import java.util.List;

/**
 * Represents an offer posted by a company, containing information such as the offer ID, name,
 * company ID, tags, date, contract time, job category, job description, company name,
 * number of applicants, and company image URL.
 */
public class OfferObject {

    String offerId;
    String name;
    String companyId;
    List<String> tags;
    Date date;
    String contract_time;
    String job_category;
    String job_description;

    String companyName;
     int numApplicants;
    String companyImageUrl;

    /**
     * Constructs an OfferObject with the specified name and company ID.
     * @param name      The name of the offer.
     * @param companyId The ID of the company associated with the offer.
     */
    public OfferObject(String name, String companyId) {
        this.name = name;
        this.companyId = companyId;
    }
    /**
     * Constructs an empty OfferObject with numApplicants initialized to 0.
     */
    public OfferObject() {
        this.numApplicants = 0;
    }
    /**
     * Constructs an OfferObject with the specified number of applicants.
     * @param numApplicants The number of applicants for the offer.
     */
    public OfferObject(int numApplicants) {
        this.numApplicants = numApplicants;
    }

    public String getCompanyImageUrl() {
        return companyImageUrl;
    }

    public void setCompanyImageUrl(String companyImageUrl) {
        this.companyImageUrl = companyImageUrl;
    }

    public int getNumApplicants() {
        return numApplicants;
    }

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getOfferId() {
        return offerId;
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
    public List<String> getTags() {
        return tags;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getContract_time() {
        return contract_time;
    }
    public String getJob_category() {
        return job_category;
    }
    public String getJob_description() {
        return job_description;
    }
}
