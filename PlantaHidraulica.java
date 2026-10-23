package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaHidraulica extends PlantaRenovable {

    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public PlantaHidraulica(String nombre, String direccion, double capacidadMaximaDeGeneracion,
                            LocalDateTime fechaPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
                            int cantidadTurbinas, double capacidadEmbalse) {

        // Llamada al constructor de la clase padre (PlantaRenovable -> Planta)
        super(nombre, direccion, capacidadMaximaDeGeneracion, fechaPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio);

        //IREP
        if (cantidadTurbinas <= 0) {
            throw new RuntimeException("La cantidad de turbinas debe ser mayor a 0");
        }
        if (capacidadEmbalse <= 0 || !Double.isFinite(capacidadEmbalse)) {
            throw new RuntimeException("La capacidad del embalse debe ser mayor a 0");
        }

        this.cantidadTurbinas = cantidadTurbinas;
        this.capacidadEmbalse = capacidadEmbalse;
    }

    public int getCantidadTurbinas() { return cantidadTurbinas; }
    public double getCapacidadEmbalse() { return capacidadEmbalse; }

@Override
    public double calcularCapacidadMinimaDeCuota() {
        return capacidadMaximaDeGeneracion * eficienciaPromedio;
    }

    @Override
    public boolean cumplioCuota(Monitoreo monitoreo) {
        if (monitoreo == null) return false;
        return monitoreo.getEnergiaGenerada() >= calcularCapacidadMinimaDeCuota();
    }
    
    @Override
    public String toString() {
        return nombre + " (HIDRAULICA)";
    }
}