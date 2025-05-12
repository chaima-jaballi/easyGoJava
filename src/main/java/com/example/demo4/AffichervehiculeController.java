/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.vehicule;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import com.example.demo4.services.vehiculeService;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class AffichervehiculeController implements Initializable {

    @FXML
    private GridPane gridev;

    vehiculeService ab=new vehiculeService();
    @FXML
    private TextField chercherevField;
    @FXML
    private Button ajouter;
    @FXML
    private Button mailButton;
    @FXML
    private ComboBox<String> triCritere;
    @FXML
    private ComboBox<String> triOrdre;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        affichervehicule();
               
    }    


    @FXML
    private void ajoutervehicule(ActionEvent ev) {
      try {
            //navigation
            Parent loader = FXMLLoader.load(getClass().getResource("ajoutervehicule.fxml"));
            chercherevField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void affichervehicule(){
         try {
            List<vehicule> vehicule = ab.recuperervehicule();
            gridev.getChildren().clear();
            int row = 0;
            int column = 0;
            for (int i = 0; i < vehicule.size(); i++) {
                //chargement dynamique d'une interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("vehicule.fxml"));
                AnchorPane pane = loader.load();
               
                //passage de parametres
                vehiculeController controller = loader.getController();
                controller.setvehicule(vehicule.get(i));
                controller.setIdev(vehicule.get(i).getId());
                gridev.add(pane, column, row);
                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }

            }
        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }

    @FXML
    private void recherchervehicule(KeyEvent ev) {
        try {
            List<vehicule> vehicule = ab.chercherev(chercherevField.getText());
            gridev.getChildren().clear();
            int row = 0;
            int column = 0;
            for (int i = 0; i < vehicule.size(); i++) {
                //chargement dynamique d'une interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("vehicule.fxml"));
                AnchorPane pane = loader.load();         
                //passage de parametres
                vehiculeController controller = loader.getController();
                controller.setvehicule(vehicule.get(i));
                controller.setIdev(vehicule.get(i).getId());
                gridev.add(pane, column, row);
                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }



    @FXML
    private void triervehicule(ActionEvent ev) throws SQLException {
        try {
            String colonne = triCritere.getValue(); // Récupère le critère (name ou prix)
            String ordre = triOrdre.getValue(); // Récupère l'ordre (ASC ou DESC)

            if (colonne == null) colonne = "name"; // Défaut si rien n'est sélectionné
            if (ordre == null) ordre = "ASC";

            List<vehicule> vehicule = ab.trierev(colonne, ordre);

            gridev.getChildren().clear();
            int row = 0;
            int column = 0;
            for (vehicule v : vehicule) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("vehicule.fxml"));
                AnchorPane pane = loader.load();
                vehiculeController controller = loader.getController();
                controller.setvehicule(v);
                controller.setIdev(v.getId());
                gridev.add(pane, column, row);
                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }



}
