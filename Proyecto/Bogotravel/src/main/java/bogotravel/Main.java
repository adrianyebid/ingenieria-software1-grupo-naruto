package bogotravel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  public static void main(String[] args) {

    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader =
        new FXMLLoader(getClass().getResource("/bogotravel/view/UsuarioRegistroView.fxml"));
    Scene scene = new Scene(loader.load());
    scene.getStylesheets().add("css/Inicio.css");
    primaryStage.setScene(scene);
    primaryStage.setTitle("Bogotravel");
    primaryStage.show();
  }
}
