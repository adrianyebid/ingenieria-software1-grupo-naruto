package bogotravel.model;

import java.time.LocalDate;

public class Entrada {
    private int id;
    private String titulo;
    private String contenido;
    private LocalDate fechaVisita;
    private int idLugar;           // FK (puede ser NULL si el lugar no es de la tabla)
    private String emailUsuario;   // FK hacia usuarios (por email)


    public Entrada() {}

    // Constructor sin id (para insertar)
    public Entrada(String titulo, String contenido, LocalDate fechaVisita, int idLugar, String emailUsuario) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaVisita = fechaVisita;
        this.idLugar = idLugar;
        this.emailUsuario = emailUsuario;
    }

    // Constructor completo
    public Entrada(int id, String titulo, String contenido, LocalDate fechaVisita, int idLugar, String emailUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaVisita = fechaVisita;
        this.idLugar = idLugar;
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

    public int getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(int idLugar) {
        this.idLugar = idLugar;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}
