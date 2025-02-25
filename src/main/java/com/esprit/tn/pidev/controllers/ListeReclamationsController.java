package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.TicketReclamation;
import com.esprit.tn.pidev.services.TicketReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ListeReclamationsController {

    @FXML
    private TableView<TicketReclamation> tableView;
    @FXML
    private TableColumn<TicketReclamation, Integer> colId;
    @FXML
    private TableColumn<TicketReclamation, String> colCategorie;
    @FXML
    private TableColumn<TicketReclamation, String> colStatut;
    @FXML
    private TableColumn<TicketReclamation, String> colDescription;
    @FXML
    private TableColumn<TicketReclamation, String> colDateCreation;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> triComboBox;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnEvaluer;
    @FXML
    private Button btnRetour;

    private TicketReclamationService service = new TicketReclamationService();
    private ObservableList<TicketReclamation> reclamationsList;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatut()));
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        colDateCreation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateCreation().toString()));

        chargerDonnees();
        triComboBox.getItems().addAll("Date de création", "Statut", "Catégorie");
        triComboBox.setValue("Date de création");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> rechercherReclamation());
    }

    private void chargerDonnees() {
        List<TicketReclamation> liste = service.getall();
        reclamationsList = FXCollections.observableArrayList(liste);
        tableView.setItems(reclamationsList);
    }

    @FXML
    private void rechercherReclamation() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            tableView.setItems(reclamationsList);
        } else {
            ObservableList<TicketReclamation> filteredList = reclamationsList.filtered(reclamation ->
                    reclamation.getCategorie().toLowerCase().contains(keyword) ||
                            reclamation.getDescription().toLowerCase().contains(keyword) ||
                            reclamation.getStatut().toLowerCase().contains(keyword) ||
                            reclamation.getDateCreation().toString().toLowerCase().contains(keyword)
            );
            tableView.setItems(filteredList);
        }
    }

    @FXML
    private void trierReclamations() {
        String critere = triComboBox.getValue();
        switch (critere) {
            case "Date de création":
                reclamationsList.sort(Comparator.comparing(TicketReclamation::getDateCreation));
                break;
            case "Statut":
                reclamationsList.sort(Comparator.comparing(TicketReclamation::getStatut));
                break;
            case "Catégorie":
                reclamationsList.sort(Comparator.comparing(TicketReclamation::getCategorie));
                break;
        }
        tableView.setItems(reclamationsList);
    }

    @FXML
    private void supprimerReclamation() {
        TicketReclamation selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.supprimer(selected);
            reclamationsList.remove(selected);
            showAlert("Succès", "Réclamation supprimée avec succès !", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Veuillez sélectionner une réclamation à supprimer.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void ajouterReclamation() {
        ouvrirFenetre("/addReclamation.fxml", "Ajouter une Réclamation");
    }

    @FXML
    private void modifierReclamation() {
        TicketReclamation selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modificationReclamation.fxml"));
                Parent root = loader.load();

                ModifierReclamationController controller = loader.getController();
                controller.initData(selected);

                Stage stage = new Stage();
                stage.setTitle("Modifier Réclamation");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une réclamation à modifier.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void evaluerReclamation() {
        TicketReclamation selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/feedback.fxml"));
                Parent root = loader.load();

                // Utilisation de setTicketId au lieu de initData
                FeedbackController controller = loader.getController();
                controller.setTicketId(selected.getId());  // Correction ici

                Stage stage = new Stage();
                stage.setTitle("Évaluer la Réclamation");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir l'interface d'évaluation.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une réclamation à évaluer.", Alert.AlertType.WARNING);
        }
    }


    @FXML
    private void retourVersHistorique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/historiqueReclamation.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Historique des Réclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'interface historiqueReclamation.fxml");
        }
    }

    private void ouvrirFenetre(String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titre);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
