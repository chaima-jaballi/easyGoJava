package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.services.PaiementServices;
import com.esprit.tn.pidev.entities.Paiement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.google.zxing.WriterException;
import javafx.embed.swing.SwingFXUtils;

public class ConfirmationTripController implements Initializable {

    @FXML
    private Glow imageGlow;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private Label paiementDetailsLabel;

    private PaiementServices paiementServices = new PaiementServices();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de générer le QR code: " + e.getMessage());
        }
    }


    @FXML
    void handleHoverEnter(MouseEvent event) {
        // Ajouter un effet de survol aux boutons
        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            // Augmenter l'effet de surbrillance
            button.setStyle(button.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        }
    }

    @FXML
    void handleHoverExit(MouseEvent event) {
        // Retirer l'effet de survol
        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            // Restaurer la taille normale
            button.setStyle(button.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        }
    }

    @FXML
    void handleImageHoverEnter(MouseEvent event) {
        // Effet de brillance sur l'image au survol
        if (imageGlow != null) {
            imageGlow.setLevel(0.5);
        }
    }

    @FXML
    void handleImageHoverExit(MouseEvent event) {
        // Retirer l'effet de brillance
        if (imageGlow != null) {
            imageGlow.setLevel(0.0);
        }
    }

    @FXML
    void handleNewSearch(ActionEvent event) {
        navigateToAddTripPage(event);
    }

    @FXML
    void handleManagePaiement(ActionEvent event) {
        navigateToManagePaiementPage(event);
    }

    private void navigateToManagePaiementPage(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page de gestion des paiements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage-paiement.fxml"));
            Parent root = loader.load();

            // Récupérer le stage (fenêtre) actuel à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de gestion de paiement.");
        }
    }

    private void navigateToAddTripPage(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page d'ajout de voyage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client_home.fxml"));
            Parent root = loader.load();

            // Récupérer le stage (fenêtre) actuel à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page d'ajout de voyage.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.toLowerCase().contains("erreur")) {
            alert = new Alert(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}