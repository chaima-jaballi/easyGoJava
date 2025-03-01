package com.esprit.tn.pidev.services;



import java.util.List;

public interface Iservice <T>{

    public void ajouter(T t);
    public void modifier(T t);
    public void supprimer(T t);
    public List<T> getall();
    public T getone();

    String getUserPhoneNumberById(int userId);

    public T getOneById(Integer x) ;
}