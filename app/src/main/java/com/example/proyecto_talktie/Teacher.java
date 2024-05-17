package com.example.proyecto_talktie;

import java.util.List;

public class Teacher {
    String name;
    String profileImage;
    String teacherId;
    String email;
    String position;
    String schoolId;
    List<String> recommendedStudents;

    public Teacher() {
    }

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
