module com.esprit.tn.pidev.pidev {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;
    requires org.json;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires stripe.java;
    requires google.maps.services;
    requires org.apache.httpcomponents.httpclient;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires java.base;
    requires org.apache.httpcomponents.httpcore;
    requires okhttp3;
    requires com.google.gson;
    requires java.desktop; // Pour l'utilisation de BufferedImage et ImageIO
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires javafx.swing;
    requires java.mail; // Module pour JavaMail
    requires java.logging; // Module pour les logs (optionnel, mais utile pour le débogage)



    opens com.esprit.tn.pidev.pidev to javafx.fxml;
    exports com.esprit.tn.pidev;
    opens com.esprit.tn.pidev.entities to javafx.base;
    exports com.esprit.tn.pidev.controllers to javafx.fxml;
    opens com.esprit.tn.pidev.controllers to javafx.fxml;

    opens com.esprit.tn.pidev.main to javafx.graphics;
    exports com.esprit.tn.pidev.main;
}