package com.esprit.tn.pidev.entities;
import java.util.Objects;
import java.sql.Timestamp;
public class HistoriqueReclamation {
    private int id ;
    private int tiketId;
    private String message;
    private Timestamp dateAction;
    private int userId;

    //constructeurs
    public HistoriqueReclamation (int i, int ticketId, String message, Timestamp dateAction, int userId){

    }

public HistoriqueReclamation(int userId,String message,Timestamp dateAction,int tiketId){
        this.userId=userId;
        this.message=message;
        this.dateAction=dateAction;
        this.tiketId=tiketId;
}

    public HistoriqueReclamation() {

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTicketId() { return tiketId; }
    public void setTicketId(int ticketId) { this.tiketId = ticketId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Timestamp getDateAction() { return dateAction; }
    public void setDateAction(Timestamp dateAction) { this.dateAction = dateAction; }
    @Override
    public int hashCode() {
        return Objects.hash(id, message);
    }


    @Override
    public String toString() {
        return "Personne{" +
                "Id=" + id +
                ", message='" + message + '\'' +
                ", dateAction='" + dateAction + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoriqueReclamation hr)) return false;
        return id == hr.id && Objects.equals(message, hr.message) && Objects.equals(userId, hr.userId) && Objects.equals(dateAction, hr.dateAction);

    }
}