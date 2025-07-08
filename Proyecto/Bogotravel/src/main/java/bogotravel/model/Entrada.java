package bogotravel.model;

import java.time.LocalDate;

public class Entrada {
    private int id;
    private String titulo;
    private String contenido;
    private LocalDate fechaVisita;
    private String lugarDescripcion;   // nuevo campo
    private String emailUsuario;

    public Entrada() {}

    // Constructor sin id (para insertar)
    public Entrada(String titulo, String contenido, LocalDate fechaVisita, String lugarDescripcion , String emailUsuario) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaVisita = fechaVisita;
        this.lugarDescripcion = lugarDescripcion;
        this.emailUsuario = emailUsuario;
    }
    // Constructor sin lugarDescripcion (para insertar sin este campo)
    public Entrada(String titulo, String contenido, LocalDate fechaVisita, String emailUsuario) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaVisita = fechaVisita;
        this.emailUsuario = emailUsuario;
    }


    // Constructor completo
    public Entrada(int id, String titulo, String contenido, LocalDate fechaVisita, String lugarDescripcion, String emailUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaVisita = fechaVisita;
        this.lugarDescripcion = lugarDescripcion;
        this.emailUsuario = emailUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDate getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(LocalDate fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public String getLugarDescripcion() {
        return lugarDescripcion;
    }

    public void setLugarDescripcion(String lugarDescripcion) {
        this.lugarDescripcion = lugarDescripcion;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}
