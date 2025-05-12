package com.esprit.tn.pidev.entities;

import java.util.Objects;
import java.sql.Timestamp;

public class Trip {
    private int id;
    private Timestamp tripDate;
    private String departurePoint;
    private String destination;
    private Timestamp departureTime;
    private Timestamp returnTime;
    private int availableSeats;
    private String tripType;
    private Double contribution;
    private String relayPoints;

    public Trip() {}

    // Constructeur
    public Trip(int id, Timestamp tripDate, String departurePoint, String destination,
                Timestamp departureTime, Timestamp returnTime, int availableSeats, String tripType,
                Double contribution, String relayPoints) {
        this.id = id;
        this.tripDate = tripDate;
        this.departurePoint = departurePoint;
        this.destination = destination;
        this.departureTime = departureTime;
        this.returnTime = returnTime;
        this.availableSeats = availableSeats;
        this.tripType = tripType;
        this.contribution = contribution;
        this.relayPoints = relayPoints;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTripDate() {
        return tripDate;
    }

    public void setTripDate(Timestamp tripDate) {
        this.tripDate = tripDate;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public Double getContribution() {
        return contribution;
    }

    public void setContribution(Double contribution) {
        this.contribution = contribution;
    }


    public String getRelayPoints() {
        return relayPoints;
    }

    public void setRelayPoints(String relayPoints) {
        this.relayPoints = relayPoints;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id == trip.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", tripDate=" + tripDate +
                ", departurePoint='" + departurePoint + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", returnTime=" + returnTime +
                ", availableSeats=" + availableSeats +
                ", tripType='" + tripType + '\'' +
                ", contribution=" + contribution +
                ", relayPoints='" + relayPoints + '\'' +
                '}';
    }
}