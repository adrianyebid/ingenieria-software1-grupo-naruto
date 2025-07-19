package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.LugarTuristico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LugarTuristicoDAOTest {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private LugarTuristicoDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        dao = new LugarTuristicoDAO();
    }

    @Test
    void testBuscarPorId_existe() throws Exception {
        try (MockedStatic<DBConnection> mocked = Mockito.mockStatic(DBConnection.class)) {
            mocked.when(DBConnection::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("nombre")).thenReturn("Monserrate");
            when(resultSet.getString("descripcion")).thenReturn("Iglesia en la cima de Bogotá");
            when(resultSet.getString("localidad")).thenReturn("Santa Fe");
            when(resultSet.getInt("id_categoria")).thenReturn(2);
            when(resultSet.getString("imagen_url")).thenReturn("http://img.com/monserrate.jpg");

            LugarTuristico lugar = dao.buscarPorId(1);

            assertNotNull(lugar);
            assertEquals("Monserrate", lugar.getNombre());
        }
    }

    @Test
    void testInsertar_ok() throws Exception {
        LugarTuristico lugar = new LugarTuristico(0, "Museo del Oro", "Museo histórico", "La Candelaria", 1, "http://img.com/oro.jpg");

        try (MockedStatic<DBConnection> mocked = Mockito.mockStatic(DBConnection.class)) {
            mocked.when(DBConnection::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeUpdate()).thenReturn(1);

            boolean result = dao.insertar(lugar);

            assertTrue(result);
        }
    }

    @Test
    void testInsertar_falla() throws Exception {
        LugarTuristico lugar = new LugarTuristico(0, "Falla", "No se puede", "Suba", 3, "http://fail.com");

        try (MockedStatic<DBConnection> mocked = Mockito.mockStatic(DBConnection.class)) {
            mocked.when(DBConnection::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB rota"));

            boolean result = dao.insertar(lugar);

            assertFalse(result);
        }
    }

    @Test
    void testListarPorCategoriaYLocalidad_vacio() throws Exception {
        try (MockedStatic<DBConnection> mocked = Mockito.mockStatic(DBConnection.class)) {
            mocked.when(DBConnection::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            List<LugarTuristico> lugares = dao.listarPorCategoriaYLocalidad(99, "inexistente");

            assertNotNull(lugares);
            assertTrue(lugares.isEmpty());
        }
    }
}
