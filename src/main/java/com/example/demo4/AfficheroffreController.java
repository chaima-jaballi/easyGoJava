/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.offre;
import com.example.demo4.entities.vehicule;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.example.demo4.services.offreService;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;



import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.web.WebView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import com.example.demo4.services.vehiculeService;
import javafx.scene.input.MouseEvent;


/**
 * FXML Controller class
 *
 * @author asus
 */
public class AfficheroffreController implements Initializable {
    // Label qui affiche le total

    private long totalAmount = 1000L; // Montant fictif (10,00 €)
    @FXML
    private Label totalPrixLabel;
    @FXML
    private TableView<offre> tableoffre;
    vehiculeService ab=new vehiculeService();
    @FXML
    private TableColumn<offre, Integer> iduserTv;
    @FXML
    private TableColumn<offre, Integer> idevTv;
    @FXML
    private TableColumn<offre, Date> datePartTv;
    @FXML
    private TableColumn<offre, String> descriptionevTv;
    @FXML
    private Label prixevLabel;
    @FXML
    private TextField descriptionevField;

    @FXML
    private TextField idread;
    @FXML
    private TextField iduserField;
    @FXML
    private TextField idevField;
    @FXML
    private DatePicker datepartField;
    @FXML
    private TextField chercherevField;
    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField expMonthField;

    @FXML
    private TextField expYearField;



    @FXML
    private WebView webView;



    @FXML
    private TextField cvcField;



    @FXML
    private Button payButton;
    offreService Ps=new offreService();
    @FXML
    private TextField datepartField1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        getOffre();
        afficherTotalPrix();



    }
    @FXML
    private void ajoutervehicule(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ajoutervehicule.fxml")));
            chercherevField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        afficherTotalPrix();
    }
    @FXML
    private void cal(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Calendrier.fxml")));
            chercherevField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        afficherTotalPrix();
    }
    @FXML
    private void stat(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Statistics.fxml")));
            chercherevField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        afficherTotalPrix();
    }
    @FXML
    private void recherchervehicule(KeyEvent ev) {
        try {
            List<vehicule> vehicule = ab.chercherev(chercherevField.getText());

            int row = 0;
            int column = 0;
            for (int i = 0; i < vehicule.size(); i++) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("vehicule.fxml"));
                AnchorPane pane = loader.load();

                vehiculeController controller = loader.getController();
                controller.setvehicule(vehicule.get(i));
                controller.setIdev(vehicule.get(i).getId());





            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


        afficherTotalPrix();
    }
    @FXML
    private void modifieroffre(ActionEvent ev) throws SQLException {

        offre pa = new offre();
        pa.setId(Integer.valueOf(idread.getText()));
        pa.setVehicules_id(Integer.valueOf(idevField.getText()));
        pa.setId_user(Integer.valueOf(iduserField.getText()));
        Date d=Date.valueOf(datepartField.getValue());
        pa.setCreated(d);
        pa.setContenu(descriptionevField.getText());


        Ps.modifieroffre(pa);
        resetPart();
        getComment();

        afficherTotalPrix();
    }

    @FXML
    private void reserverev(MouseEvent ev) throws SQLException {

        offre pa = new offre();
        pa.setId(Integer.valueOf(idread.getText()));
        pa.setVehicules_id(Integer.valueOf(idevField.getText()));
        pa.setId_user(Integer.valueOf(iduserField.getText()));
        Date d=Date.valueOf(datepartField.getValue());
        pa.setCreated(d);
        pa.setContenu(descriptionevField.getText());

        Ps.ajouterreserv(pa);
        resetPart();
        getComment();





        afficherTotalPrix();

    }

    @FXML
    private void supprimeroffre(ActionEvent ev) {
        offre p = tableoffre.getItems().get(tableoffre.getSelectionModel().getSelectedIndex());

        try {
            Ps.Deleteoffre(p);
        } catch (SQLException ex) {
            Logger.getLogger(AjoutervehiculeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("offre delete");
        alert.setContentText("offre deleted successfully!");
        alert.showAndWait();
        getComment();
        afficherTotalPrix();
    }

    @FXML
    private void choisiroffre(MouseEvent ev)  throws IOException {

        offre part = tableoffre.getItems().get(tableoffre.getSelectionModel().getSelectedIndex());

        idread.setText(String.valueOf(part.getId()));
        idevField.setText(String.valueOf(part.getVehicules_id()));
        iduserField.setText(String.valueOf(part.getId_user()));
        datepartField1.setText(String.valueOf(part.getCreated()));
        descriptionevField.setText(String.valueOf(part.getContenu()));

        afficherTotalPrix();
    }


    public void getOffre(){
        try {
            List<offre> part = Ps.recupererOffre();
            ObservableList<offre> olp = FXCollections.observableArrayList(part);
            tableoffre.setItems(olp);
            iduserTv.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            idevTv.setCellValueFactory(new PropertyValueFactory<>("vehicules_id"));
            datePartTv.setCellValueFactory(new PropertyValueFactory<>("created"));
            descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("contenu"));

            // Rendre le label invisible


            // Vérifier si le offre contient des éléments

        } catch (SQLException ex) {
            System.out.println("Erreur : " + ex.getMessage());
        }
    }

    public void getComment(){
        try {


            // TODO
            List<offre> part = Ps.recupererComment();
            ObservableList<offre> olp = FXCollections.observableArrayList(part);
            tableoffre.setItems(olp);
            iduserTv.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            idevTv.setCellValueFactory(new PropertyValueFactory<>("vehicules_id"));
            datePartTv.setCellValueFactory(new PropertyValueFactory<>("created"));
            descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("contenu"));
            // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
        afficherTotalPrix();
    }

    public void resetPart() {
        idread.setText("");
        idevField.setText("");
        iduserField.setText("");
        datepartField.setValue(null);
        afficherTotalPrix();
    }



    @FXML
    private void afficherTotalPrix() {
        try {
            double totalPrix = Ps.calculerTotalPrixOffre();
            totalPrixLabel.setText(String.format("%.2f €", totalPrix)); // Formatte le total en euros
        } catch (SQLException ex) {
            System.out.println("Erreur lors du calcul du total des prix: " + ex.getMessage());
        }
    }









    @FXML
    private void payment_nav(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PaymentForm.fxml")));
            totalPrixLabel.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }












}


