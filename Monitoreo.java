package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class Monitoreo {
	
	private LocalDateTime fechaHora;
    private double energiaGenerada;
    private int incidencias;

    public Monitoreo(LocalDateTime fechaHora, double energiaGenerada, int incidencias) {
    	
    	if (fechaHora == null) {
            throw new RuntimeException("Fecha del monitoreo invalida");
        }

        if (energiaGenerada < 0 || !Double.isFinite(energiaGenerada)) {
            throw new RuntimeException("Energia generada invalida");
        }

        if (incidencias < 0) {
            throw new RuntimeException("Cantidad de incidencias invalida");
        }
    	
    	
        this.fechaHora = fechaHora;
        this.energiaGenerada = energiaGenerada;
        this.incidencias = incidencias;
        
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public double getEnergiaGenerada() {
        return energiaGenerada;
    }

    public int getIncidencias() {
        return incidencias;
    }
}
	

