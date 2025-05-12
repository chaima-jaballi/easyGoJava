/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.User;
import com.example.demo4.entities.offre;
import com.example.demo4.entities.vehicule;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import com.example.demo4.services.vehiculeService;
import com.example.demo4.services.offreService;
import org.controlsfx.control.Rating;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class vehiculeController implements Initializable {

    int idev;
    @FXML
    private Label nomevLabel;

    @FXML
    private Label descriptionevLabel;
    @FXML
    private Label updatedevLabel;
    @FXML
    private Label createdevLabel;
    @FXML
    private Button reserverevButton;

    
    User u=new User();
    offreService Ps=new offreService();
    @FXML
    private TextField idevF;
    @FXML
    private TextField iduserF;
    
    vehiculeService Ev=new vehiculeService();
    @FXML
    private ImageView imageview;

    @FXML
    private TextField idPartField;
    @FXML
    private Button annulerButton;
    @FXML
    private Rating rating;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        idevF.setVisible(false);





                

    }    
    private vehicule eve=new vehicule();
    
    public void setvehicule(vehicule e) {
        this.eve=e;
        nomevLabel.setText(e.getName());

        descriptionevLabel.setText(e.getContenu());
        updatedevLabel.setText(String.valueOf(e.getPrix()));
        createdevLabel.setText(String.valueOf(e.getCreated()));

        idevF.setText(String.valueOf(e.getId()));
        iduserF.setText(String.valueOf(1));
         String path = e.getImage();
         File file=new File(path);
         Image img = new Image(file.toURI().toString());
         imageview.setImage(img);

    }
    public void setIdev(int idev){
        this.idev=idev;
    }


    @FXML
    private void reserverev(MouseEvent ev) throws SQLException {
        double ratingValue = rating.getRating();

        LocalDate dateActuelle = LocalDate.now();
        Date dateSQL = Date.valueOf(dateActuelle);
        offre p=new offre(dateSQL,Integer.parseInt(iduserF.getText()),Integer.parseInt(idevF.getText()), ratingValue);
        
        Ps.ajouteroffre(p);

        idPartField.setText(String.valueOf(27));
        idPartField.setVisible(false);

       
        
        reserverevButton.setVisible(false);
        try {

            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("affichercontrat.fxml")));
            idPartField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }



        }
    



    
    
}
