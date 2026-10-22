package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class MonitoreoTermico extends Monitoreo {
    private double co2Emitido;
    private double combustibleUtilizado;

    public MonitoreoTermico(LocalDateTime fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
    	
        super(fechaHora, energiaGenerada, incidencias);
        
        if (co2Emitido < 0 || !Double.isFinite(co2Emitido)) {
            throw new RuntimeException("Emision de CO2 invalida");
        }

        if (combustibleUtilizado < 0 || !Double.isFinite(combustibleUtilizado)) {
            throw new RuntimeException("Cantidad de combustible invalida");
        }
        
        this.co2Emitido = co2Emitido;
        this.combustibleUtilizado = combustibleUtilizado;
    }

    public double getCo2Emitido() {
        return co2Emitido;
    }

    public double getCombustibleUtilizado() {
        return combustibleUtilizado;
    }
    
}