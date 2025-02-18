package com.esprit.tn.pidev.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection cnx;

    private DatabaseConnection() {
        try {
            cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidev", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection(); // Initialisation ici
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
