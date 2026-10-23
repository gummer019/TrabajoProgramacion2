package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public abstract class PlantaRenovable extends Planta {

    protected String fuentePrimaria;
    protected double eficienciaPromedio;

    public PlantaRenovable(String nombre, String direccion, double capacidadMaximaDeGeneracion,
                           LocalDateTime fechaPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio) {
        // Llamada al constructor de la clase padre (Planta)
        super(nombre, direccion, capacidadMaximaDeGeneracion, fechaPuestaEnFuncionamiento);

        // Validaciones del IREP para Planta Renovable
        if (fuentePrimaria == null || fuentePrimaria.trim().isEmpty()) {
            throw new RuntimeException("Fuente primaria inválida");
        }
        if (eficienciaPromedio <= 0 || !Double.isFinite(eficienciaPromedio)) {
            throw new RuntimeException("Eficiencia promedio inválida");
        }

        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
    }

    public String getFuentePrimaria() {
        return fuentePrimaria;
    }

    public double getEficienciaPromedio() {
        return eficienciaPromedio;
    }
}