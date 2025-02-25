package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.HistoriqueReclamation;
import com.esprit.tn.pidev.services.HistoriqueReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;

public class HistoriqueReclamationController {

    // Références aux éléments de l'interface
    @FXML private TableView<HistoriqueReclamation> tableHistorique;
    @FXML private TableColumn<HistoriqueReclamation, Integer> colId;
    @FXML private TableColumn<HistoriqueReclamation, Integer> colTicketId;
    @FXML private TableColumn<HistoriqueReclamation, String> colMessage;
    @FXML private TableColumn<HistoriqueReclamation, Timestamp> colDateAction;
    @FXML private TableColumn<HistoriqueReclamation, Integer> colUserId;



    // Service pour interagir avec la base de données
    private final HistoriqueReclamationService historiqueService = new HistoriqueReclamationService();

    // Méthode d'initialisation appelée automatiquement par JavaFX
    @FXML
    public void initialize() {
        // Initialisation des colonnes de la TableView
        colId.setCellValueFactory(new PropertyValueFactory<>("id")); // Lier à la propriété "id"
        colTicketId.setCellValueFactory(new PropertyValueFactory<>("ticketId")); // Lier à la propriété "ticketId"
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message")); // Lier à la propriété "message"
        colDateAction.setCellValueFactory(new PropertyValueFactory<>("dateAction")); // Lier à la propriété "dateAction"
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId")); // Lier à la propriété "userId"

        // Charger les données au démarrage
        refreshTable();
    }

    // Méthode pour rafraîchir les données de la TableView
    public void refreshTable() {
        // Récupérer toutes les entrées de l'historique depuis le service
        ObservableList<HistoriqueReclamation> observableList = FXCollections.observableArrayList(historiqueService.getall());

        // Afficher les données dans la TableView
        tableHistorique.setItems(observableList);

        // Debug : afficher le nombre d'entrées chargées
        System.out.println("Données chargées : " + observableList.size() + " entrées");
    }

    // Méthode pour gérer le clic sur le bouton "Rafraîchir"
    @FXML
    private void handleRefreshButton() {
        System.out.println("Bouton Rafraîchir cliqué !"); // Debug : vérifier que la méthode est appelée
        refreshTable(); // Recharger les données
    }}

