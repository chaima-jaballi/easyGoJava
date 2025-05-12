package com.esprit.tn.pidev.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.TripServices;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.time.LocalDateTime;

public class AddTripController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private TextField departurePointField;

    @FXML
    private ComboBox<String> departureTimePicker;

    @FXML
    private TextField destinationField;

    @FXML
    private ComboBox<String> tripTypeComboBox;

    @FXML
    private TextField relayPointsField;

    @FXML
    private DatePicker tripDateField;

    @FXML
    private ComboBox<String> returnTimePicker;

    @FXML
    private TextField seatsField;

    @FXML
    private TextField contributionField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Remplir les ComboBox avec des heures
        departureTimePicker.setItems(FXCollections.observableArrayList(
                "08:00", "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"
        ));
        returnTimePicker.setItems(FXCollections.observableArrayList(
                "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
                "15:00", "16:00", "17:00", "18:00"
        ));

        // Remplir le ComboBox pour le type de voyage
        tripTypeComboBox.setItems(FXCollections.observableArrayList(
                "Aller seulement", "Retour seulement", "Aller-retour"
        ));

        // Validation pour le champ des sièges
        seatsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                seatsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Validation pour le champ de contribution (prix)
        contributionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                contributionField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        // Gestion du changement de type de voyage
        tripTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateTimePickersBasedOnTripType(newVal);
            }
        });
    }

    private void updateTimePickersBasedOnTripType(String tripType) {
        switch (tripType) {
            case "Aller seulement":
                returnTimePicker.setValue(null);
                returnTimePicker.setDisable(true);
                break;
            case "Retour seulement":
                departureTimePicker.setValue(null);
                departureTimePicker.setDisable(true);
                break;
            case "Aller-retour":
                departureTimePicker.setDisable(false);
                returnTimePicker.setDisable(false);
                break;
        }
    }

    @FXML
    void handleAddTrip(ActionEvent event) {
        if (validateInputs()) {
            try {
                // Récupérer les valeurs des champs
                LocalDate tripDate = tripDateField.getValue();
                String departurePoint = departurePointField.getText();
                String destination = destinationField.getText();
                String departureTime = departureTimePicker.getValue();
                String returnTime = returnTimePicker.getValue();
                int availableSeats = Integer.parseInt(seatsField.getText());
                String tripType = tripTypeComboBox.getValue();
                String relayPoints = relayPointsField.getText();
                double contribution = Double.parseDouble(contributionField.getText());

                // Convertir les dates et heures en LocalDateTime
                LocalDateTime departureDateTime = departureTime != null ?
                        LocalDateTime.of(tripDate, LocalTime.parse(departureTime)) : null;
                LocalDateTime returnDateTime = returnTime != null ?
                        LocalDateTime.of(tripDate, LocalTime.parse(returnTime)) : null;

                // Convertir en Timestamp
                Timestamp departureTimestamp = departureDateTime != null ?
                        Timestamp.valueOf(departureDateTime) : null;
                Timestamp returnTimestamp = returnDateTime != null ?
                        Timestamp.valueOf(returnDateTime) : null;

                // Créer un nouvel objet Trip
                Trip trip = new Trip();
                trip.setTripDate(Timestamp.valueOf(tripDate.atStartOfDay()));
                trip.setDeparturePoint(departurePoint);
                trip.setDestination(destination);
                trip.setDepartureTime(departureTimestamp);
                trip.setReturnTime(returnTimestamp);
                trip.setAvailableSeats(availableSeats);
                trip.setTripType(tripType);
                trip.setRelayPoints(relayPoints);
                trip.setContribution(contribution);

                // Ajouter le trip à la base de données
                TripServices tripServices = new TripServices();
                tripServices.ajouter(trip);

                // Afficher un message de succès
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le trip a été ajouté avec succès !");

                // Naviguer vers la page de paiement
                navigateToPaiementPage(event);

                // Réinitialiser les champs
                clearFields();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du trip : " + e.getMessage());
            }
        }
    }

    private boolean validateInputs() {
        if (!isDateValid()) return false;
        if (!isTripTypeValid()) return false;
        if (!isTimeValid()) return false;
        if (!isFieldsNotEmpty()) return false;
        if (!isContributionValid()) return false;
        return true;
    }

    private boolean isContributionValid() {
        if (contributionField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir un montant.");
            return false;
        }
        try {
            double contribution = Double.parseDouble(contributionField.getText());
            if (contribution <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le montant doit être supérieur à 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Montant invalide. Veuillez entrer un nombre valide.");
            return false;
        }
        return true;
    }

    private boolean isTripTypeValid() {
        if (tripTypeComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un type de voyage.");
            return false;
        }
        return true;
    }

    private boolean isDateValid() {
        if (tripDateField.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date de réservation.");
            return false;
        }

        LocalDate tripDate = tripDateField.getValue();
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusWeeks(1);

        if (tripDate.isBefore(today.plusDays(1))) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date doit être à partir de demain.");
            return false;
        }

        if (tripDate.isAfter(maxDate)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date ne peut pas dépasser une semaine à partir d'aujourd'hui.");
            return false;
        }

        return true;
    }

    private boolean isTimeValid() {
        String tripType = tripTypeComboBox.getValue();

        if ("Aller seulement".equals(tripType) && departureTimePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une heure de départ.");
            return false;
        }

        if ("Retour seulement".equals(tripType) && returnTimePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une heure de retour.");
            return false;
        }

        if ("Aller-retour".equals(tripType)) {
            if (departureTimePicker.getValue() == null || returnTimePicker.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une heure de départ et de retour.");
                return false;
            }

            if (LocalTime.parse(returnTimePicker.getValue()).isBefore(LocalTime.parse(departureTimePicker.getValue()))) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'heure de retour doit être après l'heure de départ.");
                return false;
            }
        }

        return true;
    }

    private boolean isFieldsNotEmpty() {
        if (departurePointField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le point de départ ne peut pas être vide.");
            return false;
        }
        if (destinationField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La destination ne peut pas être vide.");
            return false;
        }
        if (seatsField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer le nombre de places.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        departurePointField.clear();
        destinationField.clear();
        relayPointsField.clear();
        tripDateField.setValue(null);
        tripTypeComboBox.getSelectionModel().clearSelection();
        departureTimePicker.getSelectionModel().clearSelection();
        returnTimePicker.getSelectionModel().clearSelection();
        seatsField.clear();
        contributionField.clear();
        departureTimePicker.setDisable(false);
        returnTimePicker.setDisable(false);
    }

    private void navigateToPaiementPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/statut-reservation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la page de paiement : " + e.getMessage());
        }
    }
}