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

public class PorVisitarController {

    @FXML
    private Label nombreLugarLabel;

    @FXML
    private TextField prioridadField;

    @FXML
    private DatePicker recordatorioPicker;

    @FXML
    private Label mensajeLabel;

    private LugarTuristico lugar;

    private Stage detallesStage; // Ventana de detalles que abrió este formulario
    private Stage popupStage;    // La ventana actual (PorVisitarView)


    public void setDetallesStage(Stage stage, Stage popupStage) {
        this.detallesStage = stage;
        this.popupStage = popupStage;

    }


    public void setLugar(LugarTuristico lugar) {
        this.lugar = lugar;
        nombreLugarLabel.setText(lugar.getNombre());
    }

    @FXML
    private void guardarLugar() {
        String prioridadTexto = prioridadField.getText().trim();
        LocalDate fechaSeleccionada = recordatorioPicker.getValue();

        if (!prioridadTexto.matches("[1-3]")) {
            mensajeLabel.setText("La prioridad debe ser 1 (Alta), 2 (Media) o 3 (Baja).");
            return;
        }

        if (fechaSeleccionada == null || fechaSeleccionada.isBefore(LocalDate.now())) {
            mensajeLabel.setText("Selecciona una fecha válida de recordatorio.");
            return;
        }

        int prioridad = Integer.parseInt(prioridadTexto);
        String emailUsuario = SesionActual.getUsuario().getEmail();

        PorVisitar nuevo = new PorVisitar();
        nuevo.setIdLugar(lugar.getId());
        nuevo.setEmailUsuario(emailUsuario);
        nuevo.setPrioridad(prioridad);
        nuevo.setRecordatorio(fechaSeleccionada);

        PorVisitarDAO porVisitarDAO = new PorVisitarDAO();

        if (porVisitarDAO.yaExiste(lugar.getId(), emailUsuario)) {
            mensajeLabel.setText("Este lugar ya está en tu lista por visitar.");
            return;
        }

        if (!porVisitarDAO.agregar(nuevo)) {
            mensajeLabel.setText("Error al agregar el lugar a la lista por visitar.");
            return;
        }

        mensajeLabel.setText("¡Lugar agregado exitosamente!");

        // Cerrar ventana actual y abrir InicioView después de 1 segundo
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                javafx.application.Platform.runLater(() -> {
                    try {
                        // Cerrar ambas ventanas
                        if (popupStage != null) popupStage.close();
                        if (detallesStage != null) detallesStage.close();

                        // Abrir InicioView en una nueva ventana
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

    @FXML
    private void cancelar() {
        Stage stage = (Stage) prioridadField.getScene().getWindow();
        stage.close();
    }
}
