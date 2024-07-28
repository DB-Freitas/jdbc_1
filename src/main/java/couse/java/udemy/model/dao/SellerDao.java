package couse.java.udemy.model.dao;

import couse.java.udemy.model.entities.Seller;

import java.util.List;

public interface SellerDao {

    void insert (Seller obj);
    void update (Seller obj);
    void deleteById (Integer id);
    Seller findById (Integer id);
    List<Seller> findAll();
}
