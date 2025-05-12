
package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.Driver;
import com.esprit.tn.pidev.main.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverServices {

    private final Connection cnx;

    public DriverServices() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Récupérer tous les drivers disponibles
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT * FROM driver";
        try (PreparedStatement stm = cnx.prepareStatement(query); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                drivers.add(new Driver(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("vehicle_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Afficher l'erreur SQL
            throw new RuntimeException("Erreur lors de la récupération des drivers", e);
        }
        return drivers;
    }
}
