package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Reservation;
import com.esprit.tn.pidev.entities.Reservation.ReservationStatus;
import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.ReservationServices;
import com.esprit.tn.pidev.services.ReservationStatisticsService;
import com.esprit.tn.pidev.services.TripServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminReservationController implements Initializable {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, String> departureColumn;
    @FXML private TableColumn<Reservation, String> destinationColumn;
    @FXML private TableColumn<Reservation, LocalDateTime> dateColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private TableColumn<Reservation, Double> priceColumn;
    @FXML private TableColumn<Reservation, Integer> placesColumn;
    @FXML private TableColumn<Reservation, Void> actionColumn;

    @FXML private TextField searchDepartureField;
    @FXML private TextField searchDestinationField;
    @FXML private ComboBox<String> statusFilter;

    @FXML private BarChart<String, Number> reservationsChart;
    @FXML private Button monthlyStatsBtn;
    @FXML private Button statusStatsBtn;
    @FXML private Button destinationsBtn;

    private final ReservationServices reservationService = new ReservationServices();
    private final ReservationStatisticsService statsService = new ReservationStatisticsService();
    private final TripServices tripService = new TripServices();
    private ObservableList<Reservation> reservationList;
    private Map<Integer, Trip> tripsCache = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllTrips();
        setupTableColumns();
        loadReservations();
        setupChart();
        styleTable();
    }

    private void loadAllTrips() {
        tripService.getall().forEach(trip -> tripsCache.put(trip.getId(), trip));
    }

    private void setupTableColumns() {
        departureColumn.setCellValueFactory(cellData -> {
            Trip trip = tripsCache.get(cellData.getValue().getTripId());
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    trip != null ? trip.getDeparturePoint() : "N/A");
        });

        destinationColumn.setCellValueFactory(cellData -> {
            Trip trip = tripsCache.get(cellData.getValue().getTripId());
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    trip != null ? trip.getDestination() : "N/A");
        });

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
        dateColumn.setCellFactory(column -> new TableCell<Reservation, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        statusColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getEtatReservation().toString()));

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        placesColumn.setCellValueFactory(new PropertyValueFactory<>("nombrePlaces"));

    }

    private void loadReservations() {
        reservationList = FXCollections.observableArrayList(reservationService.getall());

        FilteredList<Reservation> filteredData = new FilteredList<>(reservationList, p -> true);

        searchDepartureField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reservation -> {
                if (newValue == null || newValue.isEmpty()) return true;

                Trip trip = tripsCache.get(reservation.getTripId());
                String departure = trip != null ? trip.getDeparturePoint().toLowerCase() : "";
                return departure.contains(newValue.toLowerCase());
            });
        });

        searchDestinationField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reservation -> {
                if (newValue == null || newValue.isEmpty()) return true;

                Trip trip = tripsCache.get(reservation.getTripId());
                String destination = trip != null ? trip.getDestination().toLowerCase() : "";
                return destination.contains(newValue.toLowerCase());
            });
        });

        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reservation -> {
                if (newValue == null || newValue.equals("Tous")) return true;
                return reservation.getEtatReservation().toString().equals(newValue);
            });
        });

        SortedList<Reservation> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(reservationsTable.comparatorProperty());
        reservationsTable.setItems(sortedData);

        statusFilter.setItems(FXCollections.observableArrayList(
                "Tous",
                ReservationStatus.CONFIRMEE.toString(),
                ReservationStatus.EN_ATTENTE.toString(),
                ReservationStatus.REFUSEE.toString()
        ));
        statusFilter.getSelectionModel().selectFirst();
    }

    private void setupChart() {
        monthlyStatsBtn.setOnAction(e -> showMonthlyStats());
        statusStatsBtn.setOnAction(e -> showStatusStats());
        destinationsBtn.setOnAction(e -> showPopularDestinations());
        showMonthlyStats();
    }

    private void showMonthlyStats() {
        reservationsChart.getData().clear();

        XYChart.Series<String, Number> reservationSeries = new XYChart.Series<>();
        reservationSeries.setName("Nombre de réservations");

        Map<String, Long> monthlyStats = statsService.getMonthlyReservations();
        monthlyStats.forEach((month, count) ->
                reservationSeries.getData().add(new XYChart.Data<>(month, count))
        );

        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenus (DT)");

        Map<String, Double> revenueStats = statsService.getMonthlyRevenue();
        revenueStats.forEach((month, revenue) ->
                revenueSeries.getData().add(new XYChart.Data<>(month, revenue))
        );

        reservationsChart.getData().addAll(reservationSeries, revenueSeries);
        reservationsChart.setTitle("Statistiques Mensuelles - " + Year.now().getValue());
    }

    private void showStatusStats() {
        reservationsChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Statut des réservations");

        Map<String, Map<String, Number>> statusStats = statsService.getReservationsByStatusWithPercentage();
        statusStats.forEach((status, data) -> {
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(
                    String.format("%s (%.1f%%)", status, data.get("percentage")),
                    data.get("count")
            );
            series.getData().add(dataPoint);
        });

        reservationsChart.getData().add(series);
        reservationsChart.setTitle("Répartition par Statut");
    }

    private void showPopularDestinations() {
        reservationsChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Destinations populaires");

        Map<String, Long> destinations = statsService.getPopularDestinations(5);
        destinations.forEach((destination, count) ->
                series.getData().add(new XYChart.Data<>(destination, count))
        );

        reservationsChart.getData().add(series);
        reservationsChart.setTitle("Top 5 des Destinations");
    }

    private void showDetails(Reservation reservation) {
        Trip trip = tripsCache.get(reservation.getTripId());

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Détails de la réservation");
        info.setHeaderText("Détails de la réservation");
        info.setContentText(
                "Départ: " + (trip != null ? trip.getDeparturePoint() : "N/A") + "\n" +
                        "Destination: " + (trip != null ? trip.getDestination() : "N/A") + "\n" +
                        "Date: " + reservation.getDateReservation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                        "Statut: " + reservation.getEtatReservation() + "\n" +
                        "Prix: " + reservation.getMontantTotal() + " DT\n" +
                        "Places: " + reservation.getNombrePlaces()
        );
        info.showAndWait();
    }

    private void styleTable() {
        reservationsTable.lookupAll(".column-header").forEach(header ->
                header.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;")
        );

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

        statusColumn.setCellFactory(column -> new TableCell<Reservation, String>() {
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
                            setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-alignment: CENTER;");
                            break;
                        case "EN_ATTENTE":
                            setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold; -fx-alignment: CENTER;");
                            break;
                        case "REFUSEE":
                            setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-alignment: CENTER;");
                            break;
                        default:
                            setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });

        priceColumn.setStyle("-fx-alignment: CENTER_RIGHT;");
        placesColumn.setStyle("-fx-alignment: CENTER;");
    }
    @FXML
    private void navigateToAdminTrip() {
        try {
            // Charger la nouvelle vue
            Parent root = FXMLLoader.load(getClass().getResource("/admin-trip.fxml"));
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle
            Stage stage = (Stage) reservationsTable.getScene().getWindow();

            // Changer la scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur (peut-être afficher un message à l'utilisateur)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de charger la page de gestion des trajets.");
            alert.showAndWait();
        }
    }
}