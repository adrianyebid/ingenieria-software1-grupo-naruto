package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.FotoEntrada;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para gestionar las operaciones relacionadas con la tabla fotos_entrada.
 */
public class FotoEntradaDAO {

    /**
     * Guarda una nueva foto asociada a una entrada en la base de datos.
     *
     * @param foto Objeto FotoEntrada que contiene el ID de la entrada y la ruta de la foto.
     * @return true si se guardó correctamente, false en caso contrario.
     */
    public boolean guardarFoto(FotoEntrada foto) {
        String sql = "INSERT INTO fotos_entrada (entrada_id, ruta) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, foto.getEntradaId());
            statement.setString(2, foto.getRuta());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar foto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todas las fotos asociadas a una entrada específica.
     *
     * @param entradaId ID de la entrada cuyas fotos se desean consultar.
     * @return Lista de objetos FotoEntrada correspondientes a la entrada.
     */
    public List<FotoEntrada> listarPorEntrada(int entradaId) {
        List<FotoEntrada> fotos = new ArrayList<>();
        String sql = "SELECT * FROM fotos_entrada WHERE entrada_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, entradaId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                FotoEntrada foto = new FotoEntrada(
                        resultSet.getInt("id"),
                        resultSet.getInt("entrada_id"),
                        resultSet.getString("ruta")
                );
                fotos.add(foto);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar fotos de entrada: " + e.getMessage());
        }

        return fotos;
    }

    /**
     * Busca una foto específica por su ID.
     *
     * @param id ID de la foto que se desea consultar.
     * @return Objeto FotoEntrada correspondiente, o null si no se encuentra.
     */
    public FotoEntrada buscarPorId(int id) {
        String sql = "SELECT * FROM fotos_entrada WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new FotoEntrada(
                        rs.getInt("id"),
                        rs.getInt("entrada_id"),
                        rs.getString("ruta")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar foto: " + e.getMessage());
        }

        return null;
    }

    /**
     * Elimina una foto de la base de datos por su ID.
     *
     * @param id ID de la foto que se desea eliminar.
     * @return true si se eliminó correctamente, false en caso contrario.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM fotos_entrada WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar foto: " + e.getMessage());
            return false;
        }
    }
}
