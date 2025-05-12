package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.TripServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class ManageTripController {

    @FXML private Label reservationDateLabel;
    @FXML private Label departurePointLabel;
    @FXML private Label destinationLabel;
    @FXML private Label departureTimeLabel;
    @FXML private Label returnTimeLabel;
    @FXML private Label availableSeatsLabel;
    @FXML private Label tripTypeLabel;
    @FXML private Label contributionLabel;
    @FXML private VBox tripDetailsBox;
    @FXML private ImageView backButton;

    private final TripServices tripServices = new TripServices();

    @FXML
    public void initialize() {
        loadTripData();
    }

    private void loadTripData() {
        Trip lastTrip = tripServices.getLastTrip();
        if (lastTrip != null) {
            if (reservationDateLabel != null)
                reservationDateLabel.setText("Date: " + formatTimestamp(lastTrip.getTripDate()));
            if (departurePointLabel != null)
                departurePointLabel.setText("Départ: " + lastTrip.getDeparturePoint());
            if (destinationLabel != null)
                destinationLabel.setText("Destination: " + lastTrip.getDestination());
            if (departureTimeLabel != null)
                departureTimeLabel.setText("Heure départ: " + formatTimestamp(lastTrip.getDepartureTime()));
            if (returnTimeLabel != null)
                returnTimeLabel.setText("Heure retour: " + formatTimestamp(lastTrip.getReturnTime()));
            if (availableSeatsLabel != null)
                availableSeatsLabel.setText("Places: " + lastTrip.getAvailableSeats());
            if (tripTypeLabel != null)
                tripTypeLabel.setText("Type: " + lastTrip.getTripType());
            if (contributionLabel != null)
                contributionLabel.setText("Prix: " + lastTrip.getContribution() + " DT");
        } else {
            if (tripDetailsBox != null) {
                tripDetailsBox.setVisible(false);
            }
            showAlert("Information", "Aucun voyage trouvé");
        }
    }

    private String formatTimestamp(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
    }

    @FXML
    private void handleModifyReservation() {
        Trip tripToUpdate = tripServices.getLastTrip();
        if (tripToUpdate == null) {
            showAlert("Erreur", "Aucun voyage à modifier");
            return;
        }

        // Création de la boîte de dialogue
        Dialog<Trip> dialog = new Dialog<>();
        dialog.setTitle("Modifier réservation");
        dialog.setHeaderText("Modifier les détails du voyage");

        // Configuration des boutons
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Création du formulaire
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 50, 20, 50));

        // Champs de formulaire
        TextField departureField = new TextField(tripToUpdate.getDeparturePoint());
        TextField destinationField = new TextField(tripToUpdate.getDestination());
        DatePicker datePicker = new DatePicker(tripToUpdate.getTripDate().toLocalDateTime().toLocalDate());

        ComboBox<String> tripTypeCombo = new ComboBox<>();
        tripTypeCombo.getItems().addAll("Aller seulement", "Retour seulement", "Aller-retour");
        tripTypeCombo.setValue(tripToUpdate.getTripType());

        ComboBox<String> departureTimeCombo = new ComboBox<>();
        departureTimeCombo.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");
        departureTimeCombo.setValue(tripToUpdate.getDepartureTime() != null ?
                tripToUpdate.getDepartureTime().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : null);

        ComboBox<String> returnTimeCombo = new ComboBox<>();
        returnTimeCombo.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");
        returnTimeCombo.setValue(tripToUpdate.getReturnTime() != null ?
                tripToUpdate.getReturnTime().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : null);

        TextField seatsField = new TextField(String.valueOf(tripToUpdate.getAvailableSeats()));
        TextField contributionField = new TextField(String.valueOf(tripToUpdate.getContribution()));

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
        tripTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switch (newVal) {
                    case "Aller seulement":
                        returnTimeCombo.setValue(null);
                        returnTimeCombo.setDisable(true);
                        break;
                    case "Retour seulement":
                        departureTimeCombo.setValue(null);
                        departureTimeCombo.setDisable(true);
                        break;
                    case "Aller-retour":
                        departureTimeCombo.setDisable(false);
                        returnTimeCombo.setDisable(false);
                        break;
                }
            }
        });

        // Ajout des champs au formulaire
        grid.add(new Label("Point de départ:"), 0, 0);
        grid.add(departureField, 1, 0);
        grid.add(new Label("Destination:"), 0, 1);
        grid.add(destinationField, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Type de voyage:"), 0, 3);
        grid.add(tripTypeCombo, 1, 3);
        grid.add(new Label("Heure départ:"), 0, 4);
        grid.add(departureTimeCombo, 1, 4);
        grid.add(new Label("Heure retour:"), 0, 5);
        grid.add(returnTimeCombo, 1, 5);
        grid.add(new Label("Places disponibles:"), 0, 6);
        grid.add(seatsField, 1, 6);
        grid.add(new Label("Prix par place (DT):"), 0, 7);
        grid.add(contributionField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        // Conversion du résultat
        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                if (!validateInputs(departureField.getText(), destinationField.getText(),
                        datePicker.getValue(), departureTimeCombo.getValue(),
                        returnTimeCombo.getValue(), seatsField.getText(),
                        contributionField.getText(), tripTypeCombo.getValue())) {
                    return null;
                }

                tripToUpdate.setDeparturePoint(departureField.getText());
                tripToUpdate.setDestination(destinationField.getText());
                tripToUpdate.setTripType(tripTypeCombo.getValue());

                if (departureTimeCombo.getValue() != null) {
                    tripToUpdate.setDepartureTime(Timestamp.valueOf(
                            datePicker.getValue().atTime(LocalTime.parse(departureTimeCombo.getValue()))));
                }

                if (returnTimeCombo.getValue() != null) {
                    tripToUpdate.setReturnTime(Timestamp.valueOf(
                            datePicker.getValue().atTime(LocalTime.parse(returnTimeCombo.getValue()))));
                }

                tripToUpdate.setAvailableSeats(Integer.parseInt(seatsField.getText()));
                tripToUpdate.setContribution(Double.parseDouble(contributionField.getText()));

                return tripToUpdate;
            }
            return null;
        });

        Optional<Trip> result = dialog.showAndWait();
        result.ifPresent(trip -> {
            tripServices.modifier(trip);
            loadTripData();
            showAlert("Succès", "Voyage modifié avec succès");
        });
    }

    @FXML
    private void handleDeleteReservation() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer ce voyage?");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Trip tripToDelete = tripServices.getLastTrip();
            if (tripToDelete != null) {
                tripServices.supprimer(tripToDelete);
                loadTripData();
                showAlert("Succès", "La réservation a été supprimée avec succès");
            }
        }
    }

    @FXML
    private void navigateToStatutReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/statut-reservation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page: " + e.getMessage());
        }
    }

    private boolean validateInputs(String departure, String destination, LocalDate date,
                                   String depTime, String retTime, String seats,
                                   String contribution, String tripType) {
        if (departure == null || departure.trim().isEmpty()) {
            showAlert("Erreur", "Le point de départ ne peut pas être vide");
            return false;
        }

        if (destination == null || destination.trim().isEmpty()) {
            showAlert("Erreur", "La destination ne peut pas être vide");
            return false;
        }

        if (date == null || date.isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date doit être valide et dans le futur");
            return false;
        }

        if (tripType == null) {
            showAlert("Erreur", "Veuillez sélectionner un type de voyage");
            return false;
        }

        if ("Aller seulement".equals(tripType) && (depTime == null || depTime.isEmpty())) {
            showAlert("Erreur", "Veuillez sélectionner une heure de départ");
            return false;
        }

        if ("Retour seulement".equals(tripType) && (retTime == null || retTime.isEmpty())) {
            showAlert("Erreur", "Veuillez sélectionner une heure de retour");
            return false;
        }

        if ("Aller-retour".equals(tripType)) {
            if (depTime == null || depTime.isEmpty() || retTime == null || retTime.isEmpty()) {
                showAlert("Erreur", "Veuillez sélectionner une heure de départ et de retour");
                return false;
            }

            try {
                LocalTime departureTime = LocalTime.parse(depTime);
                LocalTime returnTime = LocalTime.parse(retTime);
                if (returnTime.isBefore(departureTime)) {
                    showAlert("Erreur", "L'heure de retour doit être après l'heure de départ");
                    return false;
                }
            } catch (DateTimeParseException e) {
                showAlert("Erreur", "Format d'heure invalide");
                return false;
            }
        }

        try {
            int seatsCount = Integer.parseInt(seats);
            if (seatsCount <= 0) {
                showAlert("Erreur", "Le nombre de places doit être positif");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Nombre de places invalide");
            return false;
        }

        try {
            double contributionValue = Double.parseDouble(contribution);
            if (contributionValue <= 0) {
                showAlert("Erreur", "Le prix doit être supérieur à 0");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Prix invalide");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}