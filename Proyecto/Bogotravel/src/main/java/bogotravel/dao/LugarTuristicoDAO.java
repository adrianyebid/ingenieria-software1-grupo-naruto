package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.LugarTuristico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LugarTuristicoDAO {

    // Listar todos los lugares turísticos
    public List<LugarTuristico> listarTodos() {
        List<LugarTuristico> lugares = new ArrayList<>();
        String sql = "SELECT * FROM lugares_turisticos ORDER BY nombre ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                LugarTuristico lugar = new LugarTuristico(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("tipo"),
                        resultSet.getString("descripcion"),
                        resultSet.getString("localidad")
                );
                lugares.add(lugar);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar lugares turísticos: " + e.getMessage());
        }

        return lugares;
    }

    // Buscar lugar por ID
    public LugarTuristico buscarPorId(int id) {
        String sql = "SELECT * FROM lugares_turisticos WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new LugarTuristico(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("tipo"),
                        resultSet.getString("descripcion"),
                        resultSet.getString("localidad")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar lugar turístico: " + e.getMessage());
        }
        return null;
    }

    // Insertar nuevo lugar turístico
    public boolean insertar(LugarTuristico lugar) {
        String sql = "INSERT INTO lugares_turisticos (nombre, tipo, descripcion, localidad) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, lugar.getNombre());
            statement.setString(2, lugar.getTipo());
            statement.setString(3, lugar.getDescripcion());
            statement.setString(4, lugar.getLocalidad());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar lugar turístico: " + e.getMessage());
            return false;
        }
    }
}
