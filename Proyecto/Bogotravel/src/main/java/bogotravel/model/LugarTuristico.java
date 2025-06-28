package bogotravel.model;

public class LugarTuristico {

    private int id;
    private String nombre;
    private String tipo;         // Cultural, Natural, Gastronómico, etc.
    private String descripcion;
    private String localidad;


    public LugarTuristico() {}

    // Constructor sin id (para inserción)
    public LugarTuristico(String nombre, String tipo, String descripcion, String localidad) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.localidad = localidad;
    }

    // Constructor completo con id (para consultas desde la base de datos)
    public LugarTuristico(int id, String nombre, String tipo, String descripcion, String localidad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.localidad = localidad;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
}
