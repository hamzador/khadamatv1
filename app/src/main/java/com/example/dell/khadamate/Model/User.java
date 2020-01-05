package com.example.dell.khadamate.Model;

public class User {

    private String FName;
    private String LName;
    private String FullName;
    private String Email;
    private String Phone;
    private String Role;
    private String Address;
    private double Latitude;
    private double Longitude;
    private String Service;

    public User(){}

    public User(String email, String role) {
        Email = email;
        Role = role;
    }

    public User(String FName, String LName, String email, String role) {
        this.FName = FName;
        this.LName = LName;
        Email = email;
        Role = role;
    }

    public User(String FName, String LName, String email, String phone, String role, String address, double latitude, double longitude, String service) {
        this.FName = FName;
        this.LName = LName;
        Email = email;
        Phone = phone;
        Role = role;
        Address = address;
        Latitude = latitude;
        Longitude = longitude;
        Service = service;
    }

    public User(String FName, String LName, String fullName, String email, String role) {
        this.FName = FName;
        this.LName = LName;
        FullName = fullName;
        Email = email;
        Role = role;
    }

    public User(String FName, String LName, String fullName, String email, String phone, String role, String address, double latitude, double longitude, String service) {
        this.FName = FName;
        this.LName = LName;
        FullName = fullName;
        Email = email;
        Phone = phone;
        Role = role;
        Address = address;
        Latitude = latitude;
        Longitude = longitude;
        Service = service;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    @Override
    public String toString() {
        return "User{" +
                "FName='" + FName + '\'' +
                ", LName='" + LName + '\'' +
                ", Email='" + Email + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Role='" + Role + '\'' +
                ", Address='" + Address + '\'' +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", Service='" + Service + '\'' +
                '}';
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}
