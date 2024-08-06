package unit;

import couse.java.udemy.db.exceptions.DbException;
import couse.java.udemy.model.dao.impl.*;
import couse.java.udemy.model.entities.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class DepartmentDaoJDBCTest {

    @InjectMocks
    private DepartmentDaoJDBC departmentDao;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement st;

    @Mock
    private ResultSet rs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        departmentDao = new DepartmentDaoJDBC(connection);
    }

    @Test
    @DisplayName("#insert > When the department is inserted successfully > Set the generated id")
    public void testInsertWhenDepartmentIsInsertedSuccessfullySetGeneratedId() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(st);
        when(st.executeUpdate()).thenReturn(1);
        when(st.getGeneratedKeys()).thenReturn(rs);

        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        Department department = new Department(null, "HR");
        departmentDao.insert(department);

        assertNotNull(department.getId());
        assertEquals(1, department.getId());
    }

    @Test
    @DisplayName("#insert > When there is no row affected > Throw DbException")
    public void testInsertWhenNoRowAffectedThrowDbException() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(st);
        when(st.executeUpdate()).thenReturn(0);

        Department department = new Department(null, "HR");

        assertThrows(DbException.class, () -> departmentDao.insert(department));
    }

    @Test
    @DisplayName("#update > When the department is updated successfully > No exception thrown")
    public void testUpdateWhenDepartmentIsUpdatedSuccessfullyNoExceptionThrown() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(st);

        Department department = new Department(1, "HR");
        departmentDao.update(department);

        verify(st, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("#update > When there is a SQL exception > Throw DbException")
    public void testUpdateWhenSQLExceptionOccursThrowDbException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Department department = new Department(1, "HR");

        assertThrows(DbException.class, () -> departmentDao.update(department));
    }

    @Test
    @DisplayName("#deleteById > When the department is deleted successfully > No exception thrown")
    public void testDeleteByIdWhenDepartmentIsDeletedSuccessfullyNoExceptionThrown() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(st);

        departmentDao.deleteById(1);

        verify(st, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("#deleteById > When there is a SQL exception > Throw DbException")
    public void testDeleteByIdWhenSQLExceptionOccursThrowDbException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(DbException.class, () -> departmentDao.deleteById(1));
    }

    @Test
    @DisplayName("#findById > When the department is found > Return the department")
    public void testFindByIdWhenDepartmentIsFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(st);
        when(st.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("Id")).thenReturn(1);
        when(rs.getString("Name")).thenReturn("HR");

        Department department = departmentDao.findById(1);

        assertNotNull(department);
        assertEquals(1, department.getId());
        assertEquals("HR", department.getName());
    }

    @Test
    @DisplayName("#findById > When there is a SQL exception > Throw DbException")
    public void testFindByIdWhenSQLExceptionOccursThrowDbException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(DbException.class, () -> departmentDao.findById(1));
    }

    @Test
    @DisplayName("#findAll > When departments are found > Return the list of departments")
    public void testFindAllWhenDepartmentsAreFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(st);
        when(st.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getInt("Id")).thenReturn(1).thenReturn(2);
        when(rs.getString("Name")).thenReturn("HR").thenReturn("IT");

        List<Department> departments = departmentDao.findAll();

        assertNotNull(departments);
        assertEquals(2, departments.size());
        assertEquals("HR", departments.get(0).getName());
        assertEquals("IT", departments.get(1).getName());
    }

    @Test
    @DisplayName("#findAll > When there is a SQL exception > Throw DbException")
    public void testFindAllWhenSQLExceptionOccursThrowDbException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(DbException.class, () -> departmentDao.findAll());
    }
}
