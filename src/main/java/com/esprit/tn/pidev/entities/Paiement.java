package com.esprit.tn.pidev.entities;

public class Paiement {
    private int id;
    private String modePaiement;
    private String methodePaiement;
    private String numeroCarte; // Numéro de la carte (crypté)
    private String nomTitulaire; // Nom du titulaire de la carte
    private String dateExpiration; // Date d'expiration de la carte (MM/YY)
    private String cvv; // Code de sécurité de la carte (crypté)
    private double montant; // Montant du paiement
    private String paymentIntentId; // Identifiant du paiement Stripe

    // Constructeurs
    public Paiement() {}

    public Paiement(int id, String modePaiement, String methodePaiement, String numeroCarte, String nomTitulaire,
                    String dateExpiration, String cvv, double montant, String paymentIntentId) {
        this.id = id;
        this.modePaiement = modePaiement;
        this.methodePaiement = methodePaiement;
        this.numeroCarte = numeroCarte;
        this.nomTitulaire = nomTitulaire;
        this.dateExpiration = dateExpiration;
        this.cvv = cvv;
        this.montant = montant;
        this.paymentIntentId = paymentIntentId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getMethodePaiement() {
        return methodePaiement;
    }

    public void setMethodePaiement(String methodePaiement) {
        this.methodePaiement = methodePaiement;
    }

    public String getNumeroCarte() {
        return numeroCarte;
    }

    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

    public String getNomTitulaire() {
        return nomTitulaire;
    }

    public void setNomTitulaire(String nomTitulaire) {
        this.nomTitulaire = nomTitulaire;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "id=" + id +
                ", modePaiement='" + modePaiement + '\'' +
                ", methodePaiement='" + methodePaiement + '\'' +
                ", numeroCarte='" + numeroCarte + '\'' +
                ", nomTitulaire='" + nomTitulaire + '\'' +
                ", dateExpiration='" + dateExpiration + '\'' +
                ", cvv='" + cvv + '\'' +
                ", montant=" + montant +
                ", paymentIntentId='" + paymentIntentId + '\'' +
                '}';
    }
}