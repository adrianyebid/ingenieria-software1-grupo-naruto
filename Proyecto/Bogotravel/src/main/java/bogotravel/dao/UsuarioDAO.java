package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Registrar un nuevo usuario
    public boolean registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Hashear la contraseña ANTES de guardarla
            String passwordHasheado = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, passwordHasheado);  // Guardamos el hash, no el texto plano

            int filas = statement.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    // Buscar usuario por email
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

    // Validar login
    public boolean validarCredenciales(String email, String passwordPlano) {
        Usuario usuario = buscarPorEmail(email);
        if (usuario != null) {
            // Compara el password plano con el password cifrado en la BD
            return BCrypt.checkpw(passwordPlano, usuario.getPassword());
        }
        return false;
    }

}
