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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DriverTripsController {

    @FXML private TableView<Trip> tripsTable;
    @FXML private TableColumn<Trip, String> departureColumn;
    @FXML private TableColumn<Trip, String> destinationColumn;
    @FXML private TableColumn<Trip, String> dateColumn;
    @FXML private TableColumn<Trip, String> timeColumn;
    @FXML private TableColumn<Trip, Integer> seatsColumn;
    @FXML private TableColumn<Trip, Double> priceColumn;
    @FXML private TableColumn<Trip, Void> manageColumn;

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

        manageColumn.setCellFactory(param -> new TableCell<>() {
            private final Button manageBtn = new Button("Gérer trajet");
            private final HBox container = new HBox(manageBtn);

            {
                manageBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                manageBtn.setOnAction(event -> {
                    Trip trip = getTableView().getItems().get(getIndex());
                    handleManageTrip(trip);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void loadTrips() {
        tripsData.setAll(tripServices.getall());
        tripsTable.setItems(tripsData);
    }

    @FXML
    private void handleAddTrip() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add-trip.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter un nouveau trajet");
            stage.setScene(new Scene(root, 600, 400));

            stage.setOnHidden(e -> loadTrips());
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'ajout",
                    "Vérifiez que add-trip.fxml existe dans le bon chemin");
            e.printStackTrace();
        }
    }

    private void handleManageTrip(Trip trip) {
        try {
            tripServices.setCurrentTrip(trip);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage-trip1.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gérer le trajet " );
            stage.setScene(new Scene(root, 800, 600));

            stage.setOnHidden(e -> loadTrips());
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface de gestion",
                    "Vérifiez que manage-trip.fxml existe dans le bon chemin");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/statut-reservation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tripsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tableau de bord Conducteur");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner à l'accueil", e.getMessage());
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