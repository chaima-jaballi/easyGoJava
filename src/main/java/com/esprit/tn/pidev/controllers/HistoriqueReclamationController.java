package com.esprit.tn.pidev.controllers;

import com.esprit.tn.pidev.entities.HistoriqueReclamation;
import com.esprit.tn.pidev.services.HistoriqueReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HistoriqueReclamationController {

    @FXML private TextField ticketIdField;
    @FXML private TextArea messageTextArea;
    @FXML private TextField dateActionField;
    @FXML private TextField userIdField;
    @FXML private Button envoyerButton;
    @FXML private Label erreurTicketIdLabel;
    @FXML private Label erreurMessageLabel;
    @FXML private Label erreurDateLabel;
    @FXML private Label erreurUserIdLabel;

    private final HistoriqueReclamationService historiqueService = new HistoriqueReclamationService();

    @FXML
    public void initialize() {
        // Associer l'action du bouton à la méthode envoyerHistorique
        envoyerButton.setOnAction(event -> envoyerHistorique());
    }

    private void envoyerHistorique() {
        // Réinitialiser les messages d'erreur
        erreurTicketIdLabel.setText("");
        erreurMessageLabel.setText("");
        erreurDateLabel.setText("");
        erreurUserIdLabel.setText("");

        // Récupérer les valeurs des champs
        String ticketIdText = ticketIdField.getText().trim();
        String message = messageTextArea.getText().trim();
        String dateText = dateActionField.getText().trim();
        String userIdText = userIdField.getText().trim();

        // Validation des champs
        boolean valide = true;

        // Validation de l'ID du ticket
        int ticketId = 0;
        if (ticketIdText.isEmpty()) {
            erreurTicketIdLabel.setText("L'ID du ticket est obligatoire.");
            valide = false;
        } else {
            try {
                ticketId = Integer.parseInt(ticketIdText);
                if (ticketId <= 0) {
                    erreurTicketIdLabel.setText("L'ID du ticket doit être un nombre positif.");
                    valide = false;
                }
            } catch (NumberFormatException e) {
                erreurTicketIdLabel.setText("L'ID du ticket doit être un nombre valide.");
                valide = false;
            }
        }

        // Validation du message
        if (message.isEmpty()) {
            erreurMessageLabel.setText("Le message ne peut pas être vide.");
            valide = false;
        } else if (message.length() > 500) {
            erreurMessageLabel.setText("Le message ne doit pas dépasser 500 caractères.");
            valide = false;
        }

        // Validation de la date d'action
        Timestamp dateAction = null;
        if (dateText.isEmpty()) {
            erreurDateLabel.setText("La date d'action est obligatoire.");
            valide = false;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateText, formatter);
                dateAction = Timestamp.valueOf(localDateTime);
            } catch (DateTimeParseException e) {
                erreurDateLabel.setText("Le format de la date est invalide. Utilisez le format YYYY-MM-DD HH:MM:SS.");
                valide = false;
            }
        }

        // Validation de l'ID de l'utilisateur
        int userId = 0;
        if (userIdText.isEmpty()) {
            erreurUserIdLabel.setText("L'ID de l'utilisateur est obligatoire.");
            valide = false;
        } else {
            try {
                userId = Integer.parseInt(userIdText);
                if (userId <= 0) {
                    erreurUserIdLabel.setText("L'ID de l'utilisateur doit être un nombre positif.");
                    valide = false;
                }
            } catch (NumberFormatException e) {
                erreurUserIdLabel.setText("L'ID de l'utilisateur doit être un nombre valide.");
                valide = false;
            }
        }

        // Si la saisie est invalide, arrêter l'exécution
        if (!valide) {
            return;
        }

        // Créer un nouvel objet HistoriqueReclamation
        HistoriqueReclamation historique = new HistoriqueReclamation(0, ticketId, message, dateAction, userId);

        // Envoyer l'historique
        try {
            historiqueService.ajouter(historique);
            afficherAlerte("Succès", "Historique ajouté avec succès !");
            reinitialiserChamps();
        } catch (Exception e) {
            afficherAlerte("Erreur", "Une erreur s'est produite lors de l'ajout de l'historique : " + e.getMessage());
        }
    }

    private void reinitialiserChamps() {
        // Réinitialiser les champs après l'envoi
        ticketIdField.clear();
        messageTextArea.clear();
        dateActionField.clear();
        userIdField.clear();
    }

    private void afficherAlerte(String titre, String message) {
        // Afficher une boîte de dialogue d'alerte
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}