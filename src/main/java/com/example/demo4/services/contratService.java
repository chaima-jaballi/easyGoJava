package com.example.demo4.services;

import com.example.demo4.entities.Contrat;
import com.example.demo4.utils.MyDB;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class contratService implements IContratService<Contrat>{


    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public contratService() {

        cnx = MyDB.getInstance().getCnx();
    }



    @Override
    public Set<Contrat> afficher() throws SQLException {
        Set<Contrat> contrats = new HashSet<>();
        String requete = "SELECT * FROM contrat";

        try {
            ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {
                Contrat contrat = new Contrat(
                        rs.getInt("id"),
                        rs.getInt("idVehicule"),
                        rs.getString("nomprenom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("typeContrat"),
                        rs.getString("description"),
                        rs.getDate("dateDebut"),
                        rs.getDate("dateFin")
                );
                contrats.add(contrat);
            }

        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'affichage des contrats !");
            ex.printStackTrace();
        }

        return contrats;
    }


    @Override
    public void ajouter(Contrat contrat) {
        String requete = "INSERT INTO `contrat` (`idVehicule`, `nomprenom`, `adresse`, `telephone`, `typeContrat`, `description`, `dateDebut`, `dateFin`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, contrat.getIdVehicule());
            pst.setString(2, contrat.getNomprenom());
            pst.setString(3, contrat.getAdresse());
            pst.setString(4, contrat.getTelephone());
            pst.setString(5, contrat.getTypeContrat());
            pst.setString(6, contrat.getDescription());
            pst.setDate(7, new java.sql.Date(contrat.getDateDebut().getTime()));
            pst.setDate(8, new java.sql.Date(contrat.getDateFin().getTime()));

            pst.executeUpdate();
            System.out.println("Contrat ajouté avec succès !");

        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout du contrat !");
            ex.printStackTrace();
        }
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String requete = "DELETE FROM contrat WHERE id = ?";

        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Le contrat avec l'ID " + id + " a été supprimé avec succès.");
            } else {
                System.out.println("Aucun contrat trouvé avec l'ID " + id + ".");
            }

        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression du contrat !");
            ex.printStackTrace();
        }
    }


    @Override
    public void modifier(Contrat contrat) throws SQLException {
        String requete = "UPDATE contrat SET idVehicule = ?, nomprenom = ?, adresse = ?, telephone = ?, typeContrat = ?, description = ?, dateDebut = ?, dateFin = ? WHERE id = ?";

        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, contrat.getIdVehicule());
            pst.setString(2, contrat.getNomprenom());
            pst.setString(3, contrat.getAdresse());
            pst.setString(4, contrat.getTelephone());
            pst.setString(5, contrat.getTypeContrat());
            pst.setString(6, contrat.getDescription());
            pst.setDate(7, new java.sql.Date(contrat.getDateDebut().getTime()));
            pst.setDate(8, new java.sql.Date(contrat.getDateFin().getTime()));
            pst.setInt(9, contrat.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Le contrat avec l'ID " + contrat.getId() + " a été mis à jour avec succès.");
            } else {
                System.out.println("Aucun contrat trouvé avec l'ID " + contrat.getId() + ".");
            }

        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification du contrat !");
            ex.printStackTrace();
        }
    }



}
