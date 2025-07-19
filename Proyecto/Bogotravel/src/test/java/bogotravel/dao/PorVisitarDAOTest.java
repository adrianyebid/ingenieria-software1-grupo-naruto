package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.PorVisitar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * PorVisitarDAOTest.java
 * Unit tests for the PorVisitarDAO class.
 *
 * These tests verify the correct implementation of CRUD methods for the 'por_visitar' table.
 *
 * Test coverage:
 * - testAgregar: Verifies successful addition of a visit item.
 * - testYaExiste: Checks if a visit item already exists for a user and place.
 * - testEliminar: Verifies successful deletion of a visit item by ID.
 * - testListarPorUsuario: Lists all visit items for a specific user.
 */
public class PorVisitarDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    public void testAgregar() throws Exception {
        PorVisitar pv = new PorVisitar(0, 3, "usuario@correo.com", 1, LocalDate.of(2025, 12, 25));

        try (MockedStatic<DBConnection> mockedStatic = mockStatic(DBConnection.class)) {
            mockedStatic.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(1);

            PorVisitarDAO dao = new PorVisitarDAO();
            boolean resultado = dao.agregar(pv);

            assertTrue(resultado);
            verify(mockStatement).setInt(1, 3);
            verify(mockStatement).setString(2, "usuario@correo.com");
            verify(mockStatement).setInt(3, 1);
            verify(mockStatement).setDate(eq(4), any(Date.class));
        }
    }

    @Test
    public void testYaExiste() throws Exception {
        try (MockedStatic<DBConnection> mockedStatic = mockStatic(DBConnection.class)) {
            mockedStatic.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);

            PorVisitarDAO dao = new PorVisitarDAO();
            boolean existe = dao.yaExiste(3, "usuario@correo.com");

            assertTrue(existe);
        }
    }

    @Test
    public void testEliminar() throws Exception {
        try (MockedStatic<DBConnection> mockedStatic = mockStatic(DBConnection.class)) {
            mockedStatic.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(1);

            PorVisitarDAO dao = new PorVisitarDAO();
            boolean resultado = dao.eliminar(5);

            assertTrue(resultado);
            verify(mockStatement).setInt(1, 5);
        }
    }

    @Test
    public void testListarPorUsuario() throws Exception {
        try (MockedStatic<DBConnection> mockedStatic = mockStatic(DBConnection.class)) {
            mockedStatic.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getInt("id_lugar")).thenReturn(2);
            when(mockResultSet.getString("email_usuario")).thenReturn("usuario@correo.com");
            when(mockResultSet.getInt("prioridad")).thenReturn(1);
            when(mockResultSet.getDate("recordatorio")).thenReturn(Date.valueOf("2025-07-18"));

            PorVisitarDAO dao = new PorVisitarDAO();
            List<PorVisitar> lista = dao.listarPorUsuario("usuario@correo.com");

            assertEquals(1, lista.size());
            assertEquals("usuario@correo.com", lista.get(0).getEmailUsuario());
        }
    }


}
