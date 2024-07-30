package couse.java.udemy.application;

import couse.java.udemy.model.dao.DaoFactory;
import couse.java.udemy.model.dao.SellerDao;
import couse.java.udemy.model.entities.Department;
import couse.java.udemy.model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("========= TEST 1 - Find by id");
        System.out.println(sellerDao.findById(3));

        System.out.println("========= TEST 2 - Find by department");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        for (Seller obj : list){
            System.out.println(obj);
        }

        System.out.println("========= TEST 2 - Find all");
        list = sellerDao.findAll();
        for (Seller obj : list){
            System.out.println(obj);
        }
    }
}
