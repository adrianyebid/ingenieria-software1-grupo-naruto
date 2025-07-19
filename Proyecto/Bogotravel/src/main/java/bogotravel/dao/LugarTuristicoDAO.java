package bogotravel.dao;

import bogotravel.db.DBConnection;
import bogotravel.model.LugarTuristico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para manejar operaciones CRUD y consultas personalizadas sobre los lugares turísticos
 * almacenados en la base de datos.
 */
public class LugarTuristicoDAO {

  /**
   * Lista todos los lugares turísticos disponibles, ordenados por nombre.
   *
   * @return Lista de objetos LugarTuristico.
   */
  public List<LugarTuristico> listarTodos() {
    List<LugarTuristico> lugares = new ArrayList<>();
    String sql = "SELECT * FROM lugares_turisticos ORDER BY nombre ASC";

    try (Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        LugarTuristico lugar =
            new LugarTuristico(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getString("localidad"),
                resultSet.getInt("id_categoria"),
                resultSet.getString("imagen_url"));
        lugares.add(lugar);
      }

    } catch (SQLException e) {
      System.out.println("Error al listar lugares turísticos: " + e.getMessage());
    }

    return lugares;
  }

  /**
   * Busca un lugar turístico por su ID.
   *
   * @param id ID del lugar.
   * @return Objeto LugarTuristico o null si no se encuentra.
   */
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
            resultSet.getInt("id_categoria"),
            resultSet.getString("imagen_url"));
      }

    } catch (SQLException e) {
      System.out.println("Error al buscar lugar turístico: " + e.getMessage());
    }

    return null;
  }

  /**
   * Inserta un nuevo lugar turístico en la base de datos.
   *
   * @param lugar Objeto LugarTuristico a insertar.
   * @return true si se insertó correctamente, false si hubo error.
   */
  public boolean insertar(LugarTuristico lugar) {
    String sql =
        "INSERT INTO lugares_turisticos (nombre, descripcion, localidad, id_categoria, imagenUrl) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, lugar.getNombre());
      statement.setString(2, lugar.getDescripcion());
      statement.setString(3, lugar.getLocalidad());
      statement.setInt(4, lugar.getIdCategoria());
      statement.setString(5, lugar.getImagenUrl()); // ← Arreglado: antes usabas índice 4 dos veces

      return statement.executeUpdate() > 0;

    } catch (SQLException e) {
      System.out.println("Error al insertar lugar turístico: " + e.getMessage());
      return false;
    }
  }

  /**
   * Lista lugares turísticos por categoría.
   *
   * @param idCategoria ID de la categoría.
   * @return Lista de lugares de esa categoría.
   */
  public List<LugarTuristico> listarPorCategoria(int idCategoria) {
    List<LugarTuristico> lugares = new ArrayList<>();
    String sql = "SELECT * FROM lugares_turisticos WHERE id_categoria = ? ORDER BY nombre ASC";

    try (Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, idCategoria);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        LugarTuristico lugar =
            new LugarTuristico(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getString("localidad"),
                resultSet.getInt("id_categoria"),
                resultSet.getString("imagen_url"));
        lugares.add(lugar);
      }

    } catch (SQLException e) {
      System.out.println("Error al listar por categoría: " + e.getMessage());
    }

    return lugares;
  }

  /**
   * Lista lugares turísticos por localidad.
   *
   * @param localidad Nombre de la localidad (case-insensitive).
   * @return Lista de lugares en esa localidad.
   */
  public List<LugarTuristico> listarPorLocalidad(String localidad) {
    List<LugarTuristico> lugares = new ArrayList<>();
    String sql =
        "SELECT * FROM lugares_turisticos WHERE LOWER(localidad) = LOWER(?) ORDER BY nombre ASC";

    try (Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, localidad);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        LugarTuristico lugar =
            new LugarTuristico(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getString("localidad"),
                resultSet.getInt("id_categoria"),
                resultSet.getString("imagen_url"));
        lugares.add(lugar);
      }

    } catch (SQLException e) {
      System.out.println("Error al listar por localidad: " + e.getMessage());
    }

    return lugares;
  }

  /**
   * Busca lugares turísticos cuyo nombre contenga un texto parcial.
   *
   * @param nombreParcial Texto parcial a buscar.
   * @return Lista de coincidencias por nombre.
   */
  public List<LugarTuristico> buscarPorNombre(String nombreParcial) {
    List<LugarTuristico> lugares = new ArrayList<>();
    String sql =
        "SELECT * FROM lugares_turisticos WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY nombre ASC";

    try (Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, "%" + nombreParcial + "%");
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        LugarTuristico lugar =
            new LugarTuristico(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getString("localidad"),
                resultSet.getInt("id_categoria"),
                resultSet.getString("imagen_url"));
        lugares.add(lugar);
      }

    } catch (SQLException e) {
      System.out.println("Error al buscar por nombre: " + e.getMessage());
    }

    return lugares;
  }

  /**
   * Lista lugares turísticos filtrados por categoría y localidad.
   *
   * @param idCategoria ID de la categoría.
   * @param localidad Localidad del lugar turístico.
   * @return Lista de lugares que cumplen ambas condiciones.
   */
  public List<LugarTuristico> listarPorCategoriaYLocalidad(int idCategoria, String localidad) {
    List<LugarTuristico> lugares = new ArrayList<>();
    String sql =
        "SELECT * FROM lugares_turisticos WHERE id_categoria = ? AND LOWER(localidad) = LOWER(?) ORDER BY nombre ASC";

    try (Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, idCategoria);
      statement.setString(2, localidad);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        LugarTuristico lugar =
            new LugarTuristico(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getString("localidad"),
                resultSet.getInt("id_categoria"),
                resultSet.getString("imagen_url"));
        lugares.add(lugar);
      }

    } catch (SQLException e) {
      System.out.println("Error al listar por categoría y localidad: " + e.getMessage());
    }

    return lugares;
  }
}
