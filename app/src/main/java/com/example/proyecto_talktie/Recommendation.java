package com.example.proyecto_talktie;

public class Recommendation {
    private String idTeacher;
    private String recommendationText;
    private String teacherName;
    private String teacherImage;

    public Recommendation() {
    }

    public Recommendation(String idTeacher, String recommendationText, String teacherName) {
        this.idTeacher = idTeacher;
        this.recommendationText = recommendationText;
        this.teacherName = teacherName;
    }

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getRecommendationText() {
        return recommendationText;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherImage() {
        return teacherImage;
    }

    public void setTeacherImage(String teacherImage) {
        this.teacherImage = teacherImage;
    }
}
