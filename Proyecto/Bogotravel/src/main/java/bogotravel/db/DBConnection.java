package bogotravel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión a la base de datos PostgreSQL.
 * Utiliza JDBC para establecer la conexión y proporciona un método estático
 * para obtener una instancia de Connection.
 */
public class DBConnection {

  private static final String URL = "jdbc:postgresql://localhost:5432/bogotravel";
  private static final String USER = "postgres"; // reemplaza con tu usuario real
  private static final String PASSWORD = "root"; // reemplaza con tu contraseña real

  /**
   * Obtiene una conexión a la base de datos.
   *
   * @return Connection objeto que representa la conexión a la base de datos.
   * @throws SQLException si ocurre un error al establecer la conexión.
   */
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }
}
