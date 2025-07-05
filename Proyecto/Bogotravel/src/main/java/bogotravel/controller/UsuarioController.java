package bogotravel.ventana;

import bogotravel.db.DBConnection;
import java.sql.Connection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class EntradaController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println("Conexión a BD realizada");
        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}