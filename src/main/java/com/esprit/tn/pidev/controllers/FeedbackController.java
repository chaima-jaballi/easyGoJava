package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Feedback;
import com.esprit.tn.pidev.services.FeedbackService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FeedbackController {
    @FXML private TextField ratingField;
    @FXML private TextArea commentField;
    @FXML private Label messageLabel;

    private int ticketId;
    private final FeedbackService feedbackService = new FeedbackService();

    // Setter pour définir l'ID du ticket
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    @FXML
    private void handleSubmitFeedback() {
        try {
            if (ratingField.getText().isEmpty() || commentField.getText().isEmpty()) {
                afficherAlerte("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            int rating = Integer.parseInt(ratingField.getText());
            if (rating < 1 || rating > 5) {
                afficherAlerte("Erreur", "La note doit être entre 1 et 5.");
                return;
            }

            String comment = commentField.getText();
            Feedback feedback = new Feedback(0, ticketId, rating, comment, null);
            feedbackService.ajouterFeedback(feedback);

            afficherAlerte("Succès", "Feedback soumis avec succès !");
            ratingField.clear();
            commentField.clear();

        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Veuillez entrer une note valide.");
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}