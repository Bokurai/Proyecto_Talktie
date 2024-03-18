package com.example.proyecto_talktie;

import java.util.List;

public class School extends User{

    String schoolId;
    String about;
    String school_code;
    List<String> school_type;


    public School(String name, String email, String password, String city, String zipcode, String home_address, String phone_number, int profile_image, String website, String about, String school_code, List<String> school_type) {
        super(name, email, password, city, zipcode, home_address, phone_number, profile_image, website);
        this.about = about;
        this.school_code = school_code;
        this.school_type = school_type;
    }

    //constructor vacío requerido por Firebase
    public School(){
        super();
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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
