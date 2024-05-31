package com.example.proyecto_talktie.models.student;

import com.example.proyecto_talktie.models.User;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 * Class that represents the User type named Student, it inherits its variables.
 */
public class Student extends User {

    /*
     * The student's profile picture.
     */
    String profileImage;

    /*
     * The id of the student.
     */
    String studentId;

    /*
     * The description that the student writes about themselves.
     */
    String about;

    /*
     * The student's birth date.
     */
    Date birth_date;

    /*
     * The gender of the student.
     */
    String gender;

    /*
     * The school that the student is studying in.
     */
    String center;

    /*
     * (Optional) The company if the user has ever worked before.
     */
    String company;


    /*
     * (Optional) When did the student start the contract.
     */
    Timestamp start_date_job;

    /*
     * (Optional) When did the student end the contract.
     */
    Timestamp end_date_job;

    /*
     * When did the student start the formation.
     */
    Timestamp start_date_formation;

    /*
     * When did the student end the formation.
     */
    Timestamp end_date_formation;

    /*
     * Location of the center/company.
     */
    String location;

    /*
     * Fields that the student wants to work in.
     */
    String job_categories;

    /*
     * Degree of the studies.
     */
    String degree;

    /*
     * The student's recommendations written by the teachers.
     */
    Map<String, String> recommendations;

    /*
     * The companies that the student has followed.
     */
    List<String> followed;

    /*
     * Empty constructor required by Firebase.
     */
    public Student() {
    }

    /*
     * Constructor with some properties.
     */
    public Student(Date birth_date, String gender,  String degree, Timestamp start_date_formation) {
        super();
        this.birth_date = birth_date;
        this.gender = gender;
        this.degree = degree;
        this.start_date_formation = start_date_formation;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDegree() {
        return degree;
    }

    public Map<String, String> getRecommendations() {
        return recommendations;
    }
}
