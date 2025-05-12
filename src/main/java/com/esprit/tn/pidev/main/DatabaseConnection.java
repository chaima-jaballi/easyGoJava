package com.esprit.tn.pidev.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {




    private static DatabaseConnection instance;
    private Connection cnx;
    private DatabaseConnection(){

        String Url="jdbc:mysql://localhost/pidev1";
        String Username="root";
        String Password="";

        try {
            cnx= DriverManager.getConnection(Url,Username,Password);
            System.out.println("Connextion etablie");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la connexion à la base de données",e);
        }
    }

    public static DatabaseConnection getInstance() {
        if(instance==null){
            instance=  new DatabaseConnection();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}