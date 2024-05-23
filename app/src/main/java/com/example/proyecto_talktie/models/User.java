package com.example.proyecto_talktie.models;


/*
 * Class that represents the main user.
 */
public class User {

    /*
     * Id of the user.
     */
    String id;

    /*
     * Name of the user.
     */
    String name;

    /*
     * Email of the user.
     */
    String email;

    /*
     * Password of the user.
     */
    String password;

    /*
     * City where the user lives.
     */
    String city;

    /*
     * Zipcode of the user.
     */
    String zipcode;

    /*
     * Street address of the user.
     */
    String address;

    /*
     * Phone number of the user.
     */
    String phone;

    /*
     * Profile image of the user.
     */
    String profile_image;

    /*
     * Personal website of the user.
     */
    String website;

    /*
     * Empty constructor required by Firebase.
     */
    public User(){
        super();
    }

    /*
     * Constructor with the properties.
     */
    public User(String name, String email, String password, String city, String zipcode, String home_address, String phone_number, String profile_image, String website) {
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

    /*
     * Class that represents the main User.
     */
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

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
