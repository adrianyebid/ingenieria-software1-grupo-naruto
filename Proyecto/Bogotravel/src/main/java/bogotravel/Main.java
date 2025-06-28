package bogotravel;

import bogotravel.db.DBConnection;

import java.sql.Connection;


public class Main {

    public static void main(String[] args) {
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println("Conexión a BD realizada");
        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}