package com.example.proyecto_talktie.models.school;

import com.example.proyecto_talktie.models.school.Teacher;

public class Recommendation {
    private String recommendationText;
    private Teacher teacher;

    public Recommendation() {
    }

    public Recommendation(String recommendationText, Teacher teacher) {
        this.recommendationText = recommendationText;
        this.teacher = teacher;
    }


    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }


    public String getRecommendationText() {
        return recommendationText;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }


}
