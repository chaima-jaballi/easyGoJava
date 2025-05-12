package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.TripServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatutReservationController implements Initializable {

    @FXML private Label tripDateLabel;
    @FXML private Label departurePointLabel;
    @FXML private Label destinationLabel;
    @FXML private Label departureTimeLabel;
    @FXML private Label returnTimeLabel;
    @FXML private Label availableSeatsLabel;
    @FXML private Label tripTypeLabel;
    @FXML private Label relayPointsLabel;
    @FXML private Label ContributionLabel;
    @FXML private ImageView backImageView;

    private TripServices tripServices;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tripServices = new TripServices();
        loadLastTrip();

        // Configuration du clic sur l'image de retour
        backImageView.setOnMouseClicked(this::navigateToAddTrip);
    }

    private void loadLastTrip() {
        try {
            Trip trip = tripServices.getLastTrip();

            if (trip != null) {
                tripDateLabel.setText("Date: " + trip.getTripDate());
                departurePointLabel.setText("Départ: " + trip.getDeparturePoint());
                destinationLabel.setText("Destination: " + trip.getDestination());
                departureTimeLabel.setText("Heure départ: " + trip.getDepartureTime());
                returnTimeLabel.setText("Heure retour: " + trip.getReturnTime());
                availableSeatsLabel.setText("Places: " + trip.getAvailableSeats());
                tripTypeLabel.setText("Type: " + trip.getTripType());
                relayPointsLabel.setText("Points relais: " + trip.getRelayPoints());
                ContributionLabel.setText("Prix par place (DT): " + trip.getContribution());

            } else {
                showError("Aucun voyage trouvé");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de chargement: " + e.getMessage());
        }
    }

    @FXML
    private void navigateToAddTrip(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add-trip.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backImageView.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur de navigation: " + e.getMessage());
        }
    }

    @FXML
    private void navigateTodriverdashboardPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/driver_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur de navigation: " + e.getMessage());
        }
    }

    @FXML
    private void navigateTotrajetslist(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/driver_trips.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur de navigation: " + e.getMessage());
        }
    }

    @FXML
    private void navigateToManageTrip(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage-trip.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur de navigation: " + e.getMessage());
        }
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}