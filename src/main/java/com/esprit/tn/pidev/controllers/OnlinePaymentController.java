package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.Paiement;
import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.services.EmailService;
import com.esprit.tn.pidev.services.PaiementServices;
import com.esprit.tn.pidev.services.TripServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;

public class OnlinePaymentController implements Initializable {

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField cardHolderField;

    @FXML
    private TextField monthField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField cvvField;

    @FXML
    private TextField montantField;

    @FXML
    private TextField emailField;

    private Paiement paiement;
    private int paymentId;
    private double montant;

    public void setMontant(double montant) {
        this.montant = montant;
        if (montantField != null) {
            montantField.setText(String.valueOf(montant));
        }
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
        System.out.println("Payment ID reçu : " + paymentId);
    }

    public void setPaymentDetails(Paiement paiement) {
        this.paiement = paiement;
    }

    TripServices servicestrip = new TripServices();

    @FXML
    private void handlePayment() {
        try {
            String cardNumber = cardNumberField.getText().trim();
            String cardHolder = cardHolderField.getText().trim();
            String month = monthField.getText().trim();
            String year = yearField.getText().trim();
            String cvv = cvvField.getText().trim();
            String montantText = montantField.getText().trim();
            String email = emailField.getText().trim();

            if (!validateCardNumber(cardNumber)) {
                showAlert("Numéro de carte invalide", "Le numéro de carte doit contenir exactement 16 chiffres.");
                return;
            }

            if (!validateCardHolder(cardHolder)) {
                showAlert("Nom du titulaire invalide", "Le nom du titulaire ne doit contenir que des lettres et des espaces.");
                return;
            }

            if (!validateExpirationDate(month, year)) {
                showAlert("Date d'expiration invalide", "Le mois doit être entre 1 et 12, et l'année doit être supérieure ou égale à l'année en cours.");
                return;
            }

            if (!validateCVV(cvv)) {
                showAlert("CVV invalide", "Le CVV doit contenir exactement 3 chiffres.");
                return;
            }

            if (!validateMontant(montantText)) {
                showAlert("Montant invalide", "Le montant doit être un nombre positif.");
                return;
            }

            if (!validateEmail(email)) {
                showAlert("Email invalide", "Veuillez entrer une adresse email valide.");
                return;
            }

            double montant = Double.parseDouble(montantText);
            String expirationDate = String.format("%02d/%02d", Integer.parseInt(month), Integer.parseInt(year.substring(2)));

            PaiementServices paiementServices = new PaiementServices();
            paiement = paiementServices.getDernierPaiement();
            System.out.println("paymentId : " + paymentId);



            paiement.setNumeroCarte(cardNumber);
            paiement.setNomTitulaire(cardHolder);
            paiement.setDateExpiration(expirationDate);
            paiement.setCvv(cvv);
            paiement.setMontant(montant);


            paiementServices.modifier(paiement);

            // Envoyer l'email de confirmation
            sendPaymentConfirmationEmail(email, paiement);
            System.out.println("montant : " + montant);

            System.out.println("Paiement effectué avec succès !");
            cardNumberField.getScene().getWindow().hide();
            navigateToConfirmationPage();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors du traitement du paiement : " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        cardNumberField.getScene().getWindow().hide();
    }

    private boolean validateCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{16}");
    }

    private boolean validateCardHolder(String cardHolder) {
        return cardHolder.matches("[a-zA-Z\\s]+");
    }

    private boolean validateExpirationDate(String month, String year) {
        try {
            int monthValue = Integer.parseInt(month);
            int currentYear = Year.now().getValue();
            int yearValue = Integer.parseInt(year);
            return monthValue >= 1 && monthValue <= 12 && yearValue >= currentYear;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateCVV(String cvv) {
        return cvv.matches("\\d{3}");
    }

    private boolean validateMontant(String montant) {
        try {
            double value = Double.parseDouble(montant);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToConfirmationPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/confirmation-trip.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cardNumberField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de confirmation.");
        }
    }

    private void sendPaymentConfirmationEmail(String email, Paiement paiement) {
        try {
            Trip trip = servicestrip.getLastTrip();
            if (trip == null) {
                showAlert("Erreur", "Aucun voyage trouvé.");
                return;
            }

            String subject = "Confirmation de Paiement et Détails du Voyage";
            String body = String.format(
                    "<html>" +
                            "<head>" +
                            "<style>" +
                            "body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; }" +
                            ".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                            "h1 { color: #4CAF50; text-align: center; }" +
                            "h2 { color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }" +
                            "p { line-height: 1.6; }" +
                            ".details { margin-bottom: 20px; }" +
                            ".details strong { color: #4CAF50; }" +
                            ".footer { text-align: center; margin-top: 20px; font-size: 0.9em; color: #777; }" +
                            "</style>" +
                            "</head>" +
                            "<body>" +
                            "<div class='container'>" +
                            "<h1>Confirmation de Paiement</h1>" +
                            "<p>Bonjour,</p>" +
                            "<p>Votre paiement a été effectué avec succès. Voici les détails de votre transaction et de votre voyage :</p>" +

                            "<div class='details'>" +
                            "<h2>Détails du Paiement</h2>" +
                            "<p><strong>Numéro de carte :</strong> %s</p>" +
                            "<p><strong>Nom du titulaire :</strong> %s</p>" +
                            "<p><strong>Date d'expiration :</strong> %s</p>" +
                            "<p><strong>Montant :</strong> %s DT</p>" +
                            "</div>" +

                            "<div class='details'>" +
                            "<h2>Détails du Voyage</h2>" +
                            "<p><strong>Date de réservation :</strong> %s</p>" +
                            "<p><strong>Point de départ :</strong> %s</p>" +
                            "<p><strong>Destination :</strong> %s</p>" +
                            "<p><strong>Heure de départ :</strong> %s</p>" +
                            "<p><strong>Heure de retour :</strong> %s</p>" +
                            "<p><strong>Places disponibles :</strong> %d</p>" +
                            "<p><strong>Montant du voyage :</strong> %s DT</p>" +
                            "</div>" +

                            "<p>Merci pour votre confiance. Nous vous souhaitons un excellent voyage !</p>" +
                            "<div class='footer'>" +
                            "<p>Cordialement,<br>Votre équipe de support.</p>" +
                            "</div>" +
                            "</div>" +
                            "</body>" +
                            "</html>",
                    paiement.getNumeroCarte(), paiement.getNomTitulaire(), paiement.getDateExpiration(), paiement.getMontant(),
                    trip.getTripDate(), trip.getDeparturePoint(), trip.getDestination(),
                    trip.getDepartureTime(), trip.getReturnTime(), trip.getAvailableSeats(), trip.getContribution()
            );

            EmailService.sendHtmlEmail(email, subject, body);
            System.out.println("Email de confirmation envoyé à " + email);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'envoyer l'email de confirmation.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (montantField != null) {
            montantField.setText(String.valueOf(montant));
        }
    }
}