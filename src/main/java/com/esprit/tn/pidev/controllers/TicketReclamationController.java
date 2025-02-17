package com.esprit.tn.pidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.esprit.tn.pidev.services.TicketReclamationService;
import com.esprit.tn.pidev.entities.TicketReclamation;
import java.sql.Timestamp;

public class TicketReclamationController {

    @FXML private ComboBox<String> categorieComboBox;
    @FXML private TextArea descriptionTextArea;
    @FXML private TextField dateCreationField;
    @FXML private Button envoyerButton;

    private final TicketReclamationService ticketReclamationService = new TicketReclamationService();

    @FXML
    public void initialize() {
        envoyerButton.setOnAction(event -> envoyerReclamation());
    }

    private void envoyerReclamation() {
        String categorie = categorieComboBox.getValue();
        String description = descriptionTextArea.getText().trim();
        String dateText = dateCreationField.getText().trim();

        if (categorie == null || description.isEmpty() || dateText.isEmpty()) {
            afficherAlerte("Erreur de saisie", "Tous les champs sont obligatoires.");
            return;
        }

        if (description.length() > 500) {
            afficherAlerte("Erreur de saisie", "La description ne doit pas dépasser 500 caractères.");
            return;
        }
        if (categorie == null || !categorieComboBox.getItems().contains(categorie)) {
            erreurCategorieLabel.setText("Veuillez sélectionner une catégorie valide.");
            valide = false;
        }

        // Convertir la date saisie en Timestamp sans contrôle de validation
        Timestamp timestamp = Timestamp.valueOf(dateText + " 00:00:00");

        TicketReclamation ticketReclamation = new TicketReclamation(0, 0, categorie, "Nouveau", description, timestamp); // User ID should be set properly

        ticketReclamationService.ajouter(ticketReclamation);

        afficherAlerte("Succès", "Réclamation envoyée avec succès !");

        categorieComboBox.getSelectionModel().clearSelection();
        descriptionTextArea.clear();
        dateCreationField.clear();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
