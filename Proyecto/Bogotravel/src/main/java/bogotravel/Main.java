package bogotravel;

import bogotravel.dao.EntradaDAO;
import bogotravel.dao.LugarTuristicoDAO;
import bogotravel.dao.PorVisitarDAO;
import bogotravel.dao.UsuarioDAO;
import bogotravel.db.DBConnection;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import bogotravel.model.Entrada;
import bogotravel.model.LugarTuristico;
import bogotravel.model.PorVisitar;
import bogotravel.model.Usuario;
import bogotravel.sesion.SesionActual;
import bogotravel.utils.PorVisitarInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {

        //conexion bd con try catch
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (Exception e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return; // Terminar la aplicación si no se puede conectar
        }

        launch(args);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bogotravel/view/UsuarioView.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bogotravel");
        primaryStage.show();
    }


}