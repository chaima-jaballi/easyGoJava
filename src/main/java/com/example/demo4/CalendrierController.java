package com.example.demo4;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.example.demo4.entities.offre;
import com.example.demo4.services.offreService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CalendrierController implements Initializable {

    @FXML
    private CalendarView calendarView;

    private offreService offreService = new offreService();



    @FXML
    private void cal(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("afficheroffre.fxml")));
            calendarView.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Créer un calendrier pour afficher les offres
        Calendar calendar = new Calendar("Offres");

        // Récupérer les offres depuis la base de données
        try {
            List<offre> offres = offreService.recupererComment();
            for (offre o : offres) {
                // Convertir la date SQL en LocalDate
                LocalDate date = o.getCreated().toLocalDate();

                // Créer une entrée pour chaque offre
                Entry<String> entry = new Entry<>(o.getContenu());
                entry.setInterval(date, LocalTime.of(9, 0), date, LocalTime.of(17, 0)); // Heures fictives
                calendar.addEntry(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ajouter le calendrier à une source de calendrier
        CalendarSource calendarSource = new CalendarSource("Offres");
        calendarSource.getCalendars().add(calendar);

        // Ajouter la source de calendrier à la vue
        calendarView.getCalendarSources().add(calendarSource);
    }
}