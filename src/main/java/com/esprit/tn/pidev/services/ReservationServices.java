package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.Reservation;
import com.esprit.tn.pidev.main.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationServices implements Iservice<Reservation> {

    private final Connection cnx;

    public ReservationServices() {
        this.cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Reservation reservation) {
        String req = "INSERT INTO reservation (trip_id, date_reservation, etat_reservation, "
                + "montant_total, lieu_escale, nombre_places, type_handicap) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            stm.setInt(1, reservation.getTripId());
            stm.setTimestamp(2, Timestamp.valueOf(reservation.getDateReservation()));
            stm.setString(3, reservation.getEtatReservation().name());
            stm.setDouble(4, reservation.getMontantTotal());
            stm.setString(5, reservation.getLieuEscale());
            stm.setInt(6, reservation.getNombrePlaces());
            stm.setString(7, reservation.getTypeHandicap());

            int affectedRows = stm.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("L'ajout de la réservation a échoué");
            }

            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la réservation", e);
        }
    }

    @Override
    public void modifier(Reservation reservation) {
        String req = "UPDATE reservation SET trip_id = ?, date_reservation = ?, "
                + "etat_reservation = ?, montant_total = ?, lieu_escale = ?, "
                + "nombre_places = ?, type_handicap = ? WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, reservation.getTripId());
            stm.setTimestamp(2, Timestamp.valueOf(reservation.getDateReservation()));
            stm.setString(3, reservation.getEtatReservation().name());
            stm.setDouble(4, reservation.getMontantTotal());
            stm.setString(5, reservation.getLieuEscale());
            stm.setInt(6, reservation.getNombrePlaces());
            stm.setString(7, reservation.getTypeHandicap());
            stm.setInt(8, reservation.getId());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification", e);
        }
    }

    public void updateReservationStatus(int reservationId, Reservation.ReservationStatus newStatus) {
        String req = "UPDATE reservation SET etat_reservation = ? WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, newStatus.name());
            stm.setInt(2, reservationId);

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucune réservation trouvée avec l'ID: " + reservationId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du statut", e);
        }
    }

    @Override
    public void supprimer(Reservation reservation) {
        String req = "DELETE FROM reservation WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, reservation.getId());

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucune réservation trouvée avec l'ID: " + reservation.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }

    @Override
    public List<Reservation> getall() {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation ORDER BY date_reservation DESC";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des réservations", e);
        }
        return reservations;
    }

    public List<Reservation> getByStatus(Reservation.ReservationStatus status) {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation WHERE etat_reservation = ? ORDER BY date_reservation DESC";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, status.name());

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération par statut", e);
        }
        return reservations;
    }

    @Override
    public Reservation getOneById(Integer id) {
        String req = "SELECT * FROM reservation WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération par ID", e);
        }
        return null;
    }

    @Override
    public Reservation getone() {
        String req = "SELECT * FROM reservation ORDER BY id DESC LIMIT 1";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la dernière réservation", e);
        }
        return null;
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("id"));
        reservation.setTripId(rs.getInt("trip_id"));
        reservation.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
        reservation.setEtatReservation(Reservation.ReservationStatus.valueOf(rs.getString("etat_reservation")));
        reservation.setMontantTotal(rs.getDouble("montant_total"));
        reservation.setLieuEscale(rs.getString("lieu_escale"));
        reservation.setNombrePlaces(rs.getInt("nombre_places"));
        reservation.setTypeHandicap(rs.getString("type_handicap"));
        return reservation;
    }
}