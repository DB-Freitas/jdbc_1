package couse.java.udemy.application;

import couse.java.udemy.model.entities.Department;
import couse.java.udemy.model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {

        Department dept = new Department(1, "Books");

        Seller seller = new Seller(15, "Bob", "bob@gmail.com",
                new Date(), 3000.00, dept);

        System.out.println(seller);


    }
}
