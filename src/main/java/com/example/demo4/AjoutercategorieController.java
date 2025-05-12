/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temcategoriee file, choose Tools | Temcategoriees
 * and open the temcategoriee in the editor.
 */
package com.example.demo4;


import com.example.demo4.entities.categorie;
import com.example.demo4.entities.vehicule;
import com.example.demo4.services.categorieService;
import com.example.demo4.services.vehiculeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * FXML Controller class
 *
 * @author asus
 */
public class AjoutercategorieController implements Initializable {


    @FXML
    private TextField nameField;



  
    @FXML
    private TableView<categorie> categorieTv;
    @FXML
    private TableColumn<categorie, String> nomevTv;







    vehiculeService Evv=new vehiculeService();

 
    ObservableList<categorie> evs;
    categorieService Ev=new categorieService();

    @FXML
    private TableView<vehicule> vehiculeTv;
    @FXML
    private TableColumn<vehicule, String> nomevTvv;
    @FXML
    private TableColumn<vehicule, String> typeevTv;
    @FXML
    private TableColumn<vehicule, String> imageevTv;
    @FXML
    private TableColumn<vehicule, String> dateevTv;
    @FXML
    private TableColumn<vehicule, String> descriptionevTv;
    @FXML
    private TableColumn<vehicule, Integer> quantite_vehiculeTv;

    @FXML
    private TableColumn<vehicule, Integer> idevTv;
    
    @FXML
    private TextField idmodifierField;

    @FXML
    private ImageView imageview;
    @FXML
    private TextField rechercher;



    /**
     * Initializes the controller class.
     */
@Override
public void initialize(URL url, ResourceBundle rb) {



    //idLabel.setText("");
    getevs();

}




    
  

          @FXML
    private void ajoutercategorie(ActionEvent ev) {
    
         int part=0;
        if ((nameField.getText().length() == 0)  ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        }

       else{     

        categorie e = new categorie();


        e.setName(nameField.getText());








        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information ");
            alert.setHeaderText("categorie add");
            alert.setContentText("categorie added successfully!");
            alert.showAndWait();      
        try {
            Ev.ajoutercategorie(e);
            reset();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }      
        getevs();

        

    }}
    
    //fin d ajout d'un categorie
    private void reset() {
        nameField.setText("");




    }
    
   public void getevs() {  
         try {
            // TODO
            List<categorie> categorie = Ev.recuperercategorie();
            ObservableList<categorie> olp = FXCollections.observableArrayList(categorie);
            categorieTv.setItems(olp);
            nomevTv.setCellValueFactory(new PropertyValueFactory<>("name"));





           // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }//get evs

     
     @FXML
   private void modifiercategorie(ActionEvent ev) throws SQLException {
        categorie e = new categorie();
        e.setId(Integer.parseInt(idmodifierField.getText()));
        e.setName(nameField.getText());




         Ev.modifiercategorie(e);
        reset();
        getevs();

    }

    @FXML
    private void supprimercategorie(ActionEvent ev) {
           categorie e = categorieTv.getItems().get(categorieTv.getSelectionModel().getSelectedIndex());
        try {
            Ev.supprimercategorie(e);
        } catch (SQLException ex) {
            Logger.getLogger(AjoutercategorieController.class.getName()).log(Level.SEVERE, null, ex);
        }   
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("categorie delete");
        alert.setContentText("categorie deleted successfully!");
        alert.showAndWait();        
        getevs();

    }



  
    @FXML
    //ta3 tablee bch nenzel 3ala wehed ya5tarou w yet3abew textfield
    private void choisirev(MouseEvent ev) throws IOException {
        categorie e = categorieTv.getItems().get(categorieTv.getSelectionModel().getSelectedIndex());
        //idLabel.setText(String.valueOf(e.getid()));
        idmodifierField.setText(String.valueOf(e.getId()));
        nameField.setText(e.getName());







            
    }








    @FXML
    private void rechercherev(KeyEvent ev) {
        
        categorieService bs=new categorieService(); 
        categorie b= new categorie();
        ObservableList<categorie>filter= bs.chercherev(rechercher.getText());
        populateTable(filter);
    }
     private void populateTable(ObservableList<categorie> branlist){
       categorieTv.setItems(branlist);
   
       }
    @FXML
    private void navigation(ActionEvent abonn) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(getClass().getResource("DashCategoriesvehicule.fxml"));
            categorieTv.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
  


    }


    





    

