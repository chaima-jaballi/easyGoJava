package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.TripServices;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TripClientController {

    @FXML
    private TableView<Trip> tripsTable;
    @FXML
    private TableColumn<Trip, String> idColumn; // Changé de Integer à String
    @FXML
    private TableColumn<Trip, String> departureColumn;
    @FXML
    private TableColumn<Trip, String> destinationColumn;
    @FXML
    private TableColumn<Trip, String> dateColumn;
    @FXML
    private TableColumn<Trip, String> timeColumn;
    @FXML
    private TableColumn<Trip, Integer> seatsColumn;
    @FXML
    private TableColumn<Trip, Double> priceColumn;
    @FXML
    private TableColumn<Trip, Void> actionColumn;
    @FXML
    private TextField searchDepartureField;
    @FXML
    private TextField searchDestinationField;
    @FXML
    private DatePicker searchDateField;
    @FXML
    private ComboBox<String> sortComboBox;

    private ObservableList<Trip> tripsData = FXCollections.observableArrayList();
    private TripServices tripServices = new TripServices();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getId();
            return new SimpleStringProperty(id != null ? "TRJ " + id : "");
        });

        idColumn.setCellFactory(column -> new TableCell<Trip, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-text-fill: #1976d2;");
                }
            }
        });

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

        // Colonne d'actions (bouton Réserver)
        actionColumn.setCellFactory(createActionCellFactory());

        // Options de tri
        sortComboBox.getItems().addAll("Plus récents", "Plus anciens", "Prix croissant", "Prix décroissant");
        sortComboBox.getSelectionModel().selectFirst();

        // Charger les données initiales
        loadTrips();

        // Configuration de la recherche
        configureSearch();
    }

    private Callback<TableColumn<Trip, Void>, TableCell<Trip, Void>> createActionCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Trip, Void> call(final TableColumn<Trip, Void> param) {
                return new TableCell<>() {
                    private final Button reserveButton = new Button("Réserver");

                    {
                        reserveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        reserveButton.setOnAction(event -> {
                            Trip trip = getTableView().getItems().get(getIndex());
                            reserveTrip(trip);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(reserveButton);
                        }
                    }
                };
            }
        };
    }

    private void loadTrips() {
        tripsData.clear();
        List<Trip> trips = tripServices.getall();
        tripsData.addAll(trips);
        tripsTable.setItems(tripsData);
    }

    private void configureSearch() {
        // Écouteurs pour la recherche
        searchDepartureField.textProperty().addListener((obs, oldVal, newVal) -> filterTrips());
        searchDestinationField.textProperty().addListener((obs, oldVal, newVal) -> filterTrips());
        searchDateField.valueProperty().addListener((obs, oldVal, newVal) -> filterTrips());
        sortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> sortTrips());
    }

    private void filterTrips() {
        String departure = searchDepartureField.getText().toLowerCase();
        String destination = searchDestinationField.getText().toLowerCase();
        LocalDate date = searchDateField.getValue();

        List<Trip> filteredTrips = tripServices.getall().stream()
                .filter(trip ->
                        trip.getDeparturePoint().toLowerCase().contains(departure) &&
                                trip.getDestination().toLowerCase().contains(destination) &&
                                (date == null || trip.getTripDate().toLocalDateTime().toLocalDate().equals(date)))
                .collect(Collectors.toList());

        tripsData.setAll(filteredTrips);
        sortTrips();
    }

    private void sortTrips() {
        String sortOption = sortComboBox.getValue();
        if (sortOption == null) return;

        switch (sortOption) {
            case "Plus récents":
                tripsData.sort((t1, t2) -> t2.getTripDate().compareTo(t1.getTripDate()));
                break;
            case "Plus anciens":
                tripsData.sort((t1, t2) -> t1.getTripDate().compareTo(t2.getTripDate()));
                break;
            case "Prix croissant":
                tripsData.sort((t1, t2) -> Double.compare(t1.getContribution(), t2.getContribution()));
                break;
            case "Prix décroissant":
                tripsData.sort((t1, t2) -> Double.compare(t2.getContribution(), t1.getContribution()));
                break;
        }
    }

    private void reserveTrip(Trip trip) {
        // Vérifier s'il reste des places disponibles
        if (trip.getAvailableSeats() <= 0) {
            showAlert("Erreur", "Plus de places disponibles", "Désolé, il n'y a plus de places disponibles pour ce trajet.");
            return;
        }

        // Demander le nombre de places à réserver
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Réservation");
        dialog.setHeaderText("Réservation pour le trajet: " + trip.getDeparturePoint() + " → " + trip.getDestination());
        dialog.setContentText("Nombre de places:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(places -> {
            try {
                int nbPlaces = Integer.parseInt(places);
                if (nbPlaces <= 0) {
                    showAlert("Erreur", "Nombre invalide", "Veuillez entrer un nombre de places valide.");
                    return;
                }

                if (nbPlaces > trip.getAvailableSeats()) {
                    showAlert("Erreur", "Places insuffisantes", "Il ne reste que " + trip.getAvailableSeats() + " places disponibles.");
                    return;
                }

                // Ouvrir la fenêtre de réservation
                openReservationWindow(trip, nbPlaces);

            } catch (NumberFormatException e) {
                showAlert("Erreur", "Saisie invalide", "Veuillez entrer un nombre valide.");
            }
        });
    }

    private void openReservationWindow(Trip trip, int nbPlaces) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservation_form.fxml"));
            Parent root = loader.load();

            // Passer les données au contrôleur de réservation
            ReservationFormController controller = loader.getController();
            controller.initData(trip, nbPlaces);

            Stage stage = new Stage();
            stage.setTitle("Confirmation de réservation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur technique", "Impossible d'ouvrir le formulaire de réservation.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleViewReservations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/client_reservations.fxml"));
            Stage stage = (Stage) tripsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewMap() {
        showAlert("Information", "Fonctionnalité à venir", "La visualisation sur carte sera disponible dans une prochaine version.");
    }
}