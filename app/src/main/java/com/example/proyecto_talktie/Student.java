package com.example.proyecto_talktie;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Student extends User{

    /*
    email se toma del usario
    nombre
    password
    phone
    imagen
    website
     */

    String studentId;

    //Información personal
    String about;

    Date birth_date;
    String gender;

    //Relaciones
    String center;
    String company;

    //Parte de experiencia laboral
    boolean currently_working;
    Date start_date_job;
    Date end_date_job;

    //Parte del formulario de formación
    Date start_date_formation;
    Date end_date_formation;

    //Preferencias
    String location;
    String job_categories;
    String degree;

    //Recommendations
    Map<String, String> recommendations;

    public Student() {
    }

    public Student(Date birth_date, String gender, String school, String degree, Date start_date_formation) {
        this.birth_date = birth_date;
        this.gender = gender;
        this.degree = degree;
        this.start_date_formation = start_date_formation;
    }



}
