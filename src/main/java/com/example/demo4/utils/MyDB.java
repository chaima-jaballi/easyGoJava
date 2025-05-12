/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author sbekr
 */
public class MyDB {
    String url = "jdbc:mysql://localhost:3306/chaima_javafx";
    String username = "root";
    String password = "";
    Connection cnx;
    
    
    private static MyDB instance;

    private MyDB() {
        try {
            cnx = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion établie");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static MyDB getInstance() {
        if (instance == null) {
            instance = new MyDB();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }

    
}
