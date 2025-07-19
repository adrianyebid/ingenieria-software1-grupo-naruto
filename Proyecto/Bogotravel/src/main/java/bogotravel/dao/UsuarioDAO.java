package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase DAO para manejar operaciones de base de datos relacionadas con usuarios.
 */
public class UsuarioDAO {
    /**
     * Registra un nuevo usuario en la base de datos.
     * La contraseña se almacena en formato hasheado con BCrypt.
     *
     * @param usuario Objeto Usuario con los datos a registrar.
     * @return true si el registro fue exitoso, false si ocurrió un error.
     */
    public boolean registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Hashear la contraseña antes de almacenarla
            String passwordHasheado = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, passwordHasheado);

            int filas = statement.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un usuario en la base de datos por su correo electrónico.
     *
     * @param email Correo del usuario a buscar.
     * @return Objeto Usuario si se encuentra, null si no.
     */
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Usuario(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    /**
     * Valida las credenciales ingresadas por el usuario.
     * Compara el password plano con el almacenado en la base de datos.
     *
     * @param email Correo del usuario.
     * @param passwordPlano Contraseña ingresada por el usuario.
     * @return true si las credenciales son válidas, false si no.
     */
    public boolean validarCredenciales(String email, String passwordPlano) {
        Usuario usuario = buscarPorEmail(email);
        if (usuario != null) {
            // Verifica si el password coincide con el hash
            return BCrypt.checkpw(passwordPlano, usuario.getPassword());
        }
        return false;
    }

}
