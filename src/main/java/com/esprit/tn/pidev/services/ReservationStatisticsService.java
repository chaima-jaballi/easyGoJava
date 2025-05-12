package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.Reservation;
import com.esprit.tn.pidev.entities.Reservation.ReservationStatus;
import com.esprit.tn.pidev.entities.Trip;

import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationStatisticsService {

    private final ReservationServices reservationService;
    private final TripServices tripService;

    public ReservationStatisticsService() {
        this.reservationService = new ReservationServices();
        this.tripService = new TripServices();
    }

    public Map<String, Long> getMonthlyReservations() {
        List<Reservation> reservations = reservationService.getall();
        int currentYear = Year.now().getValue();

        return reservations.stream()
                .filter(res -> res.getDateReservation().getYear() == currentYear)
                .collect(Collectors.groupingBy(
                        res -> {
                            Month month = res.getDateReservation().getMonth();
                            return String.format("%s %d", getMonthName(month), currentYear);
                        },
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    public Map<String, Map<String, Number>> getReservationsByStatusWithPercentage() {
        List<Reservation> reservations = reservationService.getall();
        long total = reservations.size();

        Map<String, Map<String, Number>> result = new LinkedHashMap<>();

        reservations.stream()
                .collect(Collectors.groupingBy(
                        res -> res.getEtatReservation().toString(),
                        Collectors.counting()
                ))
                .forEach((status, count) -> {
                    Map<String, Number> stats = new HashMap<>();
                    stats.put("count", count);
                    stats.put("percentage", total > 0 ? Math.round((count * 100.0 / total) * 10) / 10.0 : 0);
                    result.put(status, stats);
                });

        return result;
    }

    public Map<String, Long> getPopularDestinations(int limit) {
        List<Reservation> reservations = reservationService.getall();
        List<Trip> trips = tripService.getall();

        Map<Integer, String> tripDestinations = trips.stream()
                .collect(Collectors.toMap(Trip::getId, Trip::getDestination));

        return reservations.stream()
                .collect(Collectors.groupingBy(
                        res -> tripDestinations.getOrDefault(res.getTripId(), "Inconnue"),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Double> getMonthlyRevenue() {
        List<Reservation> reservations = reservationService.getall();
        int currentYear = Year.now().getValue();

        return reservations.stream()
                .filter(res -> res.getDateReservation().getYear() == currentYear)
                .collect(Collectors.groupingBy(
                        res -> String.format("%s %d", getMonthName(res.getDateReservation().getMonth()), currentYear),
                        TreeMap::new,
                        Collectors.summingDouble(Reservation::getMontantTotal)
                ));
    }

    private String getMonthName(Month month) {
        String[] monthNames = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        return monthNames[month.getValue() - 1];
    }
}