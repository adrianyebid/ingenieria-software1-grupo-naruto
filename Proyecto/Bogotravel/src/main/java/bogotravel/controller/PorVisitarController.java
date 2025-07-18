package bogotravel.controller;

import bogotravel.dao.PorVisitarDAO;
import bogotravel.model.LugarTuristico;
import bogotravel.model.PorVisitar;
import bogotravel.sesion.SesionActual;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controlador para la vista que permite agregar un lugar turístico a la lista "Por Visitar".
 */
public class PorVisitarController {

    // ================================
    // Referencias a elementos de la vista FXML
    // ================================

    @FXML
    private Label nombreLugarLabel;

    @FXML
    private TextField prioridadField;

    @FXML
    private DatePicker recordatorioPicker;

    @FXML
    private Label mensajeLabel;

    // ================================
    // Atributos internos
    // ================================

    private LugarTuristico lugar;
    private Stage detallesStage; // Ventana desde donde se abrió el popup actual
    private Stage popupStage;    // Ventana actual del formulario PorVisitar

    // ================================
    // Métodos de configuración
    // ================================

    /**
     * Establece las referencias a las ventanas relacionadas.
     *
     * @param stage       Ventana de detalles
     * @param popupStage  Ventana actual (popup)
     */
    public void setDetallesStage(Stage stage, Stage popupStage) {
        this.detallesStage = stage;
        this.popupStage = popupStage;
    }

    /**
     * Establece el lugar turístico a mostrar en el formulario.
     *
     * @param lugar Lugar seleccionado
     */
    public void setLugar(LugarTuristico lugar) {
        this.lugar = lugar;
        nombreLugarLabel.setText(lugar.getNombre());
    }

    // ================================
    // Métodos de acción (botones)
    // ================================

    /**
     * Acción que guarda el lugar seleccionado en la lista de lugares por visitar,
     * con su prioridad y fecha de recordatorio.
     */
    @FXML
    private void guardarLugar() {
        String prioridadTexto = prioridadField.getText().trim();
        LocalDate fechaSeleccionada = recordatorioPicker.getValue();

        // Validación de prioridad (1, 2 o 3)
        if (!prioridadTexto.matches("[1-3]")) {
            mensajeLabel.setText("La prioridad debe ser 1 (Alta), 2 (Media) o 3 (Baja).");
            return;
        }

        // Validación de fecha de recordatorio
        if (fechaSeleccionada == null || fechaSeleccionada.isBefore(LocalDate.now())) {
            mensajeLabel.setText("Selecciona una fecha válida de recordatorio.");
            return;
        }

        int prioridad = Integer.parseInt(prioridadTexto);
        String emailUsuario = SesionActual.getUsuario().getEmail();

        // Crear objeto PorVisitar
        PorVisitar nuevo = new PorVisitar();
        nuevo.setIdLugar(lugar.getId());
        nuevo.setEmailUsuario(emailUsuario);
        nuevo.setPrioridad(prioridad);
        nuevo.setRecordatorio(fechaSeleccionada);

        PorVisitarDAO porVisitarDAO = new PorVisitarDAO();

        // Verificar si ya existe el lugar en la lista del usuario
        if (porVisitarDAO.yaExiste(lugar.getId(), emailUsuario)) {
            mensajeLabel.setText("Este lugar ya está en tu lista por visitar.");
            return;
        }

        // Intentar guardar en la base de datos
        if (!porVisitarDAO.agregar(nuevo)) {
            mensajeLabel.setText("Error al agregar el lugar a la lista por visitar.");
            return;
        }

        mensajeLabel.setText("¡Lugar agregado exitosamente!");

        // Abrir vista de inicio y cerrar ventanas después de 1 segundo
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                javafx.application.Platform.runLater(() -> {
                    try {
                        if (popupStage != null) popupStage.close();
                        if (detallesStage != null) detallesStage.close();

                        Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/InicioView.fxml"));
                        Stage nuevaVentana = new Stage();
                        root.getStylesheets().add("css/PaginaPrincipal.css");
                        nuevaVentana.setScene(new Scene(root));
                        nuevaVentana.setTitle("Inicio");
                        nuevaVentana.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Acción que cierra la ventana actual sin guardar cambios.
     */
    @FXML
    private void cancelar() {
        Stage stage = (Stage) prioridadField.getScene().getWindow();
        stage.close();
    }
}
