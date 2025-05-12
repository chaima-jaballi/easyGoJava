package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Reservation;
import com.esprit.tn.pidev.services.ReservationServices;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyReservationController {

    @FXML private TextField placesField;
    @FXML private TextField escaleField;
    @FXML private TextField handicapField;
    @FXML private Label priceLabel;

    private Reservation reservation;
    private final ReservationServices reservationServices = new ReservationServices();
    private double prixUnitaire;

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        this.prixUnitaire = reservation.getMontantTotal() / reservation.getNombrePlaces();

        // Initialiser les champs
        placesField.setText(String.valueOf(reservation.getNombrePlaces()));
        escaleField.setText(reservation.getLieuEscale());
        handicapField.setText(reservation.getTypeHandicap());
        updatePrice();

        // Écouteur pour mise à jour automatique
        placesField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && newValue.matches("\\d*")) {
                updatePrice();
            }
        });
    }

    private void updatePrice() {
        try {
            int places = Integer.parseInt(placesField.getText());
            if (places > 0) {
                double newPrice = places * prixUnitaire;
                priceLabel.setText(String.format("%.2f DT", newPrice));
            } else {
                priceLabel.setText("0.00 DT");
            }
        } catch (NumberFormatException e) {
            priceLabel.setText("0.00 DT");
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Validation des données
            int newPlaces = Integer.parseInt(placesField.getText());
            if (newPlaces <= 0) {
                showAlert("Erreur", "Nombre de places invalide", "Le nombre de places doit être positif");
                return;
            }

            // Mise à jour de la réservation
            reservation.setNombrePlaces(newPlaces);
            reservation.setLieuEscale(escaleField.getText());
            reservation.setTypeHandicap(handicapField.getText());
            reservation.setMontantTotal(newPlaces * prixUnitaire);

            // Sauvegarde
            reservationServices.modifier(reservation);
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Saisie invalide", "Veuillez entrer un nombre valide pour les places");
        } catch (Exception e) {
            showAlert("Erreur", "Échec de la modification", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) placesField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}