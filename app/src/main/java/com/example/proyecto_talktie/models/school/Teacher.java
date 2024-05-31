package com.example.proyecto_talktie.models.school;

import java.util.List;

/*
 * Class that represents the School sub-type named Teacher.
 */
public class Teacher {

    /*
     * Name of the teacher.
     */
    String name;

    /*
     * Profile image of the teacher.
     */
    String profileImage;

    /*
     * Id of the teacher.
     */
    String teacherId;

    /*
     * Email of the teacher.
     */
    String email;

    /*
     * Position of the teacher.
     */
    String position;

    /*
     * Id of the teacher's school.
     */
    String schoolId;

    /*
     * Students recommended by the teacher.
     */
    List<String> recommendedStudents;

    /*
     * Empty constructor required by Firebase.
     */
    public Teacher() {
    }

    /*
     * Constructor with some properties.
     */
    public Teacher(String name, String schoolId) {
        this.name = name;
        this.schoolId = schoolId;
    }

    public List<String> getRecommendedStudents() {
        return recommendedStudents;
    }

    public void setRecommendedStudents(List<String> recommendedStudents) {
        this.recommendedStudents = recommendedStudents;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
