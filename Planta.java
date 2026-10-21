package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Planta {

	protected String nombre;
	protected String direccion;
	protected double capacidadMaximaDeGeneracion;
	protected LocalDateTime fechaPuestaEnFuncionamiento;
	protected boolean activa;
	
	private LocalDateTime fechaUltimoCambioDeEstado;
	
	
	private List<PeriodoInactividad> periodosDeInactividad;
	private List<Monitoreo> monitoreos;
	
	public Planta( String nombre, String direccion, double capacidadMaximaDeGeneracion, LocalDateTime fechaPuestaEnFuncionamiento) {
			    
	if (nombre == null || nombre.trim().isEmpty()) {
		 throw new RuntimeException ("Nombre invalido");
	}
		
	if(direccion == null || direccion.trim().isEmpty()) {
		throw new RuntimeException ("Direccion invalida");
		
	}
	
    if (capacidadMaximaDeGeneracion <= 0 || !Double.isFinite(capacidadMaximaDeGeneracion)) {
		throw new RuntimeException("Capacidad maxima invalida");
}
    
	if(fechaPuestaEnFuncionamiento == null) {
		throw new RuntimeException("Fecha de puesta en funcionamiento invalida");

	}
	this.nombre = nombre;
    this.direccion = direccion;
    this.capacidadMaximaDeGeneracion = capacidadMaximaDeGeneracion;
    this.fechaPuestaEnFuncionamiento = fechaPuestaEnFuncionamiento;
    this.activa = true;
    
    this.fechaUltimoCambioDeEstado = fechaPuestaEnFuncionamiento;
    
    this.periodosDeInactividad = new ArrayList<>();
	this.monitoreos = new ArrayList <>();	
}
	
	public abstract double calcularCapacidadMinimaDeCuota();
	
	public abstract boolean cumplioCuota(Monitoreo monitoreo);
	
	public String getNombre() {
		return nombre;
	}
	
	public String getDireccion() {
		return direccion;
	}
	
	public double getCapacidadMaximaDeGeneracion() {
	return capacidadMaximaDeGeneracion;
}
	
	public LocalDateTime getFechaPuestaEnFuncionamiento() {
		return fechaPuestaEnFuncionamiento;
		
	}
	
	public boolean estaActiva() {
		return activa;
	}
	
	public List<Monitoreo> getMonitoreos(){
		return new ArrayList<>(monitoreos);
	}
	
	public void agregarMonitoreo (Monitoreo monitoreo) {
		if(monitoreo == null) {
			throw new RuntimeException("Monitoreo invalido");
		}
		
		monitoreos.add(monitoreo);
	}
	
	public void desactivar(LocalDateTime fechaHora) {
		
		validarFechaDeCambioDeEstado(fechaHora);
		
		if(!activa) {
			throw new RuntimeException("La planta ya esta inactiva");
		}
		
		validarOrdenCronologico(fechaHora);
		
		periodosDeInactividad.add(new PeriodoInactividad (fechaHora));
		this.activa = false;
		this.fechaUltimoCambioDeEstado = fechaHora;
	}
	
	
	public void activar( LocalDateTime fechaHora) {
		
		validarFechaDeCambioDeEstado(fechaHora);
		
		if(activa) {
			throw new RuntimeException("La planta ya esta activa");
		}
		
		validarOrdenCronologico(fechaHora);
		
		PeriodoInactividad periodo = periodosDeInactividad.get(periodosDeInactividad.size() - 1);
		
		periodo.cerrar(fechaHora);
		this.activa = true;	
		this.fechaUltimoCambioDeEstado = fechaHora;
	}
	
	private void validarFechaDeCambioDeEstado (LocalDateTime fechaHora) {
		
		if(fechaHora == null) {
			throw new RuntimeException ("Fecha invalida");
		}
		
		if(fechaHora.isBefore(fechaPuestaEnFuncionamiento)) {
			
			throw new RuntimeException ("Fecha anterior a la puesta en funcionamiento");
		}
		
	}
	
	private void validarOrdenCronologico(LocalDateTime fechaHora) {
		if (fechaHora.isBefore(fechaUltimoCambioDeEstado)) {
            throw new RuntimeException("La fecha no puede ser anterior al ultimo cambio de estado");
		}
	}
	
	public boolean estuvoInactivaEn(LocalDateTime fechaHora) {
		
		if (fechaHora == null) {
			throw new RuntimeException("Fecha invalida");
		}
		
			
		for( PeriodoInactividad periodo : periodosDeInactividad) {
			
			if(periodo.contiene (fechaHora)) {
				return true;
			}
		
		}
		return false;
	}
	
}