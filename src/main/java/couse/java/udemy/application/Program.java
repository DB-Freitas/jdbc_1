package couse.java.udemy.application;

import couse.java.udemy.model.dao.DaoFactory;
import couse.java.udemy.model.dao.SellerDao;
import couse.java.udemy.model.entities.Department;
import couse.java.udemy.model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println(sellerDao.findById(3));


    }
}
