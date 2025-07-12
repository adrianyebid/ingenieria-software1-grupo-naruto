package bogotravel;

import bogotravel.dao.*;
import bogotravel.db.DBConnection;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import bogotravel.model.*;
import bogotravel.service.FotoEntradaService;
import bogotravel.sesion.SesionActual;
import bogotravel.utils.PorVisitarInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


public class Main extends Application {

    public static void main(String[] args) {


        //launch(args);


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