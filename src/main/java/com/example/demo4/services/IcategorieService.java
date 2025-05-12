/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temcategoriee file, choose Tools | Temcategoriees
 * and open the temcategoriee in the editor.
 */
package com.example.demo4.services;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author asus
 */
public interface IcategorieService<T> {
    
       public void ajoutercategorie(T t) throws SQLException;
    public void modifiercategorie(T t) throws SQLException;
    public void supprimercategorie(T t) throws SQLException;
    public List<T> recuperercategorie() throws SQLException;
    
}
