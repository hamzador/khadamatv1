package com.example.dell.khadamate.Model;

public class Worker {
    private String firstname;
    private String lastname;
    private String FullName;
    private int dislikes;
    private int likes;
    private double latitude;
    private double longitude;
    private double distance;

    public Worker(){

    }

    public Worker(String firstname, String lastname, int dislikes, int likes, double latitude, double longitude) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dislikes = dislikes;
        this.likes = likes;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Worker(String firstname, String lastname, String fullName, int dislikes, int likes, double latitude, double longitude) {
        this.firstname = firstname;
        this.lastname = lastname;
        FullName = fullName;
        this.dislikes = dislikes;
        this.likes = likes;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "worker{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", dislikes=" + dislikes +
                ", likes=" + likes +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}

