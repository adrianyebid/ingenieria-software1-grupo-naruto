package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Registrar un nuevo usuario
    public boolean registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, username, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getUsername());
            statement.setString(4, usuario.getPassword()); // Toca hashear esto xd

            int filas = statement.executeUpdate();
            return filas > 0; // para saber si se insertó correctamente

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
                        resultSet.getString("username"),
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
            // Aquí debemos hashear el passwordPlano y compararlo con el hasheado en la base de datos
            // return BCrypt.checkpw(passwordPlano, usuario.getPassword());
            return usuario.getPassword().equals(passwordPlano); // Solo si no hasheas
        }
        return false;
    }

}
