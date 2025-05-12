package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Reservation;
import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.ReservationServices;
import com.esprit.tn.pidev.services.TripServices;
import javafx.collections.FXCollections;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DriverDashboardController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, String> tripColumn;
    @FXML private TableColumn<Reservation, String> dateColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private TableColumn<Reservation, String> detailsColumn;
    @FXML private TableColumn<Reservation, Void> actionColumn;

    private final ReservationServices reservationServices = new ReservationServices();
    private final TripServices tripServices = new TripServices();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        configureTableColumns();
        loadAllReservations();
    }

    private void configureTableColumns() {

        tripColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        "TRJ " + cellData.getValue().getTripId()));

        dateColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getDateReservation().format(dateFormatter)));

        statusColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getEtatReservationAsString()));

        detailsColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        String.format("%d place(s) - %s",
                                cellData.getValue().getNombrePlaces(),
                                cellData.getValue().getTypeHandicap())));

        // Style des cellules de statut
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status.toLowerCase()) {
                        case "confirmée":
                            setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 3 8; -fx-border-radius: 12; -fx-background-radius: 12;");
                            break;
                        case "en attente":
                            setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 3 8; -fx-border-radius: 12; -fx-background-radius: 12;");
                            break;
                        case "refusée":
                            setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 3 8; -fx-border-radius: 12; -fx-background-radius: 12;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        // Colonne d'actions avec style intégré
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button acceptBtn = new Button("Accepter");
            private final Button rejectBtn = new Button("Refuser");
            private final HBox buttons = new HBox(10, acceptBtn, rejectBtn);
            private final Label statusLabel = new Label();

            {
                // Style des boutons
                acceptBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;");
                rejectBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;");
                statusLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
                buttons.setStyle("-fx-alignment: center; -fx-padding: 5;");

                acceptBtn.setOnAction(event -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    handleReservationAction(res, Reservation.ReservationStatus.CONFIRMEE);
                });

                rejectBtn.setOnAction(event -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    handleReservationAction(res, Reservation.ReservationStatus.REFUSEE);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    Reservation res = getTableView().getItems().get(getIndex());
                    if (res.getEtatReservation() == Reservation.ReservationStatus.EN_ATTENTE) {
                        setGraphic(buttons);
                    } else {
                        String statusText = res.getEtatReservation() == Reservation.ReservationStatus.CONFIRMEE
                                ? "✓ Acceptée" : "✗ Refusée";
                        statusLabel.setText(statusText);
                        setGraphic(statusLabel);
                    }
                }
            }
        });
    }

    private void handleReservationAction(Reservation reservation, Reservation.ReservationStatus status) {
        try {
            // Vérifier si la réservation est déjà traitée
            if (reservation.getEtatReservation() != Reservation.ReservationStatus.EN_ATTENTE) {
                showAlert("Information", "Réservation déjà traitée",
                        "Cette réservation a déjà été " + reservation.getEtatReservationAsString().toLowerCase());
                return;
            }

            // Mettre à jour le statut
            reservationServices.updateReservationStatus(reservation.getId(), status);

            // Si confirmée, mettre à jour les places disponibles
            if (status == Reservation.ReservationStatus.CONFIRMEE) {
                Trip trip = tripServices.getOneById(reservation.getTripId());
                if (trip != null) {
                    int newAvailableSeats = trip.getAvailableSeats() - reservation.getNombrePlaces();
                    if (newAvailableSeats >= 0) {
                        trip.setAvailableSeats(newAvailableSeats);
                        tripServices.modifier(trip);
                    } else {
                        // Annuler la mise à jour si pas assez de places
                        reservationServices.updateReservationStatus(reservation.getId(), Reservation.ReservationStatus.EN_ATTENTE);
                        throw new Exception("Pas assez de places disponibles dans ce trajet");
                    }
                }
            }

            showAlert("Succès", "Réservation mise à jour",
                    "La réservation #" + reservation.getId() + " a été " +
                            (status == Reservation.ReservationStatus.CONFIRMEE ? "acceptée" : "refusée") + " avec succès.");

            // Actualiser le tableau
            loadAllReservations();
        } catch (Exception e) {
            showAlert("Erreur", "Échec de la mise à jour", e.getMessage());
        }
    }

    private void loadAllReservations() {
        try {
            reservationsTable.setItems(FXCollections.observableArrayList(
                    reservationServices.getall()
            ));
            reservationsTable.refresh();
        } catch (Exception e) {
            showAlert("Erreur", "Chargement des réservations",
                    "Impossible de charger les réservations: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToStatus() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/statut-reservation.fxml"));
            Stage stage = (Stage) reservationsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Statut des Réservations");
            stage.centerOnScreen();
        } catch (IOException e) {
            showAlert("Erreur", "Navigation impossible",
                    "Impossible de charger la vue précédente: " + e.getMessage());
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