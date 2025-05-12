package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.TripServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class TripFormController {

    @FXML private TextField departureField;
    @FXML private TextField destinationField;
    @FXML private DatePicker dateField;
    @FXML private TextField timeField;
    @FXML private Spinner<Integer> seatsSpinner;
    @FXML private TextField priceField;

    private Trip trip;
    private final TripServices tripServices = new TripServices();

    public void setTrip(Trip trip) {
        this.trip = trip;
        if (trip != null) {
            // Remplir les champs avec les données du trajet existant
            departureField.setText(trip.getDeparturePoint());
            destinationField.setText(trip.getDestination());
            dateField.setValue(trip.getTripDate().toLocalDateTime().toLocalDate());
            timeField.setText(trip.getDepartureTime().toLocalDateTime().toLocalTime().toString());
            seatsSpinner.getValueFactory().setValue(trip.getAvailableSeats());
            priceField.setText(String.valueOf(trip.getContribution()));
        } else {
            // Nouveau trajet - valeurs par défaut
            seatsSpinner.getValueFactory().setValue(4);
        }
    }

    @FXML
    private void handleSave() {
        try {
            if (trip == null) {
                trip = new Trip();
            }

            trip.setDeparturePoint(departureField.getText());
            trip.setDestination(destinationField.getText());
            trip.setTripDate(Timestamp.valueOf(dateField.getValue().atStartOfDay()));
            trip.setDepartureTime(Timestamp.valueOf(dateField.getValue().atTime(LocalTime.parse(timeField.getText()))));
            trip.setAvailableSeats(seatsSpinner.getValue());
            trip.setContribution(Double.parseDouble(priceField.getText()));

            if (trip.getId() == 0) {
                tripServices.ajouter(trip);
            } else {
                tripServices.modifier(trip);
            }

            closeWindow();
        } catch (Exception e) {
            showAlert("Erreur", "Données invalides", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) departureField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}