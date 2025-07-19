package bogotravel.controller;

import bogotravel.dao.EntradaDAO;
import bogotravel.model.Entrada;
import bogotravel.service.FotoEntradaService;
import bogotravel.sesion.SesionActual;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/** Controlador de la vista para crear o editar una entrada del diario de viaje. */
public class CrearEntradaController {

  private final List<File> archivosFotos =
      new ArrayList<>(); // Archivos seleccionados por el usuario
  private final EntradaDAO entradaDAO =
      new EntradaDAO(); // DAO para interactuar con la base de datos
  private final FotoEntradaService fotoService =
      new FotoEntradaService(); // Servicio para guardar fotos
  // === Componentes de la interfaz gráfica (inyectados con FXML) ===
  @FXML private TextField tituloField;
  @FXML private TextArea contenidoArea;
  @FXML private DatePicker fechaPicker;
  @FXML private TextField lugarField;
  @FXML private ListView<String> listaFotos;
  // === Atributos de lógica de negocio ===
  private Entrada entradaActual; // Entrada en edición (null si es nueva)
  private boolean entradaGuardada = false; // Indica si la entrada fue guardada exitosamente

  // === Getter público ===

  /** Indica si la entrada fue guardada correctamente (se usa para verificar en otras vistas). */
  public boolean isEntradaGuardada() {
    return entradaGuardada;
  }

  // === Métodos públicos ===

  /**
   * Carga los datos de una entrada existente en los campos del formulario (modo edición).
   *
   * @param entrada Entrada a editar
   */
  public void cargarEntrada(Entrada entrada) {
    this.entradaActual = entrada;
    tituloField.setText(entrada.getTitulo());
    contenidoArea.setText(entrada.getContenido());
    fechaPicker.setValue(entrada.getFechaVisita());
    lugarField.setText(entrada.getLugarDescripcion());
  }

  // === Métodos FXML (acciones de la interfaz) ===

  /** Permite al usuario seleccionar una imagen de su sistema para añadirla a la entrada. */
  @FXML
  public void agregarFoto() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleccionar imagen");
    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));

    File archivo = fileChooser.showOpenDialog(null);
    if (archivo != null) {
      archivosFotos.add(archivo);
      listaFotos.getItems().add(archivo.getName());
    }
  }

  /**
   * Guarda una nueva entrada o actualiza una existente. También guarda las imágenes asociadas a la
   * entrada.
   */
  @FXML
  public void guardarEntrada() {
    // Obtener valores del formulario
    String titulo = tituloField.getText();
    String contenido = contenidoArea.getText();
    LocalDate fecha = fechaPicker.getValue();
    String lugar = lugarField.getText();

    // Validación básica
    if (titulo.isEmpty() || contenido.isEmpty() || fecha == null) {
      new Alert(Alert.AlertType.WARNING, "Por favor complete todos los campos obligatorios.")
          .showAndWait();
      return;
    }
    // Validar que la fecha no sea futura
    if (fecha.isAfter(LocalDate.now())) {
      new Alert(Alert.AlertType.WARNING, "La fecha no puede ser futura.").showAndWait();
      return;
    }

    String emailUsuario = SesionActual.getUsuario().getEmail();
    boolean operacionExitosa;

    if (entradaActual != null) {
      // ✏ Modo edición: actualizamos entrada existente
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
      // Modo creación: creamos nueva entrada
      Entrada nuevaEntrada = new Entrada(titulo, contenido, fecha, lugar, emailUsuario);
      int entradaId = entradaDAO.crear(nuevaEntrada);
      operacionExitosa = entradaId != -1;

      // Si se creó correctamente, guardar las fotos con el ID correcto
      if (operacionExitosa) {
        for (File foto : archivosFotos) {
          fotoService.guardarFoto(foto, emailUsuario, entradaId);
        }
      }
    }

    // Mostrar resultado de la operación
    if (operacionExitosa) {
      entradaGuardada = true;
      new Alert(Alert.AlertType.INFORMATION, "Entrada guardada exitosamente.").showAndWait();

      // Cerrar ventana actual
      Stage stage = (Stage) tituloField.getScene().getWindow();
      stage.close();
    } else {
      new Alert(Alert.AlertType.ERROR, "Ocurrió un error al guardar la entrada.").showAndWait();
    }
  }

  // === Métodos privados ===

  /**
   * Limpia todos los campos del formulario y reinicia el estado del controlador. (Actualmente no se
   * llama desde ningún lugar, pero puede usarse para resetear el formulario si se desea).
   */
  private void limpiarFormulario() {
    tituloField.clear();
    contenidoArea.clear();
    fechaPicker.setValue(null);
    lugarField.clear();
    archivosFotos.clear();
    listaFotos.getItems().clear();
  }
}
