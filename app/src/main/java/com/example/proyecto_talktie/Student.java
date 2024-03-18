package com.example.proyecto_talktie;

import java.util.Date;
import java.util.List;

public class Student extends User{
    List<String> gender;

    Date birth_date;

    //Parte del formulario de formación
    String center;

    Date start_date_formation;

    Date end_date_formation;


    //Parte de experiencia laboral
    String company;

    String location;

    Date start_date_job;

    Date end_date_job;

    boolean currently_working;

    List<String> job_categories;

    public Student(String name, String email, String password, String city, String zipcode, String home_address, String phone_number, int profile_image, String website, List<String> gender, Date birth_date, String center, Date start_date_formation, Date end_date_formation, String company, String location, Date start_date_job, Date end_date_job, boolean currently_working, List<String> job_categories) {
        super(name, email, password, city, zipcode, home_address, phone_number, profile_image, website);
        this.gender = gender;
        this.birth_date = birth_date;
        this.center = center;
        this.start_date_formation = start_date_formation;
        this.end_date_formation = end_date_formation;
        this.company = company;
        this.location = location;
        this.start_date_job = start_date_job;
        this.end_date_job = end_date_job;
        this.currently_working = currently_working;
        this.job_categories = job_categories;
    }


    //constructor vacío requerido por Firebase
    public Student(){}
}
