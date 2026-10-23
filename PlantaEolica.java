package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaEolica extends PlantaRenovable {

    private int cantidadAerogeneradores;
    private double alturaTorres;
    private double velocidadVientoMinima;

    public PlantaEolica(String nombre, String direccion, double capacidadMaximaDeGeneracion,
                        LocalDateTime fechaPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
                        int cantidadAerogeneradores, double alturaTorres, double velocidadVientoMinima) {

        // Llamada al constructor de la clase padre (PlantaRenovable -> Planta)
        super(nombre, direccion, capacidadMaximaDeGeneracion, fechaPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio);

        //IREP
        if (cantidadAerogeneradores <= 0) {
            throw new RuntimeException("La cantidad de aerogeneradores debe ser mayor a 0");
        }
        if (alturaTorres <= 0 || !Double.isFinite(alturaTorres)) {
            throw new RuntimeException("La altura de las torres debe ser mayor a 0");
        }
        if (velocidadVientoMinima < 0 || !Double.isFinite(velocidadVientoMinima)) {
            throw new RuntimeException("La velocidad mínima del viento no puede ser negativa");
        }

        this.cantidadAerogeneradores = cantidadAerogeneradores;
        this.alturaTorres = alturaTorres;
        this.velocidadVientoMinima = velocidadVientoMinima;
    }

    public int getCantidadAerogeneradores() { return cantidadAerogeneradores; }
    public double getAlturaTorres() { return alturaTorres; }
    public double getVelocidadVientoMinima() { return velocidadVientoMinima; }

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
        return nombre + " (EOLICA)";
    }
}