/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo4;
import com.example.demo4.entities.offre;
import com.example.demo4.services.offreService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StatisticsController implements Initializable {

    public Button sensibilisationPage_btn1;
    @FXML
    private ImageView GoBackBtn;
    @FXML
    private PieChart StatsChart;

    offreService rs = new offreService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            displayStatistics();
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void cal(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("afficheroffre.fxml")));
            StatsChart.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
    public void displayStatistics() throws SQLException {
        List<offre> offres = rs.recupererComment();

        // Regrouper les offres par nom et compter le nombre de offres pour chaque nom
        Map<String, Long> offresParnom = offres.stream()
                .collect(Collectors.groupingBy(offre::getContenu, Collectors.counting()));

        // Créer une liste de offrenées pour le PieChart
        List<PieChart.Data> pieChartData = offresParnom.entrySet().stream()
                .map(entry -> new PieChart.Data(entry.getKey() + " (" + entry.getValue() + " offre(s))", entry.getValue()))
                .collect(Collectors.toList());

        // Afficher les offrenées dans le PieChart
        StatsChart.setData(FXCollections.observableArrayList(pieChartData));

        // Ajouter des fonctionnalités d'interactivité pour afficher des informations supplémentaires lors du clic sur les offrenées
        StatsChart.getData().forEach(data -> {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                String nom = data.getName().split(" ")[0];
                long nombreoffres = offresParnom.get(nom);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Statistiques par nom");
                alert.setHeaderText("nom : " + nom);
                alert.setContentText("Nombre de offres ajoutés : " + nombreoffres);
                alert.showAndWait();
            });
        });
    }
    @FXML
    private void afficheroffre(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
            StatsChart.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}