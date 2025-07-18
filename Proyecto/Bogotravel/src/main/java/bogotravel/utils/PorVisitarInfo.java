package bogotravel.utils;

import java.time.LocalDate;

public class PorVisitarInfo {

    private int idPorVisitar;
    private String nombreLugar;
    private int prioridad;
    private LocalDate recordatorio;

    public PorVisitarInfo(int idPorVisitar, String nombreLugar, int prioridad, LocalDate recordatorio) {
        this.idPorVisitar = idPorVisitar;
        this.nombreLugar = nombreLugar;
        this.prioridad = prioridad;
        this.recordatorio = recordatorio;
    }

    public int getIdPorVisitar() {
        return idPorVisitar;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public LocalDate getRecordatorio() {
        return recordatorio;
    }


}
