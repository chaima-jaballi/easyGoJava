package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Feedback;
import com.esprit.tn.pidev.services.FeedbackService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class FeedbackController {
    @FXML private Label star1, star2, star3, star4, star5;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextArea commentField, suggestionField;
    @FXML private Button submitButton;

    private int rating = 0;
    private final FeedbackService feedbackService = new FeedbackService();
    private int ticketId;

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    @FXML
    public void initialize() {
        // Ajouter des catégories avec émotions
        categoryComboBox.getItems().addAll(
                "😀 Heureux",
                "😢 Triste",
                "😡 En colère",
                "😐 Neutre"
        );
    }


    // Gestion des étoiles
    @FXML private void handleStarClick1() { setRating(1); }
    @FXML private void handleStarClick2() { setRating(2); }
    @FXML private void handleStarClick3() { setRating(3); }
    @FXML private void handleStarClick4() { setRating(4); }
    @FXML private void handleStarClick5() { setRating(5); }

    private void setRating(int ratingValue) {
        rating = ratingValue;
        Label[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < 5; i++) {
            if (i < ratingValue) {
                stars[i].setTextFill(Color.GOLD);
            } else {
                stars[i].setTextFill(Color.LIGHTGRAY);
            }
        }
    }

    @FXML
    private void handleSubmitFeedback() {
        if (rating == 0 || commentField.getText().isEmpty() || categoryComboBox.getValue() == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une note, une catégorie et ajouter un commentaire.");
            return;
        }

        String comment = commentField.getText();
        String suggestion = suggestionField.getText();
        String category = categoryComboBox.getValue();

        Feedback feedback = new Feedback(0, ticketId, rating, comment, null);
        feedbackService.ajouterFeedback(feedback);

        afficherAlerte("Succès", "Feedback soumis avec succès !");
        resetForm();
    }

    private void resetForm() {
        setRating(0);
        commentField.clear();
        suggestionField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
