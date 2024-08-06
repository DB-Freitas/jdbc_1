package unit;

import couse.java.udemy.db.exceptions.DbException;
import couse.java.udemy.model.dao.impl.SellerDaoJDBC;
import couse.java.udemy.model.entities.Department;
import couse.java.udemy.model.entities.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class SellerDaoJDBCTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement st;

    @Mock
    private ResultSet rs;

    private SellerDaoJDBC sellerDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sellerDao = new SellerDaoJDBC(connection);
    }

    @Test
    @DisplayName("#insert > When the seller is inserted successfully > Set the generated id")
    public void testInsertWhenSellerIsInsertedSuccessfullySetGeneratedId() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(st);
        when(st.executeUpdate()).thenReturn(1);
        when(st.getGeneratedKeys()).thenReturn(rs);

        // Mock result set
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        // Create a seller
        Seller seller = new Seller(null, "John Doe", "john@example.com", new java.util.Date(), 3000.0, new Department(1, "Electronics"));

        sellerDao.insert(seller);

        // Verify the results
        assertNotNull(seller.getId());
        assertEquals(1, seller.getId());
    }

    @Test
    @DisplayName("#insert > When there is no row affected > Throw DbException")
    public void testInsertWhenNoRowAffectedThrowDbException() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(st);
        when(st.executeUpdate()).thenReturn(0);

        // Create a seller
        Seller seller = new Seller(null, "John Doe", "john@example.com", new java.util.Date(), 3000.0, new Department(1, "Electronics"));

        // Verify exception
        assertThrows(DbException.class, () -> sellerDao.insert(seller));
    }

    @Test
    @DisplayName("#update > When the seller is updated successfully > No exception thrown")
    public void testUpdateWhenSellerIsUpdatedSuccessfullyNoExceptionThrown() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(st);

        // Create a seller
        Seller seller = new Seller(1, "John Doe", "john@example.com", new java.util.Date(), 3000.0, new Department(1, "Electronics"));

        sellerDao.update(seller);

        // Verify
        verify(st, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("#update > When there is a SQL exception > Throw DbException")
    public void testUpdateWhenSQLExceptionOccursThrowDbException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Create a seller
        Seller seller = new Seller(1, "John Doe", "john@example.com", new java.util.Date(), 3000.0, new Department(1, "Electronics"));

        // Verify exception
        assertThrows(DbException.class, () -> sellerDao.update(seller));
    }

    @Test
    @DisplayName("#deleteById > When the seller is deleted successfully > No exception thrown")
    public void testDeleteByIdWhenSellerIsDeletedSuccessfullyNoExceptionThrown() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(st);

        sellerDao.deleteById(1);

        // Verify no exception is thrown
        verify(st, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("#deleteById > When there is a SQL exception > Throw DbException")
    public void testDeleteByIdWhenSQLExceptionOccursThrowDbException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Verify exception
        assertThrows(DbException.class, () -> sellerDao.deleteById(1));
    }

    @Test
    @DisplayName("#findById > When the seller is found > Return the seller")
    public void testFindByIdSellerIsFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(st);
        when(st.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("Id")).thenReturn(1);
        when(rs.getString("Name")).thenReturn("John Doe");
        when(rs.getString("Email")).thenReturn("john@example.com");
        when(rs.getDouble("BaseSalary")).thenReturn(3000.0);
        when(rs.getDate("BirthDate")).thenReturn(Date.valueOf("1990-01-01"));
        when(rs.getInt("DepartmentId")).thenReturn(1);
        when(rs.getString("DepName")).thenReturn("Sales");

        Seller seller = sellerDao.findById(1);

        // Verify
        assertNotNull(seller);
        assertEquals("John Doe", seller.getName());
        assertEquals("john@example.com", seller.getEmail());
        assertEquals(3000.0, seller.getBaseSalary(), 0.001);
        assertEquals(Date.valueOf("1990-01-01"), seller.getBirthDate());
        assertEquals("Sales", seller.getDepartment().getName());
    }

    @Test
    @DisplayName("#findById > When there is a SQL exception > Throw DbException")
    public void testFindByIdSQLExceptionOccurs() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));
        assertThrows(DbException.class, () -> sellerDao.findById(1));
    }

    @Test
    @DisplayName("#findAll > When sellers are found > Return the list of sellers")
    public void testFindAllWhenSellersAreFound() throws SQLException {
        // Setup mock behavior
        when(connection.prepareStatement(anyString())).thenReturn(st);
        when(st.executeQuery()).thenReturn(rs);

        // Mock result set data
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getInt("Id")).thenReturn(1).thenReturn(2);
        when(rs.getString("Name")).thenReturn("John Doe").thenReturn("Jane Doe");
        when(rs.getString("Email")).thenReturn("john@example.com").thenReturn("jane@example.com");
        when(rs.getDouble("BaseSalary")).thenReturn(3000.0).thenReturn(3500.0);
        when(rs.getDate("BirthDate")).thenReturn(Date.valueOf("1990-01-01")).thenReturn(Date.valueOf("1985-05-15"));
        when(rs.getInt("DepartmentId")).thenReturn(1).thenReturn(1);
        when(rs.getString("DepName")).thenReturn("Sales").thenReturn("Sales");

        // Call the method under test
        List<Seller> sellers = sellerDao.findAll();

        // Verify the results
        assertNotNull(sellers);
        assertEquals(2, sellers.size());
        assertEquals("John Doe", sellers.get(0).getName());
        assertEquals("Jane Doe", sellers.get(1).getName());
    }

    @Test
    @DisplayName("#findAll > When there is a SQL exception > Throw DbException")
    public void testFindAllWhenSQLExceptionOccurs() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));
        assertThrows(DbException.class, () -> sellerDao.findAll());
    }

    @Test
    @DisplayName("#findByDepartment > When sellers are found > Return the list of sellers")
    public void testFindByDepartmentWhenSellersAreFound() throws SQLException {
        // Setup mock behavior
        when(connection.prepareStatement(anyString())).thenReturn(st);
        when(st.executeQuery()).thenReturn(rs);

        // Mock result set data
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("Id")).thenReturn(1);
        when(rs.getString("Name")).thenReturn("John Doe");
        when(rs.getString("Email")).thenReturn("john@example.com");
        when(rs.getDouble("BaseSalary")).thenReturn(3000.0);
        when(rs.getDate("BirthDate")).thenReturn(Date.valueOf("1990-01-01"));
        when(rs.getInt("DepartmentId")).thenReturn(1);
        when(rs.getString("DepName")).thenReturn("Sales");

        // Create a department object
        Department department = new Department(1, "Sales");

        // Call the method under test
        List<Seller> sellers = sellerDao.findByDepartment(department);

        // Verify the results
        assertNotNull(sellers);
        assertEquals(1, sellers.size());
        assertEquals("John Doe", sellers.get(0).getName());
    }

    @Test
    @DisplayName("#findByDepartment > When there is a SQL exception > Throw DbException")
    public void testFindByDepartmentWhenSQLExceptionOccursThrowDbException() throws SQLException {
        // Setup mock behavior
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Create a department object
        Department department = new Department(1, "Electronics");

        // Call the method under test and verify exception
        assertThrows(DbException.class, () -> sellerDao.findByDepartment(department));
    }

}
