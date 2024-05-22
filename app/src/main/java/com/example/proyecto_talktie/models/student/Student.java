package com.example.proyecto_talktie.models.student;

import com.example.proyecto_talktie.models.User;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Student extends User {

    /*
    email se toma del usario
    nombre
    password
    phone
    imagen
    website
     */

    String profileImage;
    String studentId;

    //Información personal
    String about;

    Date birth_date;
    String gender;

    //Relaciones
    String center;
    String company;

    //Parte de experiencia laboral
   // boolean currently_working;
    Timestamp start_date_job;
    Timestamp end_date_job;

    //Parte del formulario de formación
    Timestamp start_date_formation;
    Timestamp end_date_formation;

    //Preferencias
    String location;
    String job_categories;
    String degree;

    //Recommendations
    Map<String, String> recommendations;
    List<String> followed;

    public Student() {
    }

    public Student(Date birth_date, String gender, String school, String degree, Timestamp start_date_formation) {
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

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

  /*  public boolean isCurrently_working() {
        return currently_working;
    }

    public void setCurrently_working(boolean currently_working) {
        this.currently_working = currently_working;
    }*/

    public Timestamp getStart_date_job() {
        return start_date_job;
    }

    public void setStart_date_job(Timestamp start_date_job) {
        this.start_date_job = start_date_job;
    }

    public Timestamp getEnd_date_job() {
        return end_date_job;
    }

    public void setEnd_date_job(Timestamp end_date_job) {
        this.end_date_job = end_date_job;
    }

    public Timestamp getStart_date_formation() {
        return start_date_formation;
    }

    public void setStart_date_formation(Timestamp start_date_formation) {
        this.start_date_formation = start_date_formation;
    }

    public Timestamp getEnd_date_formation() {
        return end_date_formation;
    }

    public void setEnd_date_formation(Timestamp end_date_formation) {
        this.end_date_formation = end_date_formation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJob_categories() {
        return job_categories;
    }

    public void setJob_categories(String job_categories) {
        this.job_categories = job_categories;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Map<String, String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Map<String, String> recommendations) {
        this.recommendations = recommendations;
    }

    public List<String> getFollowed() {
        return followed;
    }

    public void setFollowed(List<String> followed) {
        this.followed = followed;
    }
}
