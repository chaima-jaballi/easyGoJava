
package com.esprit.tn.pidev.entities;

public class Driver {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String vehicleType;

    // Constructeurs
    public Driver() {
    }

    public Driver(int id, String name, String email, String phoneNumber, String vehicleType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.vehicleType = vehicleType;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return name; // Pour afficher le nom du driver dans la ComboBox
    }
}
