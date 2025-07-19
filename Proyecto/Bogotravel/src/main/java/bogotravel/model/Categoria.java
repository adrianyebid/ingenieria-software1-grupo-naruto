package bogotravel.model;

/** Clase que representa una categoría de viaje. */
public class Categoria {
  private int id;
  private String nombre;

  /**
   * Constructor por defecto. Este constructor es necesario para la serialización y deserialización
   * de objetos.
   */
  public Categoria() {}

  /**
   * Constructor con parámetros.
   *
   * @param id Identificador de la categoría.
   * @param nombre Nombre de la categoría.
   */
  public Categoria(int id, String nombre) {
    this.id = id;
    this.nombre = nombre;
  }

  /**
   * Constructor con nombre.
   *
   * @param nombre Nombre de la categoría.
   */
  public Categoria(String nombre) {
    this.nombre = nombre;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
}
