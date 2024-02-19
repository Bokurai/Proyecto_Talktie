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
}
