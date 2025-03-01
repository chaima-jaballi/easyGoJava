package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.HistoriqueReclamation;
import com.esprit.tn.pidev.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueReclamationService implements Iservice<HistoriqueReclamation> {

    private final Connection cnx;

    public HistoriqueReclamationService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(HistoriqueReclamation hr) {
        String req = "INSERT INTO historiquereclamation (ticketId, message, dateAction, userId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, hr.getTicketId());
            stm.setString(2, hr.getMessage());
            stm.setTimestamp(3, hr.getDateAction());
            stm.setInt(4, hr.getUserId());

            stm.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'ajout de la réclamation", e);
        }
    }


    @Override
    public void modifier(HistoriqueReclamation hr) {
        // Map pour suivre les champs modifiés et leurs nouvelles valeurs
        Map<String, Object> modifiedFields = new HashMap<>();

        if (hr.getMessage() != null) {
            modifiedFields.put("message", hr.getMessage());
        }
        if (hr.getUserId() != 0) {
            modifiedFields.put("userId", hr.getUserId()); // Correction : utiliser "userId" au lieu de "user_id"
        }
        if (hr.getTicketId() != 0) {
            modifiedFields.put("ticketId", hr.getTicketId());
        }
        if (hr.getDateAction() != null) {
            modifiedFields.put("dateAction", hr.getDateAction());
        }

        if (modifiedFields.isEmpty()) {
            return; // Aucun champ à modifier
        }

        StringBuilder req = new StringBuilder("UPDATE historiquereclamation SET ");
        for (Map.Entry<String, Object> entry : modifiedFields.entrySet()) {
            req.append(entry.getKey()).append(" = ?, ");
        }
        req.setLength(req.length() - 2); // Supprimer la dernière virgule
        req.append(" WHERE id = ?");

        try (PreparedStatement stm = cnx.prepareStatement(req.toString())) {
            int index = 1;
            for (Map.Entry<String, Object> entry : modifiedFields.entrySet()) {
                stm.setObject(index++, entry.getValue());
            }
            stm.setInt(index, hr.getId());

            stm.executeUpdate();
            System.out.println("Réclamation modifiée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la réclamation : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la modification de la réclamation", e);
        }
    }

    @Override
    public void supprimer(HistoriqueReclamation hr) {
        String req = "DELETE FROM historiquereclamation WHERE id = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, hr.getId());
            stm.executeUpdate();
            System.out.println("Réclamation supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression de la réclamation", e);
        }
    }

    @Override
    public List<HistoriqueReclamation> getall() {
        List<HistoriqueReclamation> hrs = new ArrayList<>();
        String req = "SELECT * FROM historiquereclamation";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                HistoriqueReclamation hr = new HistoriqueReclamation();
                hr.setId(rs.getInt("id"));
                hr.setTicketId(rs.getInt("ticketId")); // Correction : utiliser "ticketId" au lieu de "tiketId"
                hr.setMessage(rs.getString("message"));
                hr.setDateAction(rs.getTimestamp("dateAction"));
                hr.setUserId(rs.getInt("userId"));
                hrs.add(hr);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des réclamations", e);
        }
        return hrs;
    }
    @Override
    public String getUserPhoneNumberById(int userId) {
        String phoneNumber = null;
        String query = "SELECT phone_number FROM users WHERE id = ?"; // Adjust table/column names as per your DB schema

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                phoneNumber = rs.getString("phone_number");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider proper logging in production
        }

        return phoneNumber != null ? phoneNumber : ""; // Return empty string if not found
    }
    @Override
    public HistoriqueReclamation getOneById(Integer id) {
        HistoriqueReclamation hr = new HistoriqueReclamation();
        String req = "SELECT * FROM historiquereclamation WHERE id = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    hr.setId(rs.getInt("id"));
                    hr.setTicketId(rs.getInt("ticketId")); // Correction : utiliser "ticketId" au lieu de "tiketId"
                    hr.setMessage(rs.getString("message"));
                    hr.setDateAction(rs.getTimestamp("dateAction"));
                    hr.setUserId(rs.getInt("userId"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la réclamation : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération de la réclamation", e);
        }
        return hr;
    }

    @Override
    public HistoriqueReclamation getone() {
        HistoriqueReclamation hr = new HistoriqueReclamation();
        String req = "SELECT * FROM historiquereclamation LIMIT 1";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            if (rs.next()) {
                hr.setId(rs.getInt("id"));
                hr.setTicketId(rs.getInt("ticketId")); // Correction : utiliser "ticketId" au lieu de "tiketId"
                hr.setMessage(rs.getString("message"));
                hr.setDateAction(rs.getTimestamp("dateAction"));
                hr.setUserId(rs.getInt("userId"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la réclamation : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération de la réclamation", e);
        }
        return hr;
    }
}