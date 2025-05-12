package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.Paiement;
import com.esprit.tn.pidev.main.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaiementServices implements Iservice<Paiement> {

    private Connection cnx;

    public PaiementServices() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public void ajouter(Paiement paiement) {
        String req = "INSERT INTO paiement (mode_paiement, methode_paiement, numero_carte, nom_titulaire, date_expiration, cvv, montant, payment_intent_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, paiement.getModePaiement());
            stm.setString(2, paiement.getMethodePaiement());
            stm.setString(3, paiement.getNumeroCarte() != null ? paiement.getNumeroCarte() : "");
            stm.setString(4, paiement.getNomTitulaire() != null ? paiement.getNomTitulaire() : "");
            stm.setString(5, paiement.getDateExpiration() != null ? paiement.getDateExpiration() : null);
            stm.setString(6, paiement.getCvv() != null ? paiement.getCvv() : "");
            stm.setDouble(7, paiement.getMontant());
            stm.setString(8, paiement.getPaymentIntentId());

            stm.executeUpdate();
            System.out.println("Paiement ajouté avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'enregistrement du paiement : " + e.getMessage(), e);
        }
    }
    @Override
    public void supprimer(Paiement paiement) {
        String req = "DELETE FROM paiement WHERE id = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, paiement.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Paiement> getall() {
        List<Paiement> paiements = new ArrayList<>();
        String req = "SELECT * FROM paiement";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                Paiement paiement = new Paiement();
                paiement.setId(rs.getInt("id"));
                paiement.setModePaiement(rs.getString("mode_paiement"));
                paiement.setMethodePaiement(rs.getString("methode_paiement"));
                paiement.setNumeroCarte(rs.getString("numero_carte"));
                paiement.setNomTitulaire(rs.getString("nom_titulaire"));
                paiement.setDateExpiration(rs.getString("date_expiration"));
                paiement.setCvv(rs.getString("cvv"));
                paiement.setMontant(rs.getDouble("montant"));
                paiement.setPaymentIntentId(rs.getString("payment_intent_id"));

                paiements.add(paiement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paiements;
    }

    // In PaiementServices.java
    public Paiement getOneById(Integer id) {
        Paiement paiement = null; // Initialize to null instead of new Paiement()
        String req = "SELECT * FROM paiement WHERE id = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                paiement = new Paiement(); // Only create if found
                paiement.setId(rs.getInt("id"));
                paiement.setModePaiement(rs.getString("mode_paiement"));
                paiement.setMethodePaiement(rs.getString("methode_paiement"));
                paiement.setNumeroCarte(rs.getString("numero_carte"));
                paiement.setNomTitulaire(rs.getString("nom_titulaire"));
                paiement.setDateExpiration(rs.getString("date_expiration"));
                paiement.setCvv(rs.getString("cvv"));
                paiement.setMontant(rs.getDouble("montant"));
                paiement.setPaymentIntentId(rs.getString("payment_intent_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paiement;
    }

    @Override
    public Paiement getone() {
        Paiement paiement = new Paiement();
        String req = "SELECT * FROM paiement LIMIT 1";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);
            if (rs.next()) {
                paiement.setId(rs.getInt("id"));
                paiement.setModePaiement(rs.getString("mode_paiement"));
                paiement.setMethodePaiement(rs.getString("methode_paiement"));
                paiement.setNumeroCarte(rs.getString("numero_carte")); // Décryptez cette valeur
                paiement.setNomTitulaire(rs.getString("nom_titulaire"));
                paiement.setDateExpiration(rs.getString("date_expiration"));
                paiement.setCvv(rs.getString("cvv")); // Décryptez cette valeur
                paiement.setMontant(rs.getDouble("montant"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paiement;
    }
    public Paiement getDernierPaiement() {
        String req = "SELECT * FROM paiement ORDER BY id DESC LIMIT 1";
        Paiement paiement = null;
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);
            if (rs.next()) {
                paiement = new Paiement();
                paiement.setId(rs.getInt("id"));
                paiement.setModePaiement(rs.getString("mode_paiement"));
                paiement.setMethodePaiement(rs.getString("methode_paiement"));
                paiement.setNumeroCarte(rs.getString("numero_carte"));
                paiement.setNomTitulaire(rs.getString("nom_titulaire"));
                paiement.setDateExpiration(rs.getString("date_expiration"));
                paiement.setCvv(rs.getString("cvv"));
                paiement.setMontant(rs.getDouble("montant"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paiement;
    }
    public void updatePaiement(Paiement paiement) {
        String req = "UPDATE paiement SET mode_paiement = ?, methode_paiement = ?, numero_carte = ?, nom_titulaire = ?, date_expiration = ?, cvv = ?, montant = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, paiement.getModePaiement());
            pst.setString(2, paiement.getMethodePaiement());
            pst.setString(3, paiement.getNumeroCarte());
            pst.setString(4, paiement.getNomTitulaire());
            pst.setString(5, paiement.getDateExpiration());
            pst.setString(6, paiement.getCvv());
            pst.setDouble(7, paiement.getMontant());
            pst.setInt(8, paiement.getId()); // Ajouter cette ligne pour définir l'ID

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Paiement getById(int id) {
        String req = "SELECT * FROM paiement WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(req)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Paiement paiement = new Paiement();
                paiement.setId(rs.getInt("id"));
                paiement.setMontant(rs.getDouble("montant"));
                paiement.setModePaiement(rs.getString("mode_paiement"));
                paiement.setMethodePaiement(rs.getString("methode_paiement"));
                paiement.setNumeroCarte(rs.getString("numero_carte"));
                paiement.setNomTitulaire(rs.getString("nom_titulaire"));
                paiement.setDateExpiration(rs.getString("date_expiration"));
                paiement.setCvv(rs.getString("cvv"));
                return paiement;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du paiement", e);
        }
        return null;
    }

    public void modifier(Paiement paiement) {
        String req = "UPDATE paiement SET mode_paiement=?, methode_paiement=?, numero_carte=?, " +
                "nom_titulaire=?, date_expiration=?, cvv=?, montant=? WHERE id=?";

        try (PreparedStatement stmt = cnx.prepareStatement(req)) {
            stmt.setString(1, paiement.getModePaiement());
            stmt.setString(2, paiement.getMethodePaiement());
            stmt.setString(3, paiement.getNumeroCarte());
            stmt.setString(4, paiement.getNomTitulaire());
            stmt.setString(5, paiement.getDateExpiration());
            stmt.setString(6, paiement.getCvv());
            stmt.setDouble(7, paiement.getMontant());
            stmt.setInt(8, paiement.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du paiement", e);
        }
    }
    }


