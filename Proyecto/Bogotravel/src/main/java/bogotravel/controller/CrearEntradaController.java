package bogotravel.controller;

import bogotravel.dao.EntradaDAO;
import bogotravel.model.Entrada;
import bogotravel.sesion.SesionActual;
import bogotravel.service.FotoEntradaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CrearEntradaController {

    @FXML
    private TextField tituloField;
    @FXML
    private TextArea contenidoArea;
    @FXML
    private DatePicker fechaPicker;
    @FXML
    private TextField lugarField;
    @FXML
    private ListView<String> listaFotos;

    private Entrada entradaActual;

    private List<File> archivosFotos = new ArrayList<>();
    private final EntradaDAO entradaDAO = new EntradaDAO();
    private final FotoEntradaService fotoService = new FotoEntradaService();

    @FXML
    public void agregarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg")
        );

        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            archivosFotos.add(archivo);
            listaFotos.getItems().add(archivo.getName());
        }
    }

    @FXML
    public void guardarEntrada() {
        String titulo = tituloField.getText();
        String contenido = contenidoArea.getText();
        LocalDate fecha = fechaPicker.getValue();
        String lugar = lugarField.getText();

        if (titulo.isEmpty() || contenido.isEmpty() || fecha == null) {
            new Alert(Alert.AlertType.WARNING, "Por favor complete todos los campos obligatorios.").showAndWait();
            return;
        }

        String emailUsuario = SesionActual.getUsuario().getEmail();

        boolean operacionExitosa;

        if (entradaActual != null) {
            // ✏️ Estamos editando
            entradaActual.setTitulo(titulo);
            entradaActual.setContenido(contenido);
            entradaActual.setFechaVisita(fecha);
            entradaActual.setLugarDescripcion(lugar);

            operacionExitosa = entradaDAO.actualizar(entradaActual);

            // Guardar nuevas fotos si se agregaron
            for (File foto : archivosFotos) {
                fotoService.guardarFoto(foto, emailUsuario, entradaActual.getId());
            }

        } else {
            //  Estamos creando
            Entrada nuevaEntrada = new Entrada(titulo, contenido, fecha, lugar, emailUsuario);
            operacionExitosa = entradaDAO.crear(nuevaEntrada);

            // Si se creó correctamente, obtener el ID para guardar las fotos
            if (operacionExitosa) {
                List<Entrada> entradasUsuario = entradaDAO.listarPorUsuario(emailUsuario);
                int entradaId = entradasUsuario.get(0).getId(); // La más reciente

                for (File foto : archivosFotos) {
                    fotoService.guardarFoto(foto, emailUsuario, entradaId);
                }
            }
        }

        if (operacionExitosa) {
            new Alert(Alert.AlertType.INFORMATION, "Entrada guardada exitosamente.").showAndWait();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/InicioView.fxml"));
                Stage stage = (Stage) tituloField.getScene().getWindow();
                root.getStylesheets().add("css/PaginaPrincipal.css");
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error al regresar a la página de inicio.").showAndWait();
            }

        } else {
            new Alert(Alert.AlertType.ERROR, "Ocurrió un error al guardar la entrada.").showAndWait();
        }
    }


    public void cargarEntrada(Entrada entrada) {
        this.entradaActual = entrada;
        tituloField.setText(entrada.getTitulo());
        contenidoArea.setText(entrada.getContenido());
        fechaPicker.setValue(entrada.getFechaVisita());
        lugarField.setText(entrada.getLugarDescripcion());
    }


    private void limpiarFormulario() {
        tituloField.clear();
        contenidoArea.clear();
        fechaPicker.setValue(null);
        lugarField.clear();
        archivosFotos.clear();
        listaFotos.getItems().clear();
    }
}
