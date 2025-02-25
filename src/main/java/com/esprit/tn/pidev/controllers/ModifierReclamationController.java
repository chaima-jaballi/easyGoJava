package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.TicketReclamation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.time.LocalDate;

import com.esprit.tn.pidev.services.TicketReclamationService;

public class ModifierReclamationController {

    // Injecter le service de réclamation
    private TicketReclamationService reclamationService = new TicketReclamationService();

    @FXML
    private TextField idField;
    @FXML
    private ComboBox<String> categorieComboBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField dateCreationField;
    @FXML
    private Button enregistrerButton;
    @FXML
    private Button annulerButton;
    @FXML
    private Button btnRetour;

    private TicketReclamation reclamationActuelle;

    @FXML
    private void initialize() {
        categorieComboBox.getItems().addAll("Technique", "Facturation", "Service Client", "Autre");
        dateCreationField.setEditable(false);
    }

    public void initData(TicketReclamation reclamation) {
        if (reclamation != null) {
            this.reclamationActuelle = reclamation;
            idField.setText(String.valueOf(reclamation.getId()));
            categorieComboBox.setValue(reclamation.getCategorie());
            descriptionTextArea.setText(reclamation.getDescription());
            dateCreationField.setText(reclamation.getDateCreation().toString());
        }
    }

    @FXML
    private void handleEnregistrer(ActionEvent event) {
        if (idField.getText().isEmpty() || categorieComboBox.getSelectionModel().isEmpty() ||
                descriptionTextArea.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        if (descriptionTextArea.getText().length() > 500) {
            showAlert("Erreur", "La description ne peut pas dépasser 500 caractères.");
            return;
        }

        // Mettre à jour les valeurs de la réclamation
        reclamationActuelle.setCategorie(categorieComboBox.getValue());
        reclamationActuelle.setDescription(descriptionTextArea.getText());

        // Appeler le service pour mettre à jour la réclamation dans la base de données
        reclamationService.modifier(reclamationActuelle);

        showAlert("Succès", "La réclamation a été modifiée avec succès !");
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        ((Stage) annulerButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        ((Stage) btnRetour.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
