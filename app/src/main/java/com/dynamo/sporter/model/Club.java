package com.dynamo.sporter.model;

public class Club {

    private String id = "", name = "", dateCreated = "", ownerid = "", country = "", state = "", city = "", pincode = "", description = "";

    public Club() {}

    public Club(String name, String ownerid, String dateCreated, String country, String state, String city, String pincode, String description) {
        this.name = name;
        this.ownerid = ownerid;
        this.dateCreated = dateCreated;
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String setId(String id) {
        return this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerid() {
        return ownerid;
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

    public String getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        return "Club{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", ownerid='" + ownerid + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", pincode='" + pincode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}