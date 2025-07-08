package bogotravel.utils;

import java.time.LocalDate;

public class PorVisitarInfo {
    private String nombreLugar;
    private int prioridad;
    private LocalDate recordatorio;

    public PorVisitarInfo(String nombreLugar, int prioridad, LocalDate recordatorio) {
        this.nombreLugar = nombreLugar;
        this.prioridad = prioridad;
        this.recordatorio = recordatorio;
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
