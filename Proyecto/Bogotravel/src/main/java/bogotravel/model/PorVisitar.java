package bogotravel.model;

import java.time.LocalDate;

public class PorVisitar {

  private int id;
  private int idLugar;
  private String emailUsuario;
  private int prioridad;
  private LocalDate recordatorio;

  // Constructor vacío
  public PorVisitar() {}

  // Constructor sin id (para insertar)
  public PorVisitar(int idLugar, String emailUsuario, int prioridad, LocalDate recordatorio) {
    this.idLugar = idLugar;
    this.emailUsuario = emailUsuario;
    this.prioridad = prioridad;
    this.recordatorio = recordatorio;
  }

  // Constructor completo
  public PorVisitar(
      int id, int idLugar, String emailUsuario, int prioridad, LocalDate recordatorio) {
    this.id = id;
    this.idLugar = idLugar;
    this.emailUsuario = emailUsuario;
    this.prioridad = prioridad;
    this.recordatorio = recordatorio;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public int getPrioridad() {
    return prioridad;
  }

  public void setPrioridad(int prioridad) {
    this.prioridad = prioridad;
  }

  public LocalDate getRecordatorio() {
    return recordatorio;
  }

  public void setRecordatorio(LocalDate recordatorio) {
    this.recordatorio = recordatorio;
  }
}
