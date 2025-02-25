package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.Feedback;
import com.esprit.tn.pidev.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackService {
    private final Connection cnx;

    public FeedbackService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Ajouter un feedback
    public void ajouterFeedback(Feedback feedback) {
        String req = "INSERT INTO feedback (ticketId, rating, comment, dateFeedback) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, feedback.getTicketId());
            stm.setInt(2, feedback.getRating());
            stm.setString(3, feedback.getComment());
            stm.setTimestamp(4, feedback.getDateFeedback());

            int rowsInserted = stm.executeUpdate();
            System.out.println(rowsInserted + " feedback ajouté.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du feedback : " + e.getMessage());
        }
    }

    // Récupérer les feedbacks d'une réclamation spécifique
    public List<Feedback> getFeedbacksByReclamationId(int reclamationId) {
        List<Feedback> feedbacks = new ArrayList<>();
        String req = "SELECT * FROM feedback WHERE ticketId = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, reclamationId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Feedback feedback = new Feedback(
                        rs.getInt("id"),
                        rs.getInt("ticketId"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("dateFeedback")
                );
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des feedbacks : " + e.getMessage());
        }
        return feedbacks;
    }
}
