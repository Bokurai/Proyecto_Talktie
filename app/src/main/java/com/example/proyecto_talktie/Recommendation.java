package com.example.proyecto_talktie;

public class Recommendation {
    private String idTeacher;
    private String recommendationText;

    public Recommendation(String idTeacher, String recommendationText) {
        this.idTeacher = idTeacher;
        this.recommendationText = recommendationText;
    }

    public String getIdTeacher() {
        return idTeacher;
    }

    public String getRecommendationText() {
        return recommendationText;
    }
}
