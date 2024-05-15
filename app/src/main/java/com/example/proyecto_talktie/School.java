package com.example.proyecto_talktie;

import java.util.List;

public class School extends User{
    String schoolId;
    String summary;
    String school_code;
    List<String> school_type;
    String profileImage;


    public School(String name, String email, String password, String city, String zipcode, String home_address, String phone_number, String imageProfile, String website, String about, String school_code, List<String> school_type) {
        super(name, email, password, city, zipcode, home_address, phone_number, imageProfile, website);
        this.summary = about;
        this.school_code = school_code;
        this.school_type = school_type;
    }

    //constructor vacío requerido por Firebase
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

    public List<String> getSchool_type() {
        return school_type;
    }

    public void setSchool_type(List<String> school_type) {
        this.school_type = school_type;
    }
}
