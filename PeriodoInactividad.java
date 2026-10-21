package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PeriodoInactividad {

	private LocalDateTime inicio;
	private LocalDateTime fin;
	
	public PeriodoInactividad(LocalDateTime inicio){
		
		if(inicio == null) {
			throw new RuntimeException ("Fecha de inicio invalida");
		}
		
		this.inicio = inicio;
		this.fin = null;	
	}
	
	public void cerrar (LocalDateTime fechaHora) {
	
		if(fechaHora == null) {
			throw new RuntimeException ("Fecha de fin invalida");
		}
		
		if(fin != null) {			
			throw new RuntimeException ("El periodo ya esta cerrado");
		}
		
		if (fechaHora.isBefore(inicio)) {
			
			throw new RuntimeException ("La fecha de fin no puede ser anterior al inicio");
		}
		
		this.fin = fechaHora;
	}
	
	public boolean contiene(LocalDateTime fechaHora) {
		
		if(fechaHora == null) {
			throw new RuntimeException("Fecha invalida");
		}
		
		boolean despuesDelInicio = !fechaHora.isBefore(inicio);
		
		boolean antesDelFin = fin == null || fechaHora.isBefore(fin);
		
		return despuesDelInicio && antesDelFin;
		
	}

	public LocalDateTime getFin() {
		
		return fin;
	}
	
	
}
