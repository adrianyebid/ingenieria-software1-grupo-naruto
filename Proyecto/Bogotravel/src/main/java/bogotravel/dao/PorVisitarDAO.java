package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.PorVisitar;
import bogotravel.utils.PorVisitarInfo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PorVisitarDAO {

    // Agregar lugar a la lista "por visitar"
    public boolean agregar(PorVisitar porVisitar) {
        String sql = "INSERT INTO por_visitar (id_lugar, email_usuario, prioridad, recordatorio) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, porVisitar.getIdLugar());
            statement.setString(2, porVisitar.getEmailUsuario());
            statement.setInt(3, porVisitar.getPrioridad());
            statement.setDate(4, porVisitar.getRecordatorio() != null ?
                    Date.valueOf(porVisitar.getRecordatorio()) : null);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al agregar lugar a lista por visitar: " + e.getMessage());
            return false;
        }
    }

    // Listar lugares por visitar de un usuario
    public List<PorVisitar> listarPorUsuario(String email) {
        List<PorVisitar> lista = new ArrayList<>();
        String sql = "SELECT * FROM por_visitar WHERE email_usuario = ? ORDER BY prioridad ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PorVisitar pv = new PorVisitar(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_lugar"),
                        resultSet.getString("email_usuario"),
                        resultSet.getInt("prioridad"),
                        resultSet.getDate("recordatorio") != null ? resultSet.getDate("recordatorio").toLocalDate() : null
                );
                lista.add(pv);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar lugares por visitar: " + e.getMessage());
        }

        return lista;
    }

    public List<PorVisitarInfo> listarConNombrePorUsuario(String emailUsuario) {
        List<PorVisitarInfo> lista = new ArrayList<>();

        String sql = "SELECT lt.nombre AS lugar_nombre, pv.prioridad, pv.recordatorio "
                + "FROM por_visitar pv "
                + "JOIN lugares_turisticos lt ON pv.id_lugar = lt.id "
                + "WHERE pv.email_usuario = ? "
                + "ORDER BY pv.prioridad ASC;";


        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, emailUsuario);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nombreLugar = resultSet.getString("lugar_nombre");
                int prioridad = resultSet.getInt("prioridad");
                LocalDate recordatorio = resultSet.getDate("recordatorio").toLocalDate();

                lista.add(new PorVisitarInfo(nombreLugar, prioridad, recordatorio));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener lugares por visitar con nombre: " + e.getMessage());
        }

        return lista;
    }

    // Eliminar un lugar de la lista
    public boolean eliminar(int id) {
        String sql = "DELETE FROM por_visitar WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar lugar de la lista: " + e.getMessage());
            return false;
        }
    }
}
