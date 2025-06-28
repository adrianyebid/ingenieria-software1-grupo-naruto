package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.Entrada;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAO {

    // Insertar una nueva entrada
    public boolean crear(Entrada entrada) {
        String sql = "INSERT INTO entradas (titulo, contenido, fecha_visita, id_lugar, email_usuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entrada.getTitulo());
            statement.setString(2, entrada.getContenido());
            statement.setDate(3, Date.valueOf(entrada.getFechaVisita()));
            statement.setInt(4, entrada.getIdLugar());
            statement.setString(5, entrada.getEmailUsuario());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear entrada: " + e.getMessage());
            return false;
        }
    }

    // Listar entradas de un usuario
    public List<Entrada> listarPorUsuario(String email) {
        List<Entrada> entradas = new ArrayList<>();
        String sql = "SELECT * FROM entradas WHERE email_usuario = ? ORDER BY fecha_visita DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Entrada e = new Entrada(
                        resultSet.getInt("id"),
                        resultSet.getString("titulo"),
                        resultSet.getString("contenido"),
                        resultSet.getDate("fecha_visita").toLocalDate(),
                        resultSet.getInt("id_lugar"),
                        resultSet.getString("email_usuario")
                );
                entradas.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar entradas: " + e.getMessage());
        }

        return entradas;
    }

    // Buscar entrada por ID
    public Entrada buscarPorId(int id) {
        String sql = "SELECT * FROM entradas WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Entrada(
                        resultSet.getInt("id"),
                        resultSet.getString("titulo"),
                        resultSet.getString("contenido"),
                        resultSet.getDate("fecha_visita").toLocalDate(),
                        resultSet.getInt("id_lugar"),
                        resultSet.getString("email_usuario")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar entrada: " + e.getMessage());
        }

        return null;
    }

    // Eliminar entrada por ID
    public boolean eliminar(int id) {
        String sql = "DELETE FROM entradas WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar entrada: " + e.getMessage());
            return false;
        }
    }


}
