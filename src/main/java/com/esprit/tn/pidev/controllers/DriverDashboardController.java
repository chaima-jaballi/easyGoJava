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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DriverDashboardController {

    @FXML private TableView<Reservation> reservationsTable;
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
        styleTable();
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

        actionColumn.setCellFactory(createActionCellFactory());
    }

    private Callback<TableColumn<Reservation, Void>, TableCell<Reservation, Void>> createActionCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Reservation, Void> call(final TableColumn<Reservation, Void> param) {
                return new TableCell<>() {
                    private final Button acceptBtn = new Button("Accepter");
                    private final Button rejectBtn = new Button("Refuser");
                    private final HBox buttons = new HBox(10, acceptBtn, rejectBtn);
                    private final Label statusLabel = new Label();

                    {
                        acceptBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                        rejectBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                        buttons.setStyle("-fx-alignment: center;");

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
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Reservation res = getTableView().getItems().get(getIndex());
                            if (res.getEtatReservation() == Reservation.ReservationStatus.EN_ATTENTE) {
                                setGraphic(buttons);
                            } else {
                                statusLabel.setText(res.getEtatReservation() == Reservation.ReservationStatus.CONFIRMEE
                                        ? "✓ Acceptée" : "✗ Refusée");
                                setGraphic(statusLabel);
                            }
                        }
                    }
                };
            }
        };
    }

    private void styleTable() {
        // Style des en-têtes
        reservationsTable.lookupAll(".column-header").forEach(header ->
                header.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold;"));

        // Style des lignes
        reservationsTable.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            row.setStyle("-fx-cell-size: 40px;");

            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    if (row.getIndex() % 2 == 1) {
                        row.setStyle("-fx-background-color: #e3f2fd;");
                    } else {
                        row.setStyle("-fx-background-color: white;");
                    }
                }
            });
            return row;
        });
    }

    private void handleReservationAction(Reservation reservation, Reservation.ReservationStatus status) {
        try {
            if (reservation.getEtatReservation() != Reservation.ReservationStatus.EN_ATTENTE) {
                showAlert("Information", "Réservation déjà traitée");
                return;
            }

            reservationServices.updateReservationStatus(reservation.getId(), status);

            if (status == Reservation.ReservationStatus.CONFIRMEE) {
                Trip trip = tripServices.getOneById(reservation.getTripId());
                if (trip != null) {
                    int newSeats = trip.getAvailableSeats() - reservation.getNombrePlaces();
                    if (newSeats >= 0) {
                        trip.setAvailableSeats(newSeats);
                        tripServices.modifier(trip);
                    } else {
                        reservationServices.updateReservationStatus(reservation.getId(),
                                Reservation.ReservationStatus.EN_ATTENTE);
                        throw new Exception("Pas assez de places disponibles");
                    }
                }
            }

            showAlert("Succès", "Réservation mise à jour");
            loadAllReservations();
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    private void loadAllReservations() {
        reservationsTable.setItems(FXCollections.observableArrayList(
                reservationServices.getall()
        ));
    }

    @FXML
    private void handleBackToStatus() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/statut-reservation.fxml"));
            Stage stage = (Stage) reservationsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la vue");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}