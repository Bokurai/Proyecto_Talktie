package com.example.proyecto_talktie;

public class User {
    String name;

    String id;

    String email;

    String password;

    String city;

    String zipcode;

    String home_address;

    String phone_number;

    int profile_image;

    String website;

    public User(){
        super();
    }

    public User(String name, String email, String password, String city, String zipcode, String home_address, String phone_number, int profile_image, String website) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
        this.zipcode = zipcode;
        this.home_address = home_address;
        this.phone_number = phone_number;
        this.profile_image = profile_image;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(int profile_image) {
        this.profile_image = profile_image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }



}
