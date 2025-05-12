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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientReservationsController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, String> departureColumn;
    @FXML private TableColumn<Reservation, String> destinationColumn;
    @FXML private TableColumn<Reservation, String> dateColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private TableColumn<Reservation, Double> priceColumn;
    @FXML private TableColumn<Reservation, String> detailsColumn;
    @FXML private TableColumn<Reservation, Void> actionColumn;

    private final ReservationServices reservationServices = new ReservationServices();
    private final TripServices tripServices = new TripServices();
    private Map<Integer, Trip> tripsCache = new HashMap<>();

    @FXML
    public void initialize() {
        // Précharger tous les trajets pour optimiser les performances
        loadAllTrips();
        configureTableColumns();
        styleTable();
        setupActionColumn();
        loadReservations();
    }

    private void loadAllTrips() {
        List<Trip> allTrips = tripServices.getall();
        for (Trip trip : allTrips) {
            tripsCache.put(trip.getId(), trip);
        }
    }

    private void configureTableColumns() {
        // Colonne Départ
        departureColumn.setCellValueFactory(cellData -> {
            Trip trip = tripsCache.get(cellData.getValue().getTripId());
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    trip != null ? trip.getDeparturePoint() : "N/A");
        });

        // Colonne Destination
        destinationColumn.setCellValueFactory(cellData -> {
            Trip trip = tripsCache.get(cellData.getValue().getTripId());
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    trip != null ? trip.getDestination() : "N/A");
        });

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));

        statusColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getEtatReservation().toString()));

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));

        detailsColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        String.format("%d places - %s",
                                cellData.getValue().getNombrePlaces(),
                                cellData.getValue().getLieuEscale())));
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button payButton = new Button("Payer");

            {
                payButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
                payButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    handlePayment(reservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    if (reservation.getEtatReservation().equals(Reservation.ReservationStatus.CONFIRMEE)) {
                        setGraphic(payButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void styleTable() {
        // Style des en-têtes de colonnes
        reservationsTable.lookupAll(".column-header").forEach(node ->
                node.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;")
        );

        // Style des lignes
        reservationsTable.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            row.setStyle("-fx-cell-size: 40px;");

            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    if (row.getIndex() % 2 == 1) {
                        row.setStyle("-fx-background-color: #f8f9fa; -fx-cell-size: 40px;");
                    } else {
                        row.setStyle("-fx-background-color: white; -fx-cell-size: 40px;");
                    }
                }
            });

            return row;
        });

        // Style des cellules de statut
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "CONFIRMEE":
                            setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                            break;
                        case "EN_ATTENTE":
                            setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                            break;
                        case "REFUSEE":
                        case "ANNULEE":
                            setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
    }

    private void loadReservations() {
        reservationsTable.setItems(FXCollections.observableArrayList(
                reservationServices.getall()
        ));
    }

    private void handlePayment(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paiement.fxml"));
            Parent root = loader.load();

            PaiementController paiementController = loader.getController();

            Stage stage = (Stage) reservationsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Paiement");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la page de paiement", e.getMessage());
        }
    }

    @FXML
    private void handleModifyReservation() {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une réservation à modifier", "");
            return;
        }

        if (!selected.getEtatReservation().equals(Reservation.ReservationStatus.EN_ATTENTE)) {
            showAlert("Modification impossible", "Seules les réservations en attente peuvent être modifiées", "");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modify_reservation.fxml"));
            Parent root = loader.load();

            ModifyReservationController controller = loader.getController();
            controller.setReservation(selected);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier Réservation");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadReservations();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface de modification", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteReservation() {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une réservation à supprimer", "");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer la réservation #" + selected.getId());
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                reservationServices.supprimer(selected);
                showAlert("Succès", "Réservation supprimée", "La réservation a été supprimée avec succès.");
                loadReservations();
            } catch (Exception e) {
                showAlert("Erreur", "Échec de suppression", e.getMessage());
            }
        }
    }

    @FXML
    private void handleBackToHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/client_home.fxml"));
            Stage stage = (Stage) reservationsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil Client");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner à l'accueil", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}