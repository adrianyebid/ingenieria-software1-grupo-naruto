package bogotravel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/bogotravel";
    private static final String USER = "postgres"; // reemplaza con tu usuario real
    private static final String PASSWORD = "root"; // reemplaza con tu contraseña real

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
