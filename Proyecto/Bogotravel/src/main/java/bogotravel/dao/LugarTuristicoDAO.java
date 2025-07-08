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
                        resultSet.getString("descripcion"),
                        resultSet.getString("localidad"),
                        resultSet.getInt("id_categoria")  // Nuevo campo
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
                        resultSet.getString("descripcion"),
                        resultSet.getString("localidad"),
                        resultSet.getInt("id_categoria") // Nuevo campo
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar lugar turístico: " + e.getMessage());
        }
        return null;
    }

    // Insertar nuevo lugar turístico
    public boolean insertar(LugarTuristico lugar) {
        String sql = "INSERT INTO lugares_turisticos (nombre, descripcion, localidad, id_categoria) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, lugar.getNombre());
            statement.setString(2, lugar.getDescripcion());
            statement.setString(3, lugar.getLocalidad());
            statement.setInt(4, lugar.getIdCategoria());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar lugar turístico: " + e.getMessage());
            return false;
        }
    }
}
