package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.Entrada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EntradaDAOTest {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private EntradaDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        dao = new EntradaDAO();
    }

    @Test
    public void testCrear_exito() throws Exception {
        Entrada entrada = new Entrada(0, "Título", "Contenido", LocalDate.now(), "Lugar", "correo@test.com");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);
            when(statement.executeUpdate()).thenReturn(1);
            when(statement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(42);

            int id = dao.crear(entrada);

            assertEquals(42, id);
        }
    }

    @Test
    public void testCrear_falla() throws Exception {
        Entrada entrada = new Entrada(0, "Título", "Contenido", LocalDate.now(), "Lugar", "correo@test.com");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenThrow(new SQLException("Error DB"));

            int id = dao.crear(entrada);

            assertEquals(-1, id);
        }
    }

    @Test
    public void testListarPorUsuario() throws Exception {
        String email = "test@correo.com";

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("titulo")).thenReturn("Título");
            when(resultSet.getString("contenido")).thenReturn("Contenido");
            when(resultSet.getDate("fecha_visita")).thenReturn(Date.valueOf(LocalDate.now()));
            when(resultSet.getString("lugar_descripcion")).thenReturn("Lugar");
            when(resultSet.getString("email_usuario")).thenReturn(email);

            List<Entrada> entradas = dao.listarPorUsuario(email);

            assertEquals(1, entradas.size());
            assertEquals("Título", entradas.get(0).getTitulo());
        }
    }

    @Test
    public void testBuscarPorId_encontrado() throws Exception {
        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("titulo")).thenReturn("Título");
            when(resultSet.getString("contenido")).thenReturn("Contenido");
            when(resultSet.getDate("fecha_visita")).thenReturn(Date.valueOf(LocalDate.now()));
            when(resultSet.getString("lugar_descripcion")).thenReturn("Lugar");
            when(resultSet.getString("email_usuario")).thenReturn("correo@test.com");

            Entrada entrada = dao.buscarPorId(1);

            assertNotNull(entrada);
            assertEquals(1, entrada.getId());
        }
    }

    @Test
    public void testBuscarPorId_noEncontrado() throws Exception {
        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(false);

            Entrada entrada = dao.buscarPorId(999);

            assertNull(entrada);
        }
    }

    @Test
    public void testActualizar_exito() throws Exception {
        Entrada entrada = new Entrada(1, "Nuevo Título", "Nuevo contenido", LocalDate.now(), "Lugar nuevo", "email@test.com");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeUpdate()).thenReturn(1);

            boolean actualizado = dao.actualizar(entrada);

            assertTrue(actualizado);
        }
    }

    @Test
    public void testEliminar_exito() throws Exception {
        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeUpdate()).thenReturn(1);

            boolean eliminado = dao.eliminar(1);

            assertTrue(eliminado);
        }
    }

    @Test
    public void testEliminar_falla() throws Exception {
        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenThrow(new SQLException("Falla"));

            boolean eliminado = dao.eliminar(1);

            assertFalse(eliminado);
        }
    }
}
