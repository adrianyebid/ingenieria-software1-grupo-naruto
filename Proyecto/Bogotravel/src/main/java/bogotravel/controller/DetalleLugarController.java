package bogotravel.controller;

import bogotravel.model.LugarTuristico;
import bogotravel.sesion.SesionActual;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador para la vista de detalle de un lugar turístico. Permite visualizar la información del
 * lugar, volver a la vista anterior, cerrar sesión y agregar el lugar a la lista "Por Visitar".
 */
public class DetalleLugarController {

  // === Componentes de la interfaz gráfica ===
  @FXML private Label nombre;

  @FXML private Label descripcion;

  @FXML private Label localidad;

  @FXML private ImageView imagenView;

  @FXML private Button CerrarButton;

  @FXML private Button VolverButton;

  @FXML private Button AgregarButton;

  // === Datos de lógica ===
  private LugarTuristico lugarActual;

  // === Métodos públicos ===

  /**
   * Carga los datos del lugar turístico y los muestra en la vista.
   *
   * @param lugar El lugar turístico a mostrar
   */
  public void cargarLugar(LugarTuristico lugar) {
    this.lugarActual = lugar;
    nombre.setText(lugar.getNombre());
    descripcion.setText(lugar.getDescripcion());
    localidad.setText(lugar.getLocalidad());
    imagenView.setImage(new Image(lugar.getImagenUrl()));
  }

  // === Métodos FXML (acciones de la vista) ===

  /** Cierra la sesión actual y redirige al usuario a la pantalla de login. */
  @FXML
  private void cerrarSesion(ActionEvent event) {
    SesionActual.cerrarSesion();

    try {
      Parent root =
          FXMLLoader.load(getClass().getResource("/bogotravel/view/UsuarioLoginView.fxml"));
      Stage stage = (Stage) CerrarButton.getScene().getWindow();
      root.getStylesheets().add("css/Inicio.css");
      stage.setScene(new Scene(root));
    } catch (IOException e) {
      e.printStackTrace();
      new Alert(Alert.AlertType.ERROR, "No se pudo volver a la vista de inicio de sesión.")
          .showAndWait();
    }
  }

  /** Regresa a la vista principal de la aplicación. */
  @FXML
  private void VolverAction(ActionEvent event) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/InicioView.fxml"));
      Stage stage = (Stage) CerrarButton.getScene().getWindow();
      root.getStylesheets().add("css/PaginaPrincipal.css");
      stage.setScene(new Scene(root));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Abre una nueva ventana para agregar el lugar actual a la lista "Por Visitar". */
  @FXML
  private void agregarLugarPorVisitar(ActionEvent event) {
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("/bogotravel/view/PorVisitarView.fxml"));
      Parent root = loader.load();

      // Obtener controlador y pasar el lugar actual
      PorVisitarController controller = loader.getController();
      controller.setLugar(lugarActual);

      // Obtener ventana actual y preparar nueva ventana
      Stage detallesStage = (Stage) AgregarButton.getScene().getWindow();
      Stage popupStage = new Stage();
      controller.setDetallesStage(detallesStage, popupStage); // Paso importante

      popupStage.setTitle("Agregar a Por Visitar");
      popupStage.setScene(new Scene(root));
      popupStage.setResizable(false);
      popupStage.initModality(Modality.APPLICATION_MODAL);
      popupStage.showAndWait();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
