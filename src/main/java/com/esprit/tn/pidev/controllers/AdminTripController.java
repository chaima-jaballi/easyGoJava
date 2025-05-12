package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.TripServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class AdminTripController {

    @FXML private TableView<Trip> tripsTable;
    @FXML private TableColumn<Trip, String> departureColumn;
    @FXML private TableColumn<Trip, String> destinationColumn;
    @FXML private TableColumn<Trip, String> dateColumn;
    @FXML private TableColumn<Trip, String> timeColumn;
    @FXML private TableColumn<Trip, Integer> seatsColumn;
    @FXML private TableColumn<Trip, Double> priceColumn;
    // Removed the manage column declaration

    private final TripServices tripServices = new TripServices();
    private final ObservableList<Trip> tripsData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureTableColumns();
        loadTrips();
    }

    private void configureTableColumns() {
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("departurePoint"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));

        dateColumn.setCellValueFactory(cellData -> {
            Timestamp date = cellData.getValue().getTripDate();
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    date != null ? date.toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
        });

        timeColumn.setCellValueFactory(cellData -> {
            Timestamp time = cellData.getValue().getDepartureTime();
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    time != null ? time.toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
        });

        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("contribution"));

        // Removed the manageColumn cell factory configuration
    }

    private void loadTrips() {
        tripsData.setAll(tripServices.getall());
        tripsTable.setItems(tripsData);
    }


    @FXML
    private void handleBackToReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin-reservation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tripsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tableau de bord Admin");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner aux reservations", e.getMessage());
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