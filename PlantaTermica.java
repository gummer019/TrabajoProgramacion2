package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaTermica extends Planta {

    private String tipoCombustible;
    private double objetivoEmisionesCo2;
    private double capacidadAlmacenamientoCombustible;

    public PlantaTermica(String nombre, String direccion, double capacidadMaximaDeGeneracion,
                         LocalDateTime fechaPuestaEnFuncionamiento, String tipoCombustible,
                         double objetivoEmisionesCo2, double capacidadAlmacenamientoCombustible) {
        
        // Llamada al constructor de la clase padre (Planta)
        super(nombre, direccion, capacidadMaximaDeGeneracion, fechaPuestaEnFuncionamiento);

        //IREP
        if (tipoCombustible == null || tipoCombustible.trim().isEmpty()) {
            throw new RuntimeException("El tipo de combustible es inválido");
        }
        if (objetivoEmisionesCo2 < 0 || !Double.isFinite(objetivoEmisionesCo2)) {
            throw new RuntimeException("El objetivo de emisiones de CO2 no puede ser negativo");
        }
        if (capacidadAlmacenamientoCombustible < 0 || !Double.isFinite(capacidadAlmacenamientoCombustible)) {
            throw new RuntimeException("La capacidad de almacenamiento no puede ser negativa");
        }

        this.tipoCombustible = tipoCombustible;
        this.objetivoEmisionesCo2 = objetivoEmisionesCo2;
        this.capacidadAlmacenamientoCombustible = capacidadAlmacenamientoCombustible;
    }

    public String getTipoCombustible() { return tipoCombustible; }
    public double getObjetivoEmisionesCo2() { return objetivoEmisionesCo2; }
    public double getCapacidadAlmacenamientoCombustible() { return capacidadAlmacenamientoCombustible; }

    @Override
    public double calcularCapacidadMinimaDeCuota() {
        // Deben generar la mitad de su capacidad máxima.
        return capacidadMaximaDeGeneracion / 2.0;
    }

    @Override
    public boolean cumplioCuota(Monitoreo monitoreo) {
        if (monitoreo == null) return false;
        
        // Verificamos que alcance la energía mínima
        boolean alcanzaEnergia = monitoreo.getEnergiaGenerada() >= calcularCapacidadMinimaDeCuota();
        
        // Verificamos que sea un monitoreo térmico para evaluar las emisiones
        if (monitoreo instanceof MonitoreoTermico) {
            MonitoreoTermico mt = (MonitoreoTermico) monitoreo;
            // Tiene que emitir estrictamente MENOS CO2 que el objetivo
            return alcanzaEnergia && mt.getCo2Generado() < objetivoEmisionesCo2;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return nombre + " (TERMICA)";
    }
}