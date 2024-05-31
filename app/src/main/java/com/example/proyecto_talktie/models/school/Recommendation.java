package com.example.proyecto_talktie.models.school;

import com.example.proyecto_talktie.models.school.Teacher;

/*
 * Class that represents the Recommendation object.
 */
public class Recommendation {

    /*
     * Text of the recommendation.
     */
    private String recommendationText;

    /*
     * Instance of the teacher that writes the recommendation.
     */
    private Teacher teacher;

    /*
     * Empty constructor required by Firebase.
     */
    public Recommendation() {
    }

    /*
     * Constructor with some properties.
     */
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
