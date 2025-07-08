package bogotravel.model;

public class LugarTuristico {

    private int id;
    private String nombre;
    private String descripcion;
    private String localidad;
    private int idCategoria; // FK hacia tabla categorias

    public LugarTuristico() {}

    // Constructor sin ID (para inserción)
    public LugarTuristico(String nombre, String descripcion, String localidad, int idCategoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.localidad = localidad;
        this.idCategoria = idCategoria;
    }

    // Constructor con ID (para consultas)
    public LugarTuristico(int id, String nombre, String descripcion, String localidad, int idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.localidad = localidad;
        this.idCategoria = idCategoria;
    }

    // Getters y setters
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
