package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.HistoriqueReclamation;
import com.esprit.tn.pidev.main.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueReclamationService implements Iservice<HistoriqueReclamation> {

    Connection cnx;

    public HistoriqueReclamationService () {
        cnx = DatabaseConnection.instance.getCnx();
    }

    @Override
    public void ajouter(HistoriqueReclamation hr) {
        String req = "INSERT INTO historiquereclamation (ticket_id, message, dateAction, user_id) VALUES (?, ?, ?,?)";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, hr.getTicketId());
            stm.setString(2, hr.getMessage());
            stm.setTimestamp(3, hr.getDateAction());
            stm.setInt(4, hr.getUserId());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void modifier(HistoriqueReclamation hr) {
        // Map to track modified fields and their new values
        Map<String, Object> modifiedFields = new HashMap<>();

        if (hr.getMessage() != null) {
            modifiedFields.put("message", hr.getMessage());
        }
       if (hr.getUserId() != 0) {
           modifiedFields.put("user_id", hr.getUserId());
       }




        if (modifiedFields.isEmpty()) {
            return;
        }

        StringBuilder req = new StringBuilder("UPDATE historiquereclamation SET ");
        for (Map.Entry<String, Object> entry : modifiedFields.entrySet()) {
            req.append(entry.getKey()).append(" = ?, ");
        }
        req.setLength(req.length() - 2);

        req.append(" WHERE id = ?");

        try {
            PreparedStatement stm = cnx.prepareStatement(req.toString());

            int index = 1;
            for (Map.Entry<String, Object> entry : modifiedFields.entrySet()) {
                stm.setObject(index++, entry.getValue());
            }
            stm.setInt(index, hr.getId());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void supprimer(HistoriqueReclamation hr) {
        String req = "DELETE FROM historiquereclamation WHERE id = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, hr.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<HistoriqueReclamation> getall() {
        List<HistoriqueReclamation> hrs = new ArrayList<>();
        String req = "SELECT * FROM historiquereclamation";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                HistoriqueReclamation hr = new HistoriqueReclamation();
                hr.setId(rs.getInt("id"));
                hr.setMessage(rs.getString("message"));
                hr.setDateAction(rs.getTimestamp("dateAction"));
                hr.setUserId(rs.getInt("user_id"));

                hrs.add(hr);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hrs;
    }

    @Override
    public HistoriqueReclamation getOneById(Integer id) {
        HistoriqueReclamation hr = new HistoriqueReclamation();
        String req = "SELECT * FROM historiquereclamation WHERE id = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                hr.setId(rs.getInt("id"));
                hr.setMessage(rs.getString("message"));
                hr.setDateAction(rs.getTimestamp("dateAction"));
                hr.setUserId(rs.getInt("user_id"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hr;
    }



    @Override
    public HistoriqueReclamation getone() {
        HistoriqueReclamation hr= new HistoriqueReclamation();
        String req = "SELECT * FROM historiquereclamation LIMIT 1";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);
            if (rs.next()) {
                hr.setId(rs.getInt("id"));
                hr.setMessage(rs.getString("message"));
                hr.setDateAction(rs.getTimestamp("dateAction"));
                hr.setUserId(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hr;
    }
}