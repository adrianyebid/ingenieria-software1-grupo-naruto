package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.Entrada;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para manejar operaciones CRUD sobre la tabla 'entradas'.
 * Cada método interactúa con la base de datos para realizar acciones relacionadas
 * con las entradas del diario de viaje de un usuario.
 */
public class EntradaDAO {

    /**
     * Inserta una nueva entrada en la base de datos.
     *
     * @param entrada Objeto Entrada a insertar.
     * @return ID de la entrada creada, o -1 si hubo error.
     */
    // In EntradaDAO.java
    public int crear(Entrada entrada) {
        String sql = "INSERT INTO entradas (titulo, contenido, fecha_visita, lugar_descripcion, email_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entrada.getTitulo());
            statement.setString(2, entrada.getContenido());
            statement.setDate(3, Date.valueOf(entrada.getFechaVisita()));
            statement.setString(4, entrada.getLugarDescripcion());
            statement.setString(5, entrada.getEmailUsuario());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al crear entrada: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Obtiene una lista de entradas asociadas a un usuario, ordenadas por fecha de visita descendente.
     *
     * @param email Correo del usuario.
     * @return Lista de objetos Entrada.
     */
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
                        resultSet.getString("lugar_descripcion"),
                        resultSet.getString("email_usuario")
                );
                entradas.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar entradas: " + e.getMessage());
        }

        return entradas;
    }

    /**
     * Busca una entrada específica por su ID.
     *
     * @param id ID de la entrada a buscar.
     * @return Objeto Entrada si se encuentra, null si no existe o hay error.
     */
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
                        resultSet.getString("lugar_descripcion"),
                        resultSet.getString("email_usuario")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar entrada: " + e.getMessage());
        }

        return null;
    }

    /**
     * Actualiza una entrada existente en la base de datos.
     *
     * @param entrada Objeto Entrada con la información a actualizar.
     * @return true si se actualizó correctamente, false si hubo error.
     */
    public boolean actualizar(Entrada entrada) {
        String sql = "UPDATE entradas SET titulo = ?, contenido = ?, fecha_visita = ?, lugar_descripcion = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entrada.getTitulo());
            statement.setString(2, entrada.getContenido());
            statement.setDate(3, Date.valueOf(entrada.getFechaVisita()));
            statement.setString(4, entrada.getLugarDescripcion());
            statement.setInt(5, entrada.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar entrada: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una entrada de la base de datos usando su ID.
     *
     * @param id ID de la entrada a eliminar.
     * @return true si se eliminó correctamente, false si hubo error.
     */
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
