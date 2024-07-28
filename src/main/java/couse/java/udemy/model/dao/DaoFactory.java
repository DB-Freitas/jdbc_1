package couse.java.udemy.model.dao;

import couse.java.udemy.db.DB;
import couse.java.udemy.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());
    }
}
