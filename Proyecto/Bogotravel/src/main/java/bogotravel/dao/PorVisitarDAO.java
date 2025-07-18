package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.PorVisitar;
import bogotravel.utils.PorVisitarInfo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar la persistencia de lugares por visitar asociados a usuarios.
 */
public class PorVisitarDAO {

    /**
     * Inserta un nuevo registro en la lista "Por Visitar" para un usuario.
     *
     * @param porVisitar Objeto con la información a insertar.
     * @return true si se insertó exitosamente, false en caso contrario.
     */
    public boolean agregar(PorVisitar porVisitar) {
        String sql = "INSERT INTO por_visitar (id_lugar, email_usuario, prioridad, recordatorio) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, porVisitar.getIdLugar());
            statement.setString(2, porVisitar.getEmailUsuario());
            statement.setInt(3, porVisitar.getPrioridad());
            statement.setDate(4, porVisitar.getRecordatorio() != null
                    ? Date.valueOf(porVisitar.getRecordatorio()) : null);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al agregar lugar a lista por visitar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un lugar ya está en la lista "Por Visitar" de un usuario.
     *
     * @param idLugar       ID del lugar turístico.
     * @param emailUsuario  Email del usuario.
     * @return true si ya existe, false si no.
     */
    public boolean yaExiste(int idLugar, String emailUsuario) {
        String sql = "SELECT COUNT(*) FROM por_visitar WHERE id_lugar = ? AND email_usuario = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idLugar);
            statement.setString(2, emailUsuario);

            ResultSet rs = statement.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.out.println("Error al verificar existencia: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un lugar de la lista "Por Visitar" por su ID.
     *
     * @param id ID del registro por visitar.
     * @return true si se eliminó correctamente, false si ocurrió un error.
     */
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

    /**
     * Lista todos los lugares "Por Visitar" de un usuario.
     *
     * @param email Email del usuario.
     * @return Lista de objetos PorVisitar.
     */
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
                        resultSet.getDate("recordatorio") != null
                                ? resultSet.getDate("recordatorio").toLocalDate() : null
                );
                lista.add(pv);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar lugares por visitar: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Lista los lugares "Por Visitar" con nombre del lugar (JOIN con lugares_turisticos).
     *
     * @param emailUsuario Email del usuario.
     * @return Lista de objetos PorVisitarInfo con nombre, prioridad y recordatorio.
     */
    public List<PorVisitarInfo> listarConNombrePorUsuario(String emailUsuario) {
        List<PorVisitarInfo> lista = new ArrayList<>();
        String sql = """
                SELECT pv.id AS id_por_visitar, lt.nombre AS lugar_nombre, pv.prioridad, pv.recordatorio
                FROM por_visitar pv
                JOIN lugares_turisticos lt ON pv.id_lugar = lt.id
                WHERE pv.email_usuario = ?
                ORDER BY pv.prioridad ASC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, emailUsuario);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_por_visitar");
                String nombreLugar = resultSet.getString("lugar_nombre");
                int prioridad = resultSet.getInt("prioridad");
                Date recordatorioDate = resultSet.getDate("recordatorio");
                LocalDate recordatorio = recordatorioDate != null ? recordatorioDate.toLocalDate() : null;

                lista.add(new PorVisitarInfo(id, nombreLugar, prioridad, recordatorio));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener lugares por visitar con nombre: " + e.getMessage());
        }

        return lista;
    }
}
