package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaSolar extends PlantaRenovable {

    private double superficieCubierta;
    private int cantidadPaneles;
    private double horasRadiacionDiaria;

    public PlantaSolar(String nombre, String direccion, double capacidadMaximaDeGeneracion,
                       LocalDateTime fechaPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
                       double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
        
        // Llamada al constructor de la clase padre (PlantaRenovable -> Planta)
        super(nombre, direccion, capacidadMaximaDeGeneracion, fechaPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio);

        // IREP para Planta Solar
        if (superficieCubierta <= 0 || !Double.isFinite(superficieCubierta)) {
            throw new RuntimeException("La superficie cubierta debe ser mayor a 0");
        }
        if (cantidadPaneles <= 0) {
            throw new RuntimeException("La cantidad de paneles debe ser mayor a 0");
        }
        if (horasRadiacionDiaria <= 0 || horasRadiacionDiaria > 24 || !Double.isFinite(horasRadiacionDiaria)) {
            throw new RuntimeException("Las horas de radiación diaria deben ser mayores a 0 y hasta un máximo de 24");
        }

        this.superficieCubierta = superficieCubierta;
        this.cantidadPaneles = cantidadPaneles;
        this.horasRadiacionDiaria = horasRadiacionDiaria;
    }

    public double getSuperficieCubierta() {
        return superficieCubierta;
    }

    public int getCantidadPaneles() {
        return cantidadPaneles;
    }

    public double getHorasRadiacionDiaria() {
        return horasRadiacionDiaria;
    }

    @Override
    public double calcularCapacidadMinimaDeCuota() {
        // Toma en cuenta la radiación diaria (proporción sobre 24 hs) y la eficiencia.
        return capacidadMaximaDeGeneracion * eficienciaPromedio * (horasRadiacionDiaria / 24.0);
    }

    @Override
    public boolean cumplioCuota(Monitoreo monitoreo) {
        if (monitoreo == null) return false;
        return monitoreo.getEnergiaGenerada() >= calcularCapacidadMinimaDeCuota();
    }
    
    @Override
    public String toString() {
        // El TP exige implementar el toString de los TADs relacionados.
        return nombre + " (SOLAR)";
    }
}