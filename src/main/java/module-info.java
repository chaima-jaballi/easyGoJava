module com.esprit.tn.pidev {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jdk.jfr;
    requires com.fasterxml.jackson.annotation;
    requires java.desktop;

    opens com.esprit.tn.pidev to javafx.fxml;
    exports com.esprit.tn.pidev;
    opens com.esprit.tn.pidev.entities to javafx.base;
    exports com.esprit.tn.pidev.controllers to javafx.fxml;
    opens com.esprit.tn.pidev.controllers to javafx.fxml;

    opens com.esprit.tn.pidev.main to javafx.graphics;
    exports com.esprit.tn.pidev.main;




}