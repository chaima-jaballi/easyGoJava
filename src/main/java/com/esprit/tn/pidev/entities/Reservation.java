package com.esprit.tn.pidev.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    public enum ReservationStatus {
        EN_ATTENTE, CONFIRMEE, REFUSEE, ANNULEE
    }

    private Integer id;
    private Integer tripId;
    private LocalDateTime dateReservation;
    private ReservationStatus etatReservation = ReservationStatus.EN_ATTENTE; // Statut par défaut
    private Double montantTotal;
    private String lieuEscale;
    private Integer nombrePlaces;
    private String typeHandicap;

    // Constructeurs
    public Reservation() {}

    public Reservation(Integer id, Integer tripId, LocalDateTime dateReservation,
                       ReservationStatus etatReservation, Double montantTotal,
                       String lieuEscale, Integer nombrePlaces, String typeHandicap) {
        this.id = id;
        this.tripId = tripId;
        this.dateReservation = dateReservation;
        this.etatReservation = etatReservation;
        this.montantTotal = montantTotal;
        this.lieuEscale = lieuEscale;
        this.nombrePlaces = nombrePlaces;
        this.typeHandicap = typeHandicap;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getTripId() { return tripId; }
    public void setTripId(Integer tripId) { this.tripId = tripId; }
    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }
    public ReservationStatus getEtatReservation() { return etatReservation; }
    public void setEtatReservation(ReservationStatus etatReservation) { this.etatReservation = etatReservation; }
    public Double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }
    public String getLieuEscale() { return lieuEscale; }
    public void setLieuEscale(String lieuEscale) { this.lieuEscale = lieuEscale; }
    public Integer getNombrePlaces() { return nombrePlaces; }
    public void setNombrePlaces(Integer nombrePlaces) { this.nombrePlaces = nombrePlaces; }
    public String getTypeHandicap() { return typeHandicap; }
    public void setTypeHandicap(String typeHandicap) { this.typeHandicap = typeHandicap; }

    public String getEtatReservationAsString() {
        return etatReservation.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}