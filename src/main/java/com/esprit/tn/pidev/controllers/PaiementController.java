package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Paiement;
import com.esprit.tn.pidev.services.PaiementServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;

public class PaiementController {

    @FXML
    private RadioButton payerEnLigne;

    @FXML
    private RadioButton payerEnReel;

    @FXML
    private RadioButton carteBancaire;

    private String modePaiement;
    private String methodePaiement;
    private int tripId;
    private double montant;

    // Méthode pour définir l'ID du voyage (trip)
    public void setTripId(int tripId) {
        this.tripId = tripId;
        System.out.println("Trip ID reçu : " + tripId);
    }

    // Méthode pour définir le montant du paiement
    public void setMontant(double montant) {
        this.montant = montant;
    }

    // Méthode pour gérer le choix du paiement en ligne
    @FXML
    private void handleOnlinePayment() {
        modePaiement = "En ligne";
        carteBancaire.setDisable(false); // Activer le choix de la carte bancaire
    }

    // Méthode pour gérer le choix du paiement en réel
    @FXML
    private void handleInPersonPayment() {
        modePaiement = "En réel";
        carteBancaire.setDisable(true); // Désactiver le choix de la carte bancaire
    }

    // Méthode pour gérer le clic sur le bouton "Suivant"
    @FXML
    private void handleNext() {
        // Vérifier que le mode de paiement est sélectionné
        if (modePaiement == null) {
            showError("Veuillez sélectionner un mode de paiement.");
            return;
        }

        // Définir la méthode de paiement en fonction du choix de l'utilisateur
        if (carteBancaire.isSelected()) {
            methodePaiement = "Carte bancaire";
        } else {
            methodePaiement = "Non spécifié"; // Valeur par défaut si aucune méthode n'est sélectionnée
        }

        // Créer un nouvel objet Paiement avec les données sélectionnées
        Paiement paiement = new Paiement();
        paiement.setModePaiement(modePaiement);
        paiement.setMethodePaiement(methodePaiement);
        paiement.setMontant(montant);

        // Enregistrer le paiement dans la base de données
        PaiementServices paiementServices = new PaiementServices();
        paiementServices.ajouter(paiement);

        // Si le mode de paiement est "En ligne", charger l'interface online-paiement.fxml
        if ("En ligne".equals(modePaiement)) {
            try {
                // Charger le fichier FXML de l'interface online-paiement.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/online-paiement.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur de l'interface online-paiement.fxml
                OnlinePaymentController onlinePaymentController = loader.getController();

                // Passer l'ID du paiement au contrôleur de l'interface online-paiement.fxml
                onlinePaymentController.setPaymentId(paiement.getId());
                System.out.println("Payment ID envoyé : " + paiement.getId());
                onlinePaymentController.setMontant(montant);

                // Créer une nouvelle scène et afficher l'interface
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Paiement en ligne");
                stage.show();

                // Fermer la fenêtre actuelle
                ((Stage) payerEnLigne.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Erreur lors du chargement de l'interface de paiement en ligne : " + e.getMessage());
            }
        } else {
            // Si le paiement est en réel, fermer la fenêtre actuelle
            ((Stage) payerEnReel.getScene().getWindow()).close();
        }
    }

    // Méthode pour afficher une alerte en cas d'erreur
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour naviguer vers la page de statut de la réservation (optionnel)
    private void navigateToStatutReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/statut-reservation.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle et afficher la nouvelle interface
            Stage stage = (Stage) payerEnLigne.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page statut-reservation : " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            // Charger le fichier FXML de la page statut-reservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client_home.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) payerEnLigne.getScene().getWindow();

            // Afficher la nouvelle interface
            stage.setScene(new Scene(root));
            stage.setTitle("Statut de la réservation");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'interface statut-reservation : " + e.getMessage());
        }
    }
}