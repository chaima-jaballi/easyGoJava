/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4;
import java.util.Objects;
import com.example.demo4.entities.vehicule;
import com.example.demo4.services.categorieService;
import com.example.demo4.services.vehiculeService;
import com.example.demo4.services.offreService;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;















/**
 * FXML Controller class
 *
 * @author asus
 */
public class AjoutervehiculeController implements Initializable {

    @FXML
    private TextField descriptionevField;
    @FXML
    private DatePicker updatedevField;
    @FXML
    private TextField prixevField;
    @FXML
    private TextField total_en_stockevField;
    @FXML
    private DatePicker createdevField;
    @FXML
    private ComboBox<String> idevComboBox;
    @FXML
    private TextField imageevField;
    @FXML
    private TextField nameField;


  
    @FXML
    private TableView<vehicule> vehiculeTv;
    @FXML
    private TableColumn<vehicule, String> nomevTv;

    @FXML
    private TableColumn<vehicule, String> imageevTv;
    @FXML
    private TableColumn<vehicule, String> updatedevTv;
    @FXML
    private TableColumn<vehicule, String> createdevTv;
    @FXML
    private TableColumn<vehicule, String> descriptionevTv;
    @FXML
    private TableColumn<vehicule, String> prixevTv;
    @FXML
    private TableColumn<vehicule, String> total_en_stockevTv;
    @FXML
    private TableColumn<vehicule, String> categories_idevTv;



    

    vehiculeService Ev=new vehiculeService();
    offreService Pservice =new offreService();

    
    @FXML
    private TextField idmodifierField;

    @FXML
    private ImageView imageview;
    @FXML
    private TextField rechercher;
    @FXML
    private ImageView QrCode;

    /**
     * Initializes the controller class.
     */
@Override
public void initialize(URL url, ResourceBundle rb) {



    //idLabel.setText("");
    getevs();
    categorieService service = new categorieService();
    List<String> nameCategorieDons = service.getAllNameCategorieDon();
    ObservableList<String> options = FXCollections.observableArrayList(nameCategorieDons);
    idevComboBox.setItems(options);
}




    
  
     private boolean NoDate() {
         LocalDate currentDate = LocalDate.now();     
         LocalDate myDate = updatedevField.getValue();
         int comparisonResult = myDate.compareTo(currentDate);      
         boolean test = true;
        if (comparisonResult < 0) {

        test = true;
        } else if (comparisonResult > 0) {

         test = false;
        }
        return test;
    }
          @FXML
    private void ajoutervehicule(ActionEvent ev) {
    
         int part=0;
        if ((nameField.getText().length() == 0)  || (imageevField.getText().length() == 0) ||  (descriptionevField.getText().length() == 0) || (idevComboBox.getValue() == null) ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        }
       else if (NoDate() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("la date de updated  doit être aprés la date d'aujourd'hui");
            alert.showAndWait();
        }
       else{     

        vehicule e = new vehicule();

            // Récupérer la valeur sélectionnée dans le ComboBox
            String categoryName = idevComboBox.getValue();

            double pointDonValue = 0;
            if (categoryName.equals("cosmitique")) {
                pointDonValue = 15;
            } else if (categoryName.equals("fashion")) {
                pointDonValue = 30;
            }
            else if (categoryName.equals("sport")) {
                pointDonValue = 10;
            }
            else {
                pointDonValue = 3;
            }
            // Récupérer l'ID de la catégorie à partir de son nom
            categorieService categorieService = new categorieService();
            int categoryId = categorieService.getCategoryIDFromName(categoryName);
            e.setCategories_id(categoryId);
        e.setName(nameField.getText());
        e.setPrix(Integer.  parseInt(prixevField.getText()));
        e.setTotal_en_stock(Integer.parseInt(total_en_stockevField.getText()));

        e.setContenu(descriptionevField.getText());
        java.util.Date date_debut=java.util.Date.from(updatedevField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date sqlDate = new Date(date_debut.getTime());
        e.setUpdated(sqlDate);
                //

                java.util.Date date_created=java.util.Date.from(createdevField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

                Date sqlDate1 = new Date(date_created.getTime());
                e.setCreated(sqlDate1);


        //lel image
        e.setImage(imageevField.getText());      
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information ");
            alert.setHeaderText("vehicule add");
            alert.setContentText("vehicule added successfully!");
            alert.showAndWait();      
        try {
            Ev.ajoutervehicule(e);
            reset();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }      
        getevs();
        

    }}
    

    private void reset() {
        nameField.setText("");

        descriptionevField.setText("");
        imageevField.setText("");


        updatedevField.setValue(null);
        createdevField.setValue(null);
        prixevField.setText("");
        total_en_stockevField.setText("");
    }
    
   public void getevs() {  
         try {
            // TODO
            List<vehicule> vehicule = Ev.recuperervehicule();
            ObservableList<vehicule> olp = FXCollections.observableArrayList(vehicule);
            vehiculeTv.setItems(olp);
            nomevTv.setCellValueFactory(new PropertyValueFactory<>("name"));

            imageevTv.setCellValueFactory(new PropertyValueFactory<>("image"));
             updatedevTv.setCellValueFactory(new PropertyValueFactory<>("updated"));
             createdevTv.setCellValueFactory(new PropertyValueFactory<>("created"));
            descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("contenu"));
            prixevTv.setCellValueFactory(new PropertyValueFactory<>("prix"));
            total_en_stockevTv.setCellValueFactory(new PropertyValueFactory<>("total_en_stock"));
             categories_idevTv.setCellValueFactory(new PropertyValueFactory<>("Categorie"));



        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }

     
     @FXML
   private void modifiervehicule(ActionEvent ev) throws SQLException {
        vehicule e = new vehicule();
        e.setId(Integer.parseInt(idmodifierField.getText()));
        e.setName(nameField.getText());
        e.setPrix(Integer.parseInt(prixevField.getText()));
        e.setTotal_en_stock(Integer.parseInt(total_en_stockevField.getText()));

        e.setContenu(descriptionevField.getText()); 
        Date d=Date.valueOf(updatedevField.getValue());
         Date c=Date.valueOf(createdevField.getValue());
        e.setUpdated(d);
         e.setCreated(c);
        e.setImage(imageevField.getText());


         Ev.modifiervehicule(e);
        reset();
        getevs();         
    }

    @FXML
    private void supprimervehicule(ActionEvent ev) {
           vehicule e = vehiculeTv.getItems().get(vehiculeTv.getSelectionModel().getSelectedIndex());
        try {
            Ev.supprimervehicule(e);
        } catch (SQLException ex) {
            Logger.getLogger(AjoutervehiculeController.class.getName()).log(Level.SEVERE, null, ex);
        }   
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("vehicule delete");
        alert.setContentText("vehicule deleted successfully!");
        alert.showAndWait();        
        getevs();    
    }

    @FXML
    private void affichervehicule(ActionEvent ev) {
         try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("affichervehicule.fxml")));
             nameField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

  
    @FXML

    private void choisirev(MouseEvent ev) throws IOException {
        vehicule e = vehiculeTv.getItems().get(vehiculeTv.getSelectionModel().getSelectedIndex());

        idmodifierField.setText(String.valueOf(e.getId()));
        nameField.setText(e.getName());
        prixevField.setText(String.valueOf(e.getPrix()));
        total_en_stockevField.setText(String.valueOf(e.getTotal_en_stock()));

        imageevField.setText(e.getImage());
        descriptionevField.setText(e.getContenu());



        //lel image
        String path = e.getImage();
               File file=new File(path);
              Image img = new Image(file.toURI().toString());
                imageview.setImage(img);
                
        //////////////      
        String filename = Ev.GenerateQrev(e);
        System.out.println("filename lenaaa " + filename);
        String path1="C:\\xampp\\htdocs\\xchangex\\imgQr\\qrcode"+filename;
        File file1=new File(path1);
        Image img1 = new Image(file1.toURI().toString());
        //Image image = new Image(getClass().getResourceAsStream("src/utils/img/" + filename));
        QrCode.setImage(img1);
            
    }



    @FXML
    private void afficheroffres(ActionEvent ev) {
         try {

            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("afficheroffre.fxml")));
             nameField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void affichercontrats(ActionEvent ev) {
        try {

            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("affichercontrat.fxml")));
            nameField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    private void uploadImage(ActionEvent ev)throws FileNotFoundException, IOException  {

        Random rand = new Random();
        int x = rand.nextInt(1000);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        String DBPath = "C:\\\\xampp\\\\htdocs\\\\imageP\\\\"  + x + ".jpg";
        if (file != null) {
            FileInputStream Fsource = new FileInputStream(file.getAbsolutePath());
            FileOutputStream Fdestination = new FileOutputStream(DBPath);
            BufferedInputStream bin = new BufferedInputStream(Fsource);
            BufferedOutputStream bou = new BufferedOutputStream(Fdestination);
            System.out.println(file.getAbsoluteFile());
            String path=file.getAbsolutePath();
            Image img = new Image(file.toURI().toString());
            imageview.setImage(img);    
            imageevField.setText(DBPath);
            int b = 0;
            while (b != -1) {
                b = bin.read();
                bou.write(b);
            }
            bin.close();
            bou.close();          
        } else {
            System.out.println("error");
        }
    }


    

    @FXML
    private void rechercherev(KeyEvent ev) {
        
        vehiculeService bs=new vehiculeService();
        vehicule b= new vehicule();
        ObservableList<vehicule>filter= bs.chercherev(rechercher.getText());
        populateTable(filter);
    }
     private void populateTable(ObservableList<vehicule> branlist){
       vehiculeTv.setItems(branlist);
   
       }






    @FXML
    private void excelabonn(ActionEvent abonn) {

        try {
            String filename = "C:\\xampp\\htdocs\\fichierExcelJava\\dataArticle.xls";
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");

            // Création de la ligne de titres
            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("Nom de vehicule");
            rowhead.createCell((short) 1).setCellValue("contenu");


            // Récupération des données depuis la base de données
            List<vehicule> vehicules = Ev.recuperervehicule();

            // Ajout des données dans le fichier Excel
            for (int i = 0; i < vehicules.size(); i++) {
                HSSFRow row = sheet.createRow((short) i + 1);
                row.createCell((short) 0).setCellValue(vehicules.get(i).getName());
                row.createCell((short) 1).setCellValue(vehicules.get(i).getContenu());

            }

            // Écriture des données dans le fichier Excel
            FileOutputStream fileOut = new FileOutputStream(filename);
            hwb.write(fileOut);
            fileOut.close();

            System.out.println("Your excel file has been generated!");

            // Ouvrir le fichier Excel
            File file = new File(filename);
            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


    @FXML
    private void pdfabonn(ActionEvent abonn) throws FileNotFoundException, SQLException, IOException {
        // vehicule tab_Recselected = vehiculeTv.getSelectionModel().getSelectedItem();
        long millis = System.currentTimeMillis();
        java.sql.Date DateRapport = new java.sql.Date(millis);

        String DateLyoum = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(DateRapport);//yyyyMMddHHmmss
        System.out.println("Date d'aujourdhui : " + DateLyoum);

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(String.valueOf(DateLyoum + ".pdf")));//yyyy-MM-dd
            document.open();
            Paragraph ph1 = new Paragraph("Voici un rapport détaillé de notre application qui contient tous les vehicules . Pour chaque vehicule, nous fournissons des informations telles que la date d'Aujourd'hui :" + DateRapport );
            Paragraph ph2 = new Paragraph(".");
            PdfPTable table = new PdfPTable(4);
            //On créer l'objet cellule.
            PdfPCell cell;
            //contenu du tableau.
            table.addCell("Nom");
            table.addCell("contenu");
            table.addCell("Date ");
            table.addCell("image");

            vehicule r = new vehicule();
            Ev.recuperervehicule().forEach(new Consumer<vehicule>() {
                @Override
                public void accept(vehicule e) {
                    table.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(String.valueOf(e.getName()));
                    table.addCell(String.valueOf(e.getContenu()));
                    table.addCell(String.valueOf(e.getUpdated()));
                    try {
                        // Créer un objet Image à partir de l'image
                        String path = e.getImage();
                        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(path);

                        // Définir la taille de l'image dans le tableau
                        img.scaleToFit(100, 100); // Définir la largeur et la hauteur de l'image

                        // Ajouter l'image à la cellule du tableau
                        PdfPCell cell = new PdfPCell(img);
                        table.addCell(cell);
                    } catch (Exception ex) {
                        table.addCell("Erreur lors du chargement de l'image");
                    }
                }
            });
            document.add(ph1);
            document.add(ph2);
            document.add(table);
        } catch (Exception e) {
            System.out.println(e);
        }
        document.close();

        ///Open FilePdf
        File file = new File(DateLyoum + ".pdf");
        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) //checks file exists or not  
        {
            desktop.open(file); //opens the specified file   
        }
    }
    }


    





    

