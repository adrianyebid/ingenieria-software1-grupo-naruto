package bogotravel;

import bogotravel.dao.UsuarioDAO;
import bogotravel.db.DBConnection;
import java.sql.Connection;

import bogotravel.model.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        System.out.println("Conectando a la base de datos...");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bogotravel/view/UsuarioView.fxml"));
//        Scene scene = new Scene(loader.load());
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Bogotravel");
//        primaryStage.show();
    }


}