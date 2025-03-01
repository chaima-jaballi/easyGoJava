package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.TicketReclamation;
import com.esprit.tn.pidev.main.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketReclamationService implements Iservice<TicketReclamation> {

    Connection cnx;

    public TicketReclamationService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(TicketReclamation tr) {
        String req = "INSERT INTO ticketreclamation (user_id, categorie, statut, description,dateCreation) VALUES (?, ?, ?,?,?)";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, tr.getUserId());
            stm.setString(2, tr.getCategorie());
            stm.setString(3, tr.getStatut());
            stm.setString(4, tr.getDescription());
            stm.setTimestamp(5, tr.getDateCreation());



            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(TicketReclamation tr) {
        // Map to track modified fields and their new values
        Map<String, Object> modifiedFields = new HashMap<>();

        if (tr.getCategorie() != null) {
            modifiedFields.put("categorie", tr.getCategorie());
        }
        if (tr.getUserId() != 0) {
            modifiedFields.put("user_id", tr.getUserId());
        }
        if (tr.getDescription() != null) {
            modifiedFields.put("description", tr.getDescription());
        }
        if (tr.getStatut() != null) {
            modifiedFields.put("statut", tr.getStatut());
        }


        if (modifiedFields.isEmpty()) {
            return;
        }

        StringBuilder req = new StringBuilder("UPDATE ticketreclamation SET ");
        for (Map.Entry<String, Object> entry : modifiedFields.entrySet()) {
            req.append(entry.getKey()).append(" = ?, ");
        }
        req.setLength(req.length() - 2);

        req.append(" WHERE id = ?");

        try {
            PreparedStatement stm = cnx.prepareStatement(req.toString());

            int index = 1;
            for (Map.Entry<String, Object> entry : modifiedFields.entrySet()) {
                stm.setObject(index++, entry.getValue());
            }
            stm.setInt(index, tr.getId());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void supprimer(TicketReclamation ticket) {
        try {
            // Supprimer d'abord les feedbacks associés
            String deleteFeedbackQuery = "DELETE FROM feedback WHERE ticketId = ?";
            PreparedStatement psFeedback = cnx.prepareStatement(deleteFeedbackQuery);
            psFeedback.setInt(1, ticket.getId());
            psFeedback.executeUpdate();

            // Supprimer ensuite le ticket
            String deleteTicketQuery = "DELETE FROM ticketreclamation WHERE id = ?";
            PreparedStatement psTicket = cnx.prepareStatement(deleteTicketQuery);
            psTicket.setInt(1, ticket.getId());
            psTicket.executeUpdate();

            System.out.println("Ticket supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TicketReclamation> getall() {
        List<TicketReclamation> trs = new ArrayList<>();
        String req = "SELECT * FROM ticketreclamation";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                TicketReclamation tr = new TicketReclamation();
                tr.setId(rs.getInt("id"));
                tr.setCategorie(rs.getString("categorie"));
                tr.setDateCreation(rs.getTimestamp("dateCreation"));
                tr.setUserId(rs.getInt("user_id"));
                tr.setStatut(rs.getString("statut"));
                tr.setDescription(rs.getString("description"));


                trs.add(tr);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trs;
    }
    @Override
    public String getUserPhoneNumberById(int userId) {
        String phoneNumber = null;
        String query = "SELECT phoneNumber FROM users WHERE id = ?"; // Adjust table/column names as needed
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                phoneNumber = rs.getString("phoneNumber");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération du numéro : " + e.getMessage());
        }

        // Normalize and validate
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            if (!phoneNumber.startsWith("+")) {
                phoneNumber = "+216" + phoneNumber.trim(); // Assuming Tunisian numbers, adjust country code
            }
            if (phoneNumber.matches("^\\+[1-9]\\d{1,14}$")) {
                return phoneNumber;
            }
        }
        return null; // Return null if invalid or not found (not "")
    }
    @Override
    public TicketReclamation getOneById(Integer id) {
        TicketReclamation tr = new TicketReclamation();
        String req = "SELECT * FROM ticketreclamation WHERE id = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                tr.setId(rs.getInt("id"));
                tr.setCategorie(rs.getString("categorie"));
                tr.setDateCreation(rs.getTimestamp("dateCreation"));
                tr.setUserId(rs.getInt("user_id"));
                tr.setStatut(rs.getString("statut"));
                tr.setDescription(rs.getString("description"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tr;
    }


    @Override
    public TicketReclamation getone() {
        TicketReclamation tr = new TicketReclamation();
        String req = "SELECT * FROM ticketreclamation LIMIT 1";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);
            if (rs.next()) {
                tr.setId(rs.getInt("id"));
                tr.setCategorie(rs.getString("categorie"));
                tr.setDateCreation(rs.getTimestamp("dateCreation"));
                tr.setUserId(rs.getInt("user_id"));
                tr.setStatut(rs.getString("statut"));
                tr.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tr;
    }
}