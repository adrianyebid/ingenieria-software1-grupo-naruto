package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UsuarioDAO.
 * Se utilizan mocks para evitar dependencia de una base de datos real.
 */
class UsuarioDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        usuarioDAO = new UsuarioDAO();
    }

    @Test
    void testRegistrarUsuarioExitoso() throws Exception {
        Usuario nuevoUsuario = new Usuario(0, "Ana", "ana@example.com", "1234");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> mockDb = mockStatic(DBConnection.class)) {
            mockDb.when(DBConnection::getConnection).thenReturn(mockConnection);

            boolean resultado = usuarioDAO.registrar(nuevoUsuario);

            assertTrue(resultado, "El usuario debería haberse registrado exitosamente");
            verify(mockStatement, times(1)).setString(eq(1), eq("Ana"));
            verify(mockStatement, times(1)).setString(eq(2), eq("ana@example.com"));
            verify(mockStatement, times(1)).setString(eq(3), anyString()); // hash
        }
    }

    @Test
    void testRegistrarUsuarioFallaPorExcepcion() {
        Usuario usuario = new Usuario(0, "Ana", "ana@example.com", "1234");

        try (MockedStatic<DBConnection> mockDb = mockStatic(DBConnection.class)) {
            mockDb.when(DBConnection::getConnection).thenThrow(new RuntimeException("Error de conexión simulada"));

            boolean resultado = usuarioDAO.registrar(usuario);
            assertFalse(resultado, "El método debe retornar false si ocurre una excepción al registrar");
        }
    }



    @Test
    void testBuscarPorEmailUsuarioEncontrado() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("nombre")).thenReturn("Juan");
        when(mockResultSet.getString("email")).thenReturn("juan@example.com");
        when(mockResultSet.getString("password")).thenReturn("hash123");

        try (MockedStatic<DBConnection> mockDb = mockStatic(DBConnection.class)) {
            mockDb.when(DBConnection::getConnection).thenReturn(mockConnection);

            Usuario usuario = usuarioDAO.buscarPorEmail("juan@example.com");

            assertNotNull(usuario);
            assertEquals("Juan", usuario.getNombre());
            assertEquals("juan@example.com", usuario.getEmail());
            assertEquals("hash123", usuario.getPassword());
        }
    }

    @Test
    void testBuscarPorEmailUsuarioNoExiste() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        try (MockedStatic<DBConnection> mockDb = mockStatic(DBConnection.class)) {
            mockDb.when(DBConnection::getConnection).thenReturn(mockConnection);

            Usuario usuario = usuarioDAO.buscarPorEmail("inexistente@example.com");

            assertNull(usuario, "Debe retornar null si no se encuentra el usuario");
        }
    }

    @Test
    void testValidarCredencialesCorrectas() throws Exception {
        String email = "lucas@example.com";
        String passwordPlano = "secreta";
        String passwordHash = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(2);
        when(mockResultSet.getString("nombre")).thenReturn("Lucas");
        when(mockResultSet.getString("email")).thenReturn(email);
        when(mockResultSet.getString("password")).thenReturn(passwordHash);

        try (MockedStatic<DBConnection> mockDb = mockStatic(DBConnection.class)) {
            mockDb.when(DBConnection::getConnection).thenReturn(mockConnection);

            boolean valido = usuarioDAO.validarCredenciales(email, passwordPlano);
            assertTrue(valido, "Las credenciales deberían ser válidas");
        }
    }

    @Test
    void testValidarCredencialesIncorrectas() throws Exception {
        String email = "lucas@example.com";
        String passwordPlano = "malaclave";
        String passwordHash = BCrypt.hashpw("correcta", BCrypt.gensalt());

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(2);
        when(mockResultSet.getString("nombre")).thenReturn("Lucas");
        when(mockResultSet.getString("email")).thenReturn(email);
        when(mockResultSet.getString("password")).thenReturn(passwordHash);

        try (MockedStatic<DBConnection> mockDb = mockStatic(DBConnection.class)) {
            mockDb.when(DBConnection::getConnection).thenReturn(mockConnection);

            boolean valido = usuarioDAO.validarCredenciales(email, passwordPlano);
            assertFalse(valido, "La contraseña es incorrecta, debe retornar false");
        }
    }

    @Test
    void testValidarCredencialesUsuarioNoExiste() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // no existe

        try (MockedStatic<DBConnection> mockDb = mockStatic(DBConnection.class)) {
            mockDb.when(DBConnection::getConnection).thenReturn(mockConnection);

            boolean valido = usuarioDAO.validarCredenciales("noexiste@example.com", "clave");
            assertFalse(valido, "Debe retornar false si el usuario no existe");
        }
    }
}
