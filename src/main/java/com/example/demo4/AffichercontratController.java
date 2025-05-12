package com.example.demo4;

import com.example.demo4.entities.Contrat;
import com.example.demo4.services.contratService;
import com.itextpdf.text.BaseColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AffichercontratController {

    // Liaison avec les éléments FXML
    @FXML
    private TableView<Contrat> tableContrats;
    @FXML
    private TableColumn<Contrat, Integer> idContratCol, idVehiculeCol;
    @FXML
    private TableColumn<Contrat, String> nomPrenomCol, adresseCol, telephoneCol, typeContratCol, descriptionCol;
    @FXML
    private TableColumn<Contrat, String> dateDebutCol, dateFinCol;

    @FXML
    private TextField idVehiculeField, nomPrenomField, adresseField, telephoneField, typeContratField, descriptionField;
    @FXML
    private DatePicker dateDebutField, dateFinField;
    @FXML
    private Button ajouterContratBtn, modifierContratBtn, supprimerContratBtn, rechercherContratBtn;
    @FXML
    private TextField rechercheField;

    @FXML
    private Button genererPDFBtn;
    private contratService service = new contratService();

    @FXML
    private ImageView qrCodeImageView;


    // Initialisation de la table
    @FXML
    public void initialize() {
        // Lier les colonnes aux attributs de Contrat
        idContratCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idVehiculeCol.setCellValueFactory(new PropertyValueFactory<>("idVehicule"));
        nomPrenomCol.setCellValueFactory(new PropertyValueFactory<>("nomprenom"));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        typeContratCol.setCellValueFactory(new PropertyValueFactory<>("typeContrat"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDebutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

        tableContrats.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                remplirChamps();
            }
        });


        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> rechercherContrat());
        genererPDFBtn.setOnAction(event -> genererPDF());


        afficherContrats(); // Charger les contrats au démarrage
    }

    public void afficherContrats() {
        try {
            Set<Contrat> contrats = service.afficher();
            ObservableList<Contrat> contratList = FXCollections.observableArrayList(contrats);
            tableContrats.setItems(contratList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ajouterContrat() {
        Contrat contrat = new Contrat(
                Integer.parseInt(idVehiculeField.getText()),
                nomPrenomField.getText(),
                adresseField.getText(),
                telephoneField.getText(),
                typeContratField.getText(),
                descriptionField.getText(),
                java.sql.Date.valueOf(dateDebutField.getValue()),
                java.sql.Date.valueOf(dateFinField.getValue())
        );

        service.ajouter(contrat);
        afficherContrats();
    }

    @FXML
    public void supprimerContrat() {
        Contrat selected = tableContrats.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                service.supprimer(selected.getId());
                afficherContrats();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void modifierContrat() {
        Contrat selected = tableContrats.getSelectionModel().getSelectedItem();

        if (selected != null) {
            try {
                // Mettre à jour les valeurs du contrat sélectionné
                selected.setIdVehicule(Integer.parseInt(idVehiculeField.getText()));
                selected.setNomprenom(nomPrenomField.getText());
                selected.setAdresse(adresseField.getText());
                selected.setTelephone(telephoneField.getText());
                selected.setTypeContrat(typeContratField.getText());
                selected.setDescription(descriptionField.getText());
                selected.setDateDebut(java.sql.Date.valueOf(dateDebutField.getValue()));
                selected.setDateFin(java.sql.Date.valueOf(dateFinField.getValue()));

                // Appel de la fonction de modification dans le service
                service.modifier(selected);

                // Rafraîchir l'affichage après modification
                afficherContrats();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez sélectionner un contrat à modifier !");
        }
    }

    @FXML
    public void remplirChamps() {
        Contrat selected = tableContrats.getSelectionModel().getSelectedItem();

        if (selected != null) {
            idVehiculeField.setText(String.valueOf(selected.getIdVehicule()));
            nomPrenomField.setText(selected.getNomprenom());
            adresseField.setText(selected.getAdresse());
            telephoneField.setText(selected.getTelephone());
            typeContratField.setText(selected.getTypeContrat());
            descriptionField.setText(selected.getDescription());



            // ✅ Générer le QR Code à chaque sélection
            genererQRCode(selected);
        }
    }



    @FXML
    public void rechercherContrat() {
        String recherche = rechercheField.getText().trim().toLowerCase();

        if (recherche.isEmpty()) {
            afficherContrats(); // Si la recherche est vide, on affiche tous les contrats
            return;
        }

        try {
            Set<Contrat> contrats = service.afficher();
            ObservableList<Contrat> contratsFiltres = FXCollections.observableArrayList();

            for (Contrat c : contrats) {
                boolean correspond =
                        (c.getNomprenom() != null && c.getNomprenom().toLowerCase().contains(recherche)) ||
                                (c.getTelephone() != null && c.getTelephone().contains(recherche)) ||
                                (c.getTypeContrat() != null && c.getTypeContrat().toLowerCase().contains(recherche)) ||
                                (c.getDateDebut() != null && c.getDateDebut().toString().contains(recherche)) ||
                                (c.getDateFin() != null && c.getDateFin().toString().contains(recherche));

                if (correspond) {
                    contratsFiltres.add(c);
                }
            }

            tableContrats.setItems(contratsFiltres);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void genererPDF() {
        Document document = new Document();
        try {
            String cheminFichier = "Contrats.pdf"; // Nom du fichier
            PdfWriter.getInstance(document, new FileOutputStream(cheminFichier));
            document.open();

            // 🔹 Ajouter un titre centré et stylisé
            Paragraph titre = new Paragraph("Liste des Contrats");
            titre.setAlignment(Paragraph.ALIGN_CENTER);
            titre.setSpacingAfter(20);
            document.add(titre);

            // 🔹 Création du tableau avec colonnes bien dimensionnées
            PdfPTable table = new PdfPTable(8); // 8 colonnes
            table.setWidthPercentage(100); // Largeur totale de la page
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{1, 2, 3, 3, 2, 2, 3, 3}); // Ajustement des colonnes

            // 🔹 Style pour les en-têtes (gras et centrés)
            PdfPCell cell;
            Font fontHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            String[] headers = {"ID", "ID Véhicule", "Nom & Prénom", "Adresse", "Téléphone", "Type Contrat", "Description", "Dates"};
            for (String header : headers) {
                cell = new PdfPCell(new Paragraph(header, fontHeader));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(8);
                cell.setBackgroundColor(new BaseColor(200, 200, 200)); // Gris clair
                table.addCell(cell);
            }

            // 🔹 Remplir le tableau avec les contrats
            for (Contrat c : tableContrats.getItems()) {
                table.addCell(String.valueOf(c.getId()));
                table.addCell(String.valueOf(c.getIdVehicule()));
                table.addCell(c.getNomprenom());
                table.addCell(c.getAdresse());
                table.addCell(c.getTelephone());
                table.addCell(c.getTypeContrat());
                table.addCell(c.getDescription());
                table.addCell(c.getDateDebut() + " → " + c.getDateFin());
            }

            document.add(table);
            document.close();

            // ✅ Ouvrir automatiquement le PDF
            File pdfFile = new File(cheminFichier);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }



    private void genererQRCode(Contrat contrat) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String data = "Contrat ID: " + contrat.getId() + "\n" +
                    "Véhicule ID: " + contrat.getIdVehicule() + "\n" +
                    "Nom: " + contrat.getNomprenom() + "\n" +
                    "Téléphone: " + contrat.getTelephone() + "\n" +
                    "Date Début: " + contrat.getDateDebut() + "\n" +
                    "Date Fin: " + contrat.getDateFin();

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200, hints);

            BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }

            // Convertir l'image en ImageView JavaFX
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            qrCodeImageView.setImage(image);

            // Sauvegarder le QR Code en image (optionnel)
            ImageIO.write(bufferedImage, "png", new File("QRCode.png"));

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void ajoutervehicule(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ajoutervehicule.fxml")));
            nomPrenomField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }



}
