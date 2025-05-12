/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4.test;

import java.util.Date;

import java.sql.SQLException;
import java.util.Calendar;

import com.example.demo4.entities.Contrat;
import com.example.demo4.services.contratService;
import com.example.demo4.services.vehiculeService;
import com.example.demo4.services.offreService;


/**
 *
 * @author asus
 */
public class test {
    
      public static void main(String[] args) throws SQLException {

          contratService cs = new contratService();

          // Obtenir la date actuelle
          Date dateDebut = new Date();

          // Calculer la date de fin (7 jours après la date de début)
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(dateDebut);
          calendar.add(Calendar.DAY_OF_MONTH, 7);
          Date dateFin = calendar.getTime();

          // Création d'un objet Contrat avec des données d'exemple
          Contrat cf = new Contrat(
                  46, // ID du véhicule
                  "Ahmed Ben Salah", // Nom et prénom
                  "12 Rue des Fleurs, Tunis", // Adresse
                  "98765432", // Numéro de téléphone
                  "Vente", // Type de contrat (Location/Vente/Autre)
                  "Contrat de vente du véhicule avec garanties", // Description
                  dateDebut, // Date de début du contrat
                  dateFin // Date de fin (une semaine après)
          );

          // Ajout du contrat à la base de données
          cs.ajouter(cf);







//cs.supprimer(1);

          System.out.println(cs.afficher().toString());



         /* Date d=Date.valueOf("2022-06-11");
          Date d1=Date.valueOf("2020-04-12");*/
   /*     try {
            //kifeh ya9ra el orde fel base de donnée , kifeh 3raf nom ev bch n3amarha f nom 

            
            


            offreService ps=new offreService();
            //ps.offre(p);
          //  ps.offre(p1);
           // ps.offre(p2);

            //ps.offre(p2);
            System.out.println("");
            vehiculeService ab = new vehiculeService();
            //ab.ajoutervehicule(e1);
            //ab.ajoutervehicule(e2);
           // ab.ajoutervehicule(e3);
            //ab.ajouter(p);
            //ab.modifiervehicule(e);
            //ab.supprimervehicule(e3);
            System.out.println(ab.recuperervehicule());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
*/
    }



    
}
