package com.example.proyecto_talktie.models.school;

import com.example.proyecto_talktie.models.User;

/*
 * Class that represents the User type named School, it inherits its variables.
 */
public class School extends User {

    /*
     * Id of the school.
     */
    String schoolId;

    /*
     * Summary of the school.
     */
    String summary;

    /*
     * Code of the school.
     */
    String school_code;

    /*
     * Type of the school.
     */
    String typeSchool;

    /*
     * Headquarters of the school.
     */
    String headquarters;

    /*
     * Profile image of the school.
     */
    String profileImage;

    /*
     * Constructor with some properties.
     */
    public School(String name, String email, String password, String city, String zipcode, String home_address, String phone_number, String profileImage, String website, String about, String school_code, String typeSchool) {
        super(name, email, password, city, zipcode, home_address, phone_number, profileImage, website);
        this.summary = about;
        this.school_code = school_code;
        this.typeSchool = typeSchool;
    }

    /*
     * Empty constructor required by Firebase.
     */
    public School(){
        super();
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String getSchool_code() {
        return school_code;
    }

    public void setSchool_code(String school_code) {
        this.school_code = school_code;
    }

    public String getTypeSchool() {
        return typeSchool;
    }

    public void setTypeSchool(String typeSchool) {
        this.typeSchool = typeSchool;
    }
    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }
}
