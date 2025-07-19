package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.FotoEntrada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FotoEntradaDAOTest {

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
    public void testGuardarFoto() throws Exception {
        FotoEntrada foto = new FotoEntrada(0, 1, "ruta/ejemplo.jpg");

        try (MockedStatic<DBConnection> mockedStatic = mockStatic(DBConnection.class)) {
            mockedStatic.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(1);

            FotoEntradaDAO dao = new FotoEntradaDAO();
            boolean resultado = dao.guardarFoto(foto);

            assertTrue(resultado);
            verify(mockStatement).setInt(1, 1);
            verify(mockStatement).setString(2, "ruta/ejemplo.jpg");
        }
    }

    @Test
    public void testListarPorEntrada() throws Exception {
        try (MockedStatic<DBConnection> mockedStatic = mockStatic(DBConnection.class)) {
            mockedStatic.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getInt("entrada_id")).thenReturn(2);
            when(mockResultSet.getString("ruta")).thenReturn("ruta/foto.jpg");

            FotoEntradaDAO dao = new FotoEntradaDAO();
            List<FotoEntrada> lista = dao.listarPorEntrada(2);

            assertEquals(1, lista.size());
            assertEquals("ruta/foto.jpg", lista.get(0).getRuta());
        }
    }

    @Test
    public void testBuscarPorId() throws Exception {
        try (MockedStatic<DBConnection> mockedStatic = mockStatic(DBConnection.class)) {
            mockedStatic.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getInt("entrada_id")).thenReturn(5);
            when(mockResultSet.getString("ruta")).thenReturn("imagen.jpg");

            FotoEntradaDAO dao = new FotoEntradaDAO();
            FotoEntrada foto = dao.buscarPorId(1);

            assertNotNull(foto);
            assertEquals(5, foto.getEntradaId());
            assertEquals("imagen.jpg", foto.getRuta());
        }
    }

    @Test
    public void testEliminar() throws Exception {
        try (MockedStatic<DBConnection> mockedStatic = mockStatic(DBConnection.class)) {
            mockedStatic.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(1);

            FotoEntradaDAO dao = new FotoEntradaDAO();
            boolean resultado = dao.eliminar(1);

            assertTrue(resultado);
            verify(mockStatement).setInt(1, 1);
        }
    }
}
