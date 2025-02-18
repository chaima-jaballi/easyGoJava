package com.esprit.tn.pidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.esprit.tn.pidev.services.TicketReclamationService;
import com.esprit.tn.pidev.entities.TicketReclamation;
import java.sql.Timestamp;

public class TicketReclamationController {

    @FXML private ComboBox<String> categorieComboBox;
    @FXML private ComboBox<String> statutComboBox;
    @FXML private TextArea descriptionTextArea;
    @FXML private TextField dateCreationField;
    @FXML private Button envoyerButton;

    private final TicketReclamationService ticketReclamationService = new TicketReclamationService();

    @FXML
    public void initialize() {
        // Initialiser les catégories
        categorieComboBox.getItems().addAll("Technique", "Facturation", "Service Client", "Autre");
        categorieComboBox.getSelectionModel().selectFirst();

        // Initialiser les statuts
        statutComboBox.getItems().addAll("En attente", "En cours", "Résolu");
        statutComboBox.getSelectionModel().selectFirst();

        // Remplir automatiquement la date de création
        dateCreationField.setText(java.time.LocalDate.now().toString());

        // Lier l'action du bouton Envoyer
        envoyerButton.setOnAction(event -> envoyerReclamation());
    }

    private void envoyerReclamation() {
        String categorie = categorieComboBox.getValue();
        String statut = statutComboBox.getValue();
        String description = descriptionTextArea.getText().trim();
        String dateText = dateCreationField.getText().trim();

        if (categorie == null || statut == null || description.isEmpty() || dateText.isEmpty()) {
            afficherAlerte("Erreur de saisie", "Tous les champs sont obligatoires.");
            return;
        }

        if (description.length() > 500) {
            afficherAlerte("Erreur de saisie", "La description ne doit pas dépasser 500 caractères.");
            return;
        }

        if (!validerDate(dateText)) {
            afficherAlerte("Erreur de saisie", "Le format de la date est invalide. Utilisez YYYY-MM-DD.");
            return;
        }

        // Convertir la date en Timestamp
        Timestamp timestamp = Timestamp.valueOf(dateText + " 00:00:00");

        // Créer et ajouter la réclamation avec un user_id = 0
        TicketReclamation ticketReclamation = new TicketReclamation(0, 0, categorie, statut, description, timestamp);
        ticketReclamationService.ajouter(ticketReclamation);

        afficherAlerte("Succès", "Réclamation envoyée avec succès !");

        // Réinitialiser les champs
        categorieComboBox.getSelectionModel().clearSelection();
        statutComboBox.getSelectionModel().clearSelection();
        descriptionTextArea.clear();
        dateCreationField.setText(java.time.LocalDate.now().toString());
    }

    private boolean validerDate(String dateText) {
        try {
            java.time.LocalDate.parse(dateText); // Valide le format YYYY-MM-DD
            return true;
        } catch (Exception e) {
            return false;
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
