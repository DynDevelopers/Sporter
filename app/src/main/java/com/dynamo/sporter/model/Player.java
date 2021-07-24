package com.dynamo.sporter.model;

public class Player {

    private String id, preferredSports, age, firstname, lastname, email, phoneNo, country, state, city, pincode, description, gender, clubid;
    private boolean clubAdmin;
    public Player() { }
    public Player(String firstname, String lastname, String age, String email, String phoneNo, String preferredSports, String gender, String country, String state, String city, String pincode, String description, String clubid, boolean clubAdmin) {
        this.preferredSports = preferredSports;
        this.age = age;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.preferredSports = preferredSports;
        this.gender = gender;
        this.description = description;
        this.clubid = clubid;
        this.clubAdmin = clubAdmin;
    }

    public String getPreferredSports() {
        return preferredSports;
    }

    public String getAge() {
        return age;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPincode() {
        return pincode;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClubid() {
        return clubid;
    }

    public boolean isClubAdmin() {
        return clubAdmin;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", preferredSports='" + preferredSports + '\'' +
                ", age='" + age + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", pincode='" + pincode + '\'' +
                ", description='" + description + '\'' +
                ", gender='" + gender + '\'' +
                ", clubid='" + clubid + '\'' +
                ", clubAdmin=" + clubAdmin +
                '}';
    }
}