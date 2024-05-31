package com.example.proyecto_talktie.models.company;

/*
 * Class that represents the data when a Student applies to a Offer.
 */
public class ApplicantData {

    /*
     * Cover letter of the applicant
     */
    String letter;

    /*
     * Document URL of the applicant.
     */
    String documentUrl;

    /*
     * Experience of the applicant.
     */
    String experience;

    /*
     * Empty constructor required by Firebase.
     */
    public ApplicantData() {
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
