package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class MonitoreoEolico extends Monitoreo {
    private double velocidadViento;

    public MonitoreoEolico(LocalDateTime fechaHora,
            double energiaGenerada, int incidencias,
            double velocidadViento) {

        super(fechaHora, energiaGenerada, incidencias);

        if (velocidadViento < 0 || !Double.isFinite(velocidadViento)) {
            throw new RuntimeException("Velocidad del viento invalida");
        }

        this.velocidadViento = velocidadViento;
    }
    
    
    public double getVelocidadViento() {
        return velocidadViento;
    }
}