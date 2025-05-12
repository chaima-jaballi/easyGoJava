package com.esprit.tn.pidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.esprit.tn.pidev.services.TicketReclamationService;
import com.esprit.tn.pidev.entities.TicketReclamation;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;

public class TicketReclamationController {

    @FXML private Button retourButton;
    @FXML private ComboBox<String> categorieComboBox;
    @FXML private ComboBox<String> statutComboBox;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker dateCreationField;
    @FXML private Button envoyerButton;
    @FXML private Button supprimerButton;

    private final TicketReclamationService ticketReclamationService = new TicketReclamationService();

    @FXML
    public void initialize() {
        // Initialisation des catégories et statuts
        categorieComboBox.getItems().addAll("disponibilité des trajets", "Techniques", "coût et financement", "communication");
        categorieComboBox.getSelectionModel().selectFirst();

        statutComboBox.getItems().addAll("En attente", "En cours", "Résolu");
        statutComboBox.getSelectionModel().selectFirst();

        // Remplir la date de création avec la date actuelle
        dateCreationField.setValue(LocalDate.now());

        // Actions des boutons
        envoyerButton.setOnAction(event -> envoyerReclamation());
        retourButton.setOnAction(event -> retournerVersListeReclamation());  // Associer l'action du bouton Retour
        supprimerButton.setOnAction(event -> supprimerReclamation());
    }

    private void envoyerReclamation() {
        String categorie = categorieComboBox.getValue();
        String description = descriptionTextArea.getText().trim();
        LocalDate date = dateCreationField.getValue();

        // Vérification des champs obligatoires
        if (categorie == null || description.isEmpty() || date == null) {
            afficherAlerte("Erreur de saisie", "Tous les champs sont obligatoires.");
            return;
        }

        // Vérification de la longueur de la description
        if (description.length() > 500) {
            afficherAlerte("Erreur de saisie", "La description ne doit pas dépasser 500 caractères.");
            return;
        }

        // Convertir LocalDate en Timestamp
        Timestamp timestamp = Timestamp.valueOf(date.atStartOfDay());

        // Définir le statut par défaut à "En attente"
        String statut = "En attente";

        // Créer l'objet TicketReclamation
        TicketReclamation ticketReclamation = new TicketReclamation(0, 1, categorie, statut, description, timestamp);

        // Ajouter le ticket à la base de données
        ticketReclamationService.ajouter(ticketReclamation);

        // Afficher une alerte de succès
        afficherAlerte("Succès", "Réclamation envoyée avec succès !");

        // Ouvrir la fenêtre de confirmation
        ouvrirFenetreConfirmation();

        // Réinitialiser les champs
        categorieComboBox.getSelectionModel().selectFirst();
        descriptionTextArea.clear();
        dateCreationField.setValue(LocalDate.now());
    }
    private void supprimerReclamation() {
        descriptionTextArea.clear();
        categorieComboBox.getSelectionModel().selectFirst();
        statutComboBox.getSelectionModel().selectFirst();
        dateCreationField.setValue(LocalDate.now());

        afficherAlerte("Succès", "Réclamation annulée !");
    }


    // Chargement de la fenêtre de confirmation après l'alerte
    private void ouvrirFenetreConfirmation() {
        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Votre réclamation a été envoyée avec succès !");

        confirmation.showAndWait();

        // Vérifier si le fichier FXML existe
        URL fxmlURL = getClass().getResource("/reclamationEnvoyée.fxml");
        if (fxmlURL == null) {
            System.err.println("Le fichier FXML reclamationEnvoyee.fxml n'a pas été trouvé !");
            afficherAlerte("Erreur", "Impossible de charger la fenêtre de confirmation.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Confirmation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible de charger la fenêtre de confirmation.");
        }
    }

    // Modification de la méthode retournerVersListeReclamation
    private void retournerVersListeReclamation() {
        try {
            // Charger le fichier FXML pour l'historique des réclamations
            URL fxmlURL = getClass().getResource("/listeReclamation.fxml");

            // Vérification que le fichier FXML existe
            if (fxmlURL == null) {
                System.err.println("Le fichier FXML historiqueReclamation.fxml n'a pas été trouvé !");
                afficherAlerte("Erreur", "Impossible de charger l'interface Liste des Réclamations.");
                return;
            }

            // Charger le fichier FXML dans une nouvelle scène
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();

            // Créer et afficher la nouvelle scène
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Liste des Réclamations");
            stage.show();

            // Fermer la scène actuelle (facultatif, si tu veux fermer la fenêtre actuelle)
            Stage currentStage = (Stage) retourButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible de charger l'interface Liste des Réclamations.");
        }
    }


    // Déplacement de la méthode afficherAlerte à l'intérieur de la classe
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}