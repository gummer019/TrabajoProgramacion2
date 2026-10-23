package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SmartGrids implements ISmartGrids {
	
	private Map<String, Planta> plantas;
	
	private final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	public SmartGrids() {
		this.plantas = new HashMap<>();
	}
	
	private LocalDateTime parsearFechaPuestaEnFuncionamiento(String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr, FORMATO_FECHA);
        return fecha.atStartOfDay();
    }

    private LocalDateTime parsearFechaHora(String fechaHoraStr) {
        if (fechaHoraStr != null && fechaHoraStr.length() == 10) {
            return LocalDate.parse(fechaHoraStr, FORMATO_FECHA).atStartOfDay();
        }
        return LocalDateTime.parse(fechaHoraStr, FORMATO_FECHA_HORA);
    }

    private Planta obtenerPlantaOExcepcion(String nombre) {
        Planta p = this.plantas.get(nombre);
        if (p == null) {
            throw new RuntimeException("La planta " + nombre + " no existe.");
        }
        return p;
    }

    private void validarNombreNuevo(String nombre) {
        if (this.plantas.containsKey(nombre)) {
            throw new RuntimeException("Ya existe una planta registrada con el nombre: " + nombre);
        }
    }

    @Override
    public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaximaDeGeneracion,
                                     String fechaPuestaEnFuncionamiento, double superficieCubierta,
                                     int cantidadPaneles, double horasRadiacionDiaria) {
        validarNombreNuevo(nombre);
        LocalDateTime fechaPuesta = parsearFechaPuestaEnFuncionamiento(fechaPuestaEnFuncionamiento);
        PlantaSolar solar = new PlantaSolar(nombre, direccion, capacidadMaximaDeGeneracion, fechaPuesta,
                superficieCubierta, cantidadPaneles, horasRadiacionDiaria);
        this.plantas.put(nombre, solar);
    }

    @Override
    public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaximaDeGeneracion,
                                      String fechaPuestaEnFuncionamiento, int cantidadAerogeneradores,
                                      double alturaTorres, double velocidadMinimaViento) {
        validarNombreNuevo(nombre);
        LocalDateTime fechaPuesta = parsearFechaPuestaEnFuncionamiento(fechaPuestaEnFuncionamiento);
        PlantaEolica eolica = new PlantaEolica(nombre, direccion, capacidadMaximaDeGeneracion, fechaPuesta,
                cantidadAerogeneradores, alturaTorres, velocidadMinimaViento);
        this.plantas.put(nombre, eolica);
    }

    @Override
    public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaximaDeGeneracion,
                                          String fechaPuestaEnFuncionamiento, int cantidadTurbinas,
                                          double capacidadEmbalse) {
        validarNombreNuevo(nombre);
        LocalDateTime fechaPuesta = parsearFechaPuestaEnFuncionamiento(fechaPuestaEnFuncionamiento);
        PlantaHidraulica hidraulica = new PlantaHidraulica(nombre, direccion, capacidadMaximaDeGeneracion, fechaPuesta,
                cantidadTurbinas, capacidadEmbalse);
        this.plantas.put(nombre, hidraulica);
    }

    @Override
    public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaximaDeGeneracion,
                                       String fechaPuestaEnFuncionamiento, String tipoCombustible,
                                       double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {
        validarNombreNuevo(nombre);
        LocalDateTime fechaPuesta = parsearFechaPuestaEnFuncionamiento(fechaPuestaEnFuncionamiento);
        PlantaTermica termica = new PlantaTermica(nombre, direccion, capacidadMaximaDeGeneracion, fechaPuesta,
                tipoCombustible, objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible);
        this.plantas.put(nombre, termica);
    }

    
    @Override
    public void activarPlanta(String nombre, String fechaHora) {
        Planta p = obtenerPlantaOExcepcion(nombre);
        p.activar(parsearFechaHora(fechaHora));
    }

    @Override
    public void desactivarPlanta(String nombre, String fechaHora) {
        Planta p = obtenerPlantaOExcepcion(nombre);
        p.desactivar(parsearFechaHora(fechaHora));
    }

    @Override
    public boolean estaActiva(String nombre) {
        Planta p = this.plantas.get(nombre);
        return p != null && p.isActiva();
    }

    private void validarMonitoreo(Planta p, LocalDateTime fh) {
        if (!p.isActiva()) {
            throw new RuntimeException("No se puede registrar monitoreo en una planta inactiva.");
        }
        if (fh.isBefore(p.getFechaPuestaEnFuncionamiento())) {
            throw new RuntimeException("La fecha del monitoreo es anterior a la fecha de puesta en funcionamiento.");
        }
        for (Monitoreo m : p.getMonitoreos()) {
            if (m.getFechaHora().isEqual(fh)) {
                throw new RuntimeException("Ya existe un monitoreo registrado para esta fecha y hora.");
            }
        }
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
        Planta p = obtenerPlantaOExcepcion(nombre);
        if (p instanceof PlantaEolica || p instanceof PlantaTermica) {
            throw new RuntimeException("Tipo de monitoreo incorrecto para esta planta.");
        }
        LocalDateTime fh = parsearFechaHora(fechaHora);
        validarMonitoreo(p, fh);
        Monitoreo m = new Monitoreo(fh, energiaGenerada, incidencias) {};
        p.agregarMonitoreo(m);
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
        Planta p = obtenerPlantaOExcepcion(nombre);
        if (!(p instanceof PlantaEolica)) {
            throw new RuntimeException("Solo se pueden registrar monitoreos eólicos en plantas eólicas.");
        }
        LocalDateTime fh = parsearFechaHora(fechaHora);
        validarMonitoreo(p, fh);
        MonitoreoEolico m = new MonitoreoEolico(fh, energiaGenerada, incidencias, velocidadViento);
        p.agregarMonitoreo(m);
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
        Planta p = obtenerPlantaOExcepcion(nombre);
        if (!(p instanceof PlantaTermica)) {
            throw new RuntimeException("Solo se pueden registrar monitoreos térmicos en plantas térmicas.");
        }
        LocalDateTime fh = parsearFechaHora(fechaHora);
        validarMonitoreo(p, fh);
        MonitoreoTermico m = new MonitoreoTermico(fh, energiaGenerada, incidencias, co2Emitido, combustibleUtilizado);
        p.agregarMonitoreo(m);
    }


	@Override
	public List<String> consultarPlantas() {
		List<String> resultado = new ArrayList<>();
		for (Planta p : this.plantas.values()) {
			String estado = p.isActiva() ? "ACTIVA" : "INACTIVA";
			String fechaFormateada = p.getFechaPuestaEnFuncionamiento().format(FORMATO_FECHA_HORA);
			resultado.add(p.getNombre() + " (" + estado + "): " + fechaFormateada);
		}
		return resultado;
	}

	@Override
	public double capacidadMinimaCuota(String nombre) {
		Planta p = obtenerPlantaOExcepcion(nombre);
		return p.calcularCapacidadMinimaDeCuota();
	}

	@Override
	public double totalGenerado(String nombre) {
		Planta p = obtenerPlantaOExcepcion(nombre);
		double total = 0.0;
		for (Monitoreo m : p.getMonitoreos()) {
			total += m.getEnergiaGenerada();
		}
		return total;
	}

	@Override
	public List<String> plantasInactivasEn(String fechaHora) {
		LocalDateTime fh = parsearFechaHora(fechaHora);
		List<String> inactivas = new ArrayList<>();
		for (Planta p : this.plantas.values()) {
			if (p.estuvoInactivaEn(fh)) {
				inactivas.add(p.getNombre());
			}
		}
		return inactivas;
	}

	@Override
	public int limpiarMonitoreosSinDatos() {
		int borrados = 0;
		for (Planta p : this.plantas.values()) {
			Iterator<Monitoreo> it = p.getMonitoreos().iterator();
			while (it.hasNext()) {
				Monitoreo m = it.next();
				if (m.getEnergiaGenerada() == 0.0 && m.getIncidencias() == 0) {
					it.remove();
					borrados++;
				}
			}
		}
		return borrados;
	}


	@Override
	public int[] incidenciasPorHora(String nombre) {
		Planta p = obtenerPlantaOExcepcion(nombre);
		int[] resultado = new int[24];
		for (Monitoreo m : p.getMonitoreos()) {
			int hora = m.getFechaHora().getHour();
			resultado[hora] += m.getIncidencias();
		}
		return resultado;
	}

	@Override
	public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
		Planta p = obtenerPlantaOExcepcion(nombre);
		LocalDateTime inicio = parsearFechaHora(fechaDesde);
		LocalDateTime fin = parsearFechaHora(fechaHasta);

		if (inicio.isAfter(fin)) {
			throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha de fin.");
		}

		int[] histograma = new int[24];
		for (Monitoreo m : p.getMonitoreos()) {
			LocalDateTime fh = m.getFechaHora();
			if ((fh.isEqual(inicio) || fh.isAfter(inicio)) && (fh.isEqual(fin) || fh.isBefore(fin))) {
				if (p.cumplioCuota(m)) {
					histograma[fh.getHour()]++;
				}
			}
		}
		return histograma;
	}

	@Override
	public Map<String, Integer> plantasRenovablesPorFuente() {
		Map<String, Integer> conteo = new HashMap<>();
		for (Planta p : this.plantas.values()) {
			if (p instanceof PlantaRenovable) {
				PlantaRenovable pr = (PlantaRenovable) p;
				String fuente = pr.getFuentePrimaria();
				conteo.put(fuente, conteo.getOrDefault(fuente, 0) + 1);
			}
		}
		return conteo;
	}

	@Override
	public List<String> plantasQueNuncaCumplieronCuota() {
		List<String> resultado = new ArrayList<>();
		for (Planta p : this.plantas.values()) {
			boolean cumplioAlgunaVez = false;
			for (Monitoreo m : p.getMonitoreos()) {
				if (p.cumplioCuota(m)) {
					cumplioAlgunaVez = true;
					break;
				}
			}
			if (!cumplioAlgunaVez) {
				resultado.add(p.getNombre());
			}
		}
		return resultado;
	}

	@Override
	public String horasSinVelocidadMinimaPorPlantaEolica() {
		StringBuilder sb = new StringBuilder();
		sb.append("Plantas Eolicas: Horas con poco viento\n");

		for (Planta p : this.plantas.values()) {
			if (p instanceof PlantaEolica) {
				PlantaEolica pe = (PlantaEolica) p;
				sb.append(pe.getNombre()).append(": ").append(pe.getVelocidadMinimaViento()).append(" m/s\n");

				for (Monitoreo m : pe.getMonitoreos()) {
					if (m instanceof MonitoreoEolico) {
						MonitoreoEolico me = (MonitoreoEolico) m;
						if (me.getVelocidadViento() < pe.getVelocidadMinimaViento()) {
							String horaStr = String.format("%02d", me.getFechaHora().getHour());
							String velStr = String.format("%.0f", me.getVelocidadViento());
							sb.append(horaStr).append(": ").append(velStr).append(" m/s\n");
						}
					}
				}
			}
		}
		return sb.toString();
	}

	@Override
	public List<String> plantasOrdenadasPorEnergiaGenerada() {
		List<Planta> lista = new ArrayList<>(this.plantas.values());
		lista.sort((p1, p2) -> Double.compare(totalGenerado(p2.getNombre()), totalGenerado(p1.getNombre())));

		List<String> resultado = new ArrayList<>();
		for (Planta p : lista) {
			resultado.add(p.getNombre() + ": " + totalGenerado(p.getNombre()));
		}
		return resultado;
	}
}