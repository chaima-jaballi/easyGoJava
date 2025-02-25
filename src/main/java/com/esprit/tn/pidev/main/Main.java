package com.esprit.tn.pidev.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/addReclamation.fxml"));
            // Create the scene
            Scene scene = new Scene(root);

            // Set up the stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("EasyGo, EasyLife");
            primaryStage.show();
        } catch (Exception e) {
            // Print the stack trace for debugging
            e.printStackTrace();
            System.err.println("Failed to load FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}