package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.Trip;
import com.esprit.tn.pidev.main.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripServices implements Iservice<Trip> {

    private final Connection cnx;
    private Trip currentTrip;

    public TripServices() {
        this.cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Trip trip) {
        String req = "INSERT INTO trip (trip_date, departure_point, destination, "
                + "departure_time, return_time, available_seats, contribution, "
                + "trip_type, relay_points) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            stm.setTimestamp(1, trip.getTripDate());
            stm.setString(2, trip.getDeparturePoint());
            stm.setString(3, trip.getDestination());
            stm.setTimestamp(4, trip.getDepartureTime());
            stm.setTimestamp(5, trip.getReturnTime());
            stm.setInt(6, trip.getAvailableSeats());
            stm.setDouble(7, trip.getContribution());
            stm.setString(8, trip.getTripType());
            stm.setString(9, trip.getRelayPoints());

            int affectedRows = stm.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("L'ajout du voyage a échoué, aucune ligne affectée");
            }

            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trip.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du voyage: " + e.getMessage(), e);
        }
    }

    @Override
    public void modifier(Trip trip) {
        if (trip.getId() <= 0) {
            throw new IllegalArgumentException("ID de voyage invalide");
        }

        String req = "UPDATE trip SET "
                + "trip_date = ?, "
                + "departure_point = ?, "
                + "destination = ?, "
                + "departure_time = ?, "
                + "return_time = ?, "
                + "available_seats = ?, "
                + "contribution = ?, "
                + "trip_type = ?, "
                + "relay_points = ? "
                + "WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setTimestamp(1, trip.getTripDate());
            stm.setString(2, trip.getDeparturePoint());
            stm.setString(3, trip.getDestination());
            stm.setTimestamp(4, trip.getDepartureTime());
            stm.setTimestamp(5, trip.getReturnTime());
            stm.setInt(6, trip.getAvailableSeats());
            stm.setDouble(7, trip.getContribution());
            stm.setString(8, trip.getTripType());
            stm.setString(9, trip.getRelayPoints());
            stm.setInt(10, trip.getId());

            int rowsUpdated = stm.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("Aucun voyage trouvé avec l'ID: " + trip.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du voyage: " + e.getMessage(), e);
        }
    }

    @Override
    public void supprimer(Trip trip) {
        if (trip.getId() <= 0) {
            throw new IllegalArgumentException("ID de voyage invalide");
        }

        String req = "DELETE FROM trip WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, trip.getId());

            int rowsDeleted = stm.executeUpdate();

            if (rowsDeleted == 0) {
                throw new RuntimeException("Aucun voyage trouvé avec l'ID: " + trip.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du voyage: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Trip> getall() {
        List<Trip> trips = new ArrayList<>();
        String req = "SELECT * FROM trip ORDER BY trip_date DESC";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Trip trip = mapResultSetToTrip(rs);
                trips.add(trip);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des voyages: " + e.getMessage(), e);
        }

        return trips;
    }

    @Override
    public Trip getOneById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de voyage invalide");
        }

        String req = "SELECT * FROM trip WHERE id = ?";
        Trip trip = null;

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    trip = mapResultSetToTrip(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du voyage: " + e.getMessage(), e);
        }

        return trip;
    }

    @Override
    public Trip getone() {
        String req = "SELECT * FROM trip ORDER BY id DESC LIMIT 1";
        Trip trip = null;

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            if (rs.next()) {
                trip = mapResultSetToTrip(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du dernier voyage: " + e.getMessage(), e);
        }

        return trip;
    }

    public Trip getLastTrip() {
        return currentTrip != null ? currentTrip : getone();
    }

    public void setCurrentTrip(Trip trip) {
        this.currentTrip = trip;
    }

    private Trip mapResultSetToTrip(ResultSet rs) throws SQLException {
        Trip trip = new Trip();
        trip.setId(rs.getInt("id"));
        trip.setTripDate(rs.getTimestamp("trip_date"));
        trip.setDeparturePoint(rs.getString("departure_point"));
        trip.setDestination(rs.getString("destination"));
        trip.setDepartureTime(rs.getTimestamp("departure_time"));
        trip.setReturnTime(rs.getTimestamp("return_time"));
        trip.setAvailableSeats(rs.getInt("available_seats"));
        trip.setContribution(rs.getDouble("contribution"));
        trip.setTripType(rs.getString("trip_type"));
        trip.setRelayPoints(rs.getString("relay_points"));
        return trip;
    }

    public void updateTripStatus(int tripId, String status) {
        if (tripId <= 0) {
            throw new IllegalArgumentException("ID de voyage invalide");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Le statut ne peut pas être vide");
        }

        String req = "UPDATE trip SET status = ? WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, status);
            stm.setInt(2, tripId);

            int rowsUpdated = stm.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("Aucun voyage trouvé avec l'ID: " + tripId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du statut: " + e.getMessage(), e);
        }
    }
}