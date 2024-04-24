package com.example.proyecto_talktie;

public class User {

    String id;

    String name;

    String email;

    String password;

    String city;

    String zipcode;

    String address;

    String phone;

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
        this.address = home_address;
        this.phone = phone_number;
        this.profile_image = profile_image;
        this.website = website;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
