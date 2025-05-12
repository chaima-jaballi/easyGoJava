/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4.services;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author asus
 */
public interface IvehiculeService<T> {
    
       public void ajoutervehicule(T t) throws SQLException;
    public void modifiervehicule(T t) throws SQLException;
    public void supprimervehicule(T t) throws SQLException;
    public List<T> recuperervehicule() throws SQLException;
    
}
