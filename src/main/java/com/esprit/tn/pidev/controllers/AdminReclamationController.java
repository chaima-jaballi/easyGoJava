package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.TicketReclamation;
import com.esprit.tn.pidev.services.SmsService;
import com.esprit.tn.pidev.services.TicketReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class AdminReclamationController implements Initializable {
    @FXML private Button btnGoToAddReclamation;
    @FXML private TableView<TicketReclamation> tableView;
    @FXML private TableColumn<TicketReclamation, Integer> colId;
    @FXML private TableColumn<TicketReclamation, Integer> colUserId;
    @FXML private TableColumn<TicketReclamation, String> colCategorie;
    @FXML private TableColumn<TicketReclamation, String> colStatut;
    @FXML private TableColumn<TicketReclamation, String> colDescription;
    @FXML private TableColumn<TicketReclamation, Timestamp> colDateCreation;
    @FXML private TableColumn<TicketReclamation, Void> colActions;
    @FXML private Button btnRetour;
    @FXML private Button btnStats;

    private final TicketReclamationService service = new TicketReclamationService();
    private final SmsService smsService = new SmsService();
    private ObservableList<TicketReclamation> reclamations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reclamations = FXCollections.observableArrayList(service.getall());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDateCreation.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button validerButton = new Button("Valider");
            private final Button annulerButton = new Button("Annuler");
            private final HBox pane = new HBox(10, validerButton, annulerButton);

            {
                validerButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                annulerButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                validerButton.setOnAction(event -> {
                    TicketReclamation reclamation = getTableView().getItems().get(getIndex());
                    reclamation.setStatut("traité");
                    service.modifier(reclamation);
                    tableView.refresh();

                    String userPhoneNumber = service.getUserPhoneNumberById(reclamation.getUserId());
                    if (userPhoneNumber != null && !userPhoneNumber.trim().isEmpty()) {
                        try {
                            smsService.sendSms(userPhoneNumber, "Votre réclamation #" + reclamation.getId() + " a été traitée.");
                        } catch (Exception e) {
                            showAlert("Erreur SMS", "Échec de l'envoi du SMS : " + e.getMessage());
                        }
                    }
                });

                annulerButton.setOnAction(event -> {
                    TicketReclamation reclamation = getTableView().getItems().get(getIndex());
                    reclamation.setStatut("non traité");
                    service.modifier(reclamation);
                    tableView.refresh();

                    String userPhoneNumber = service.getUserPhoneNumberById(reclamation.getUserId());
                    if (userPhoneNumber != null && !userPhoneNumber.trim().isEmpty()) {
                        try {
                            smsService.sendSms(userPhoneNumber, "Votre réclamation #" + reclamation.getId() + " n'a pas été traitée.");
                        } catch (Exception e) {
                            showAlert("Erreur SMS", "Échec de l'envoi du SMS : " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tableView.setItems(reclamations);
    }

    @FXML
    public void showStats() {
        // Calculer le nombre total de réclamations
        double totalReclamations = reclamations.size();
        if (totalReclamations == 0) {
            showAlert("Information", "Aucune réclamation disponible pour les statistiques.");
            return;
        }

        // Compter les réclamations traitées et non traitées
        long treatedCount = reclamations.stream().filter(r -> "traité".equals(r.getStatut())).count();
        long untreatedCount = reclamations.stream().filter(r -> "non traité".equals(r.getStatut())).count();

        // Calculer les pourcentages
        double treatedPercentage = (treatedCount / totalReclamations) * 100;
        double untreatedPercentage = (untreatedCount / totalReclamations) * 100;

        // Formater les pourcentages avec 2 décimales
        DecimalFormat df = new DecimalFormat("#.##");
        String treatedLabel = "Traitées (" + df.format(treatedPercentage) + "%)";
        String untreatedLabel = "Non Traitées (" + df.format(untreatedPercentage) + "%)";

        // Créer les axes du BarChart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Statut");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre de réclamations");

        // Créer le BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Statistiques des Réclamations");

        // Ajouter les données avec pourcentages dans les étiquettes
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Réclamations");
        series.getData().add(new XYChart.Data<>(treatedLabel, treatedCount));
        series.getData().add(new XYChart.Data<>(untreatedLabel, untreatedCount));
        barChart.getData().add(series);

        // Styliser les barres avec les nouvelles couleurs
        barChart.setBarGap(10); // Espacement entre les barres
        barChart.lookupAll(".chart-bar").forEach(node -> {
            if (node.getParent().getChildrenUnmodifiable().indexOf(node) == 0) {
                node.setStyle("-fx-bar-fill: #28a745;"); // Bleu pour traité (première barre)
            } else if (node.getParent().getChildrenUnmodifiable().indexOf(node) == 1) {
                node.setStyle("-fx-bar-fill: #dc3545;"); // Jaune pour non traité (deuxième barre)
            }
        });

        // Ajouter une légende ou des informations supplémentaires
        Label statsLabel = new Label("Total : " + (int)totalReclamations + " réclamations\n" +
                "Traitées : " + treatedCount + " (" + df.format(treatedPercentage) + "%)\n" +
                "Non Traitées : " + untreatedCount + " (" + df.format(untreatedPercentage) + "%)");
        statsLabel.setStyle("-fx-font-size: 14px;");

        // Afficher dans une nouvelle fenêtre
        Stage statsStage = new Stage();
        statsStage.setTitle("Statistiques des Réclamations");
        VBox vbox = new VBox(10, barChart, statsLabel);
        Scene scene = new Scene(vbox, 400, 350);
        statsStage.setScene(scene);
        statsStage.show();
    }

    @FXML
    private void retour() {
        // Votre logique existante pour btnRetour
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}