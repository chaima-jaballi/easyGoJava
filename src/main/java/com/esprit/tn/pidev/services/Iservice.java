

package com.esprit.tn.pidev.services;

import com.esprit.tn.pidev.entities.Reservation;
import com.esprit.tn.pidev.entities.Trip;
import java.util.List;

public interface Iservice<T> {

    public void ajouter(T t);
    public void modifier(T t);
    public void supprimer(T t);
    public List<T> getall();
    public T getone();
    public T getOneById(Integer id) ;
}
