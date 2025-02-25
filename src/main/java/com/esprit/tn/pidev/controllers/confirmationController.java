package com.esprit.tn.pidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class confirmationController {

    @FXML
    private Button btnRetour;

    @FXML
    private void initialize() {
        // Action sur le bouton retour
        btnRetour.setOnAction(event -> retournerVersFormulaire());
    }

    private void retournerVersFormulaire() {
        try {
            // Charger la scène précédente (formulaire de réclamation par exemple)
            // Assurez-vous que le chemin du fichier FXML est correct
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listeReclamation.fxml"));

            Parent root = loader.load();  // Charger le fichier FXML

            // Obtenir la fenêtre actuelle et la mettre à jour avec la nouvelle scène
            Stage stage = (Stage) btnRetour.getScene().getWindow(); // Obtenir la fenêtre actuelle
            Scene scene = new Scene(root); // Créer une nouvelle scène avec le root chargé
            stage.setScene(scene); // Appliquer la nouvelle scène à la fenêtre
            stage.show(); // Afficher la scène
        } catch (IOException e) {
            e.printStackTrace();
            // Afficher une alerte ou un message d'erreur pour l'utilisateur en cas de problème
        }
    }
}
