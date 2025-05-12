package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Reservation;
import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.ReservationServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;

public class ReservationFormController {

    @FXML private Label tripInfoLabel;
    @FXML private Label priceLabel;
    @FXML private ComboBox<String> handicapTypeComboBox;
    @FXML private TextField escaleField;
    @FXML private Button confirmButton;

    private Trip trip;
    private int nbPlaces;

    public void initData(Trip trip, int nbPlaces) {
        this.trip = trip;
        this.nbPlaces = nbPlaces;

        tripInfoLabel.setText(String.format("Trajet: %s → %s (%s places)",
                trip.getDeparturePoint(), trip.getDestination(), nbPlaces));

        double totalPrice = trip.getContribution() * nbPlaces;
        priceLabel.setText(String.format("Prix total: %.2f DT", totalPrice));

        handicapTypeComboBox.getItems().addAll("Aucun", "Mobilité réduite", "Malvoyant", "Autre");
        handicapTypeComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleConfirmReservation() {
        Reservation reservation = new Reservation();
        reservation.setTripId(trip.getId());
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setMontantTotal(trip.getContribution() * nbPlaces);
        reservation.setLieuEscale(escaleField.getText());
        reservation.setNombrePlaces(nbPlaces);
        reservation.setTypeHandicap(handicapTypeComboBox.getValue());

        try {
            new ReservationServices().ajouter(reservation);
            showAlert("Succès", "Réservation enregistrée",
                    "Votre réservation " + reservation.getId() + " est en attente de confirmation");
            confirmButton.getScene().getWindow().hide();
        } catch (Exception e) {
            showAlert("Erreur", "Échec de la réservation", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}