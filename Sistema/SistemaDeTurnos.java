package Sistema;

import java.util.*;
import java.util.Map.Entry;

import Estructuras.*;

public class SistemaDeTurnos {

	private String nombreSistema;
	private Hashtable<Integer, Votante> votantes;
	private Hashtable<Integer, Votante> votantesQueVotaron;
	private LinkedList<Tupla<Integer, Integer>> turnosAsignados;
	private Conjunto<Mesa> mesas;

	/*
	 * Constructor del sistema de asignación de turnos. Si el parámetro nombre fuera
	 * null, debe generar una excepción.
	 */
	public SistemaDeTurnos(String nombreSistema) {
		if (nombreSistema == null) {
			throw new IllegalArgumentException("Se debe ingresar un nombre para el sistema");
		}
		this.nombreSistema = nombreSistema;
		votantes = new Hashtable<Integer, Votante>();
		votantesQueVotaron = new Hashtable<Integer, Votante>();
		turnosAsignados = new LinkedList<Tupla<Integer, Integer>>();
		mesas = new Conjunto<Mesa>();
	}

	/*
	 * Registrar a los votantes. Antes de asignar un turno el votante debe estar
	 * registrado en el sistema. Si no lo está, se genera una excepción. Si la edad
	 * es menor a 16, se genera una excepción
	 */
	public void registrarVotante(int dni, String nombre, int edad, boolean enfPrevia, boolean trabaja) {
		Votante nuevo = new Votante(dni, nombre, edad, enfPrevia, trabaja);
		if (nuevo.getEdad() < 16) {
			throw new RuntimeException("No se puede registrar a menores de 16 años como votantes");
		} else if (estaRegistrado(dni)) {
			throw new RuntimeException("Ya existe un votante registrado con el dni " + dni);
		} else {
			votantes.put(dni, nuevo);
		}
	}

	public boolean estaRegistrado(int dni) {
		return votantes.containsKey(dni);
	}

	/*
	 * Agregar una nueva mesa del tipo dado en el parámetro y asignar el presidente
	 * de cada una, el cual deberá estar entre los votantes registrados y sin turno
	 * asignado. - Devuelve el número de mesa creada. si el president es un votante
	 * que no está registrado debe generar una excepción si el tipo de mesa no es
	 * válido debe generar una excepción Los tipos válidos son: “Enf_Preex”,
	 * “Mayor65”, “General” y “Trabajador”
	 */
	public int agregarMesa(String tipoMesa, int dni) {
		Mesa nueva = null;
		if (tipoMesa.equals("Enf_Preex")) {
			nueva = new MesaEnfermos(tipoMesa, asignarPresidente(dni), mesas.cantElementos());
			Tupla<Integer, Integer> turnoDelPresidente = nueva.reservarTurno(votantes.get(dni));
			turnosAsignados.add(turnoDelPresidente);
		}
		if (tipoMesa.equals("Mayor65")) {
			nueva = new MesaMayores(tipoMesa, asignarPresidente(dni), mesas.cantElementos());
			Tupla<Integer, Integer> turnoDelPresidente = nueva.reservarTurno(votantes.get(dni));
			turnosAsignados.add(turnoDelPresidente);
		}
		if (tipoMesa.equals("General")) {
			nueva = new MesaGeneral(tipoMesa, asignarPresidente(dni), mesas.cantElementos());
			Tupla<Integer, Integer> turnoDelPresidente = nueva.reservarTurno(votantes.get(dni));
			turnosAsignados.add(turnoDelPresidente);
		}
		if (tipoMesa.equals("Trabajador")) {
			nueva = new MesaTrabajadores(tipoMesa, asignarPresidente(dni), mesas.cantElementos());
			Tupla<Integer, Integer> turnoDelPresidente = nueva.reservarTurno(votantes.get(dni));
			turnosAsignados.add(turnoDelPresidente);
		}
		if (!tipoMesa.equals("Enf_Preex") && !tipoMesa.equals("Mayor65") && !tipoMesa.equals("General")
				&& !tipoMesa.equals("Trabajador")) {
			throw new RuntimeException("El tipo de mesa no es válido");
		}
		mesas.agregarElemento(nueva);
		return nueva.getNumero();
	}

	public Votante asignarPresidente(int dni) {
		Votante presidente = null;
		if (estaRegistrado(dni) && consultaTurno(dni) == null) {
			presidente = votantes.get(dni);
		}
		if (!estaRegistrado(dni)) {
			throw new RuntimeException("El presidente no está registrado");
		}
		return presidente;
	}

	/*
	 * Asigna un turno a un votante determinado. - Si el DNI no pertenece a un
	 * votante registrado debe generar una excepción. - Si el votante ya tiene turno
	 * asignado se devuelve el turno como: Número de Mesa y Franja Horaria. - Si aún
	 * no tiene turno asignado se busca una franja horaria disponible en una mesa
	 * del tipo correspondiente al votante y se devuelve el turno asignado, como
	 * Número de Mesa y Franja Horaria. - Si no hay mesas con horarios disponibles
	 * no modifica nada y devuelve null. (Se supone que el turno permitirá conocer
	 * la mesa y la franja horaria asignada)
	 */
	public Tupla<Integer, Integer> asignarTurno(int dni) {
		Tupla<Integer, Integer> turno = consultaTurno(dni);
		if (!mesas.estaVacio()) {
			int i = 0;
			String mesaDelVotante = consultarTipoDeMesa(dni);
			Mesa m = (Mesa) mesas.dameElemento();
			while (consultaTurno(dni) == null && i != mesas.cantElementos()) {
				if (mesaDelVotante.equals("EnfYMayor")) {
					if (m.esDeEnfYMayor()) {
						if (m.tieneCuposDisponibles()) {
							turno = m.reservarTurno(votantes.get(dni));
							turnosAsignados.add(turno);
						}else {
							i++;
							m = (Mesa) mesas.dameElemento();
						}
					}else {
						i++;
						m = (Mesa) mesas.dameElemento();
					}
				} else {
					if (mismoTipoDeMesa(m, mesaDelVotante) && m.tieneCuposDisponibles()) {
						turno = m.reservarTurno(votantes.get(dni));
						turnosAsignados.add(turno);
					} else {
						m = (Mesa) mesas.dameElemento();
						i++;
					}
				}
			}
		}
		return turno;
	}
	
	private boolean mismoTipoDeMesa(Mesa m, String mesaDelVotante) {
		return (m.esDeEnfermos() && mesaDelVotante.equals("Enf_Preex")) ||
				(m.esDeMayores() && mesaDelVotante.equals("Mayor65")) ||
				(m.esDeTrabajadores() && mesaDelVotante.equals("Trabajador")) ||
				(m.esGeneral() && mesaDelVotante.equals("General"));
	}

	/*
	 * Asigna turnos automáticamente a los votantes sin turno. El sistema busca si
	 * hay alguna mesa y franja horaria factible en la que haya disponibilidad.
	 * Devuelve la cantidad de turnos que pudo asignar.
	 */
	public int asignarTurnos() {
		int turnos = 0;
		Set<Integer> votantesDni = votantes.keySet();
		Iterator<Integer> it = votantesDni.iterator();
		int dni;
		Tupla<Integer, Integer> turno;
		while (it.hasNext()) {
			dni = it.next();
			if (consultaTurno(dni) == null) {
				turno = asignarTurno(dni);
				if (turno != null) {
					turnos++;
				}
			}
		}
		return turnos;
	}

	/*
	 * Consulta el turno de un votante dado su DNI. Devuelve Mesa y franja horaria.
	 * - Si el DNI no pertenece a un votante genera una excepción. - Si el votante
	 * no tiene turno devuelve null.
	 */
	public Tupla<Integer, Integer> consultaTurno(int dni) {
		Votante v = votantes.get(dni);
		if (v == null) {
			throw new RuntimeException("El DNI no pertenece a un votante registrado");
		}
		return v.consultarTurno();
	}

	/*
	 * Hace efectivo el voto del votante determinado por su DNI. Si el DNI no está
	 * registrado entre los votantes debe generar una excepción Si ya había votado
	 * devuelve false. Si no, efectúa el voto y devuelve true.
	 */
	public Boolean votar(int dni) {
		Votante v = votantes.get(dni);
		Boolean b;
		if (v == null) {
			throw new RuntimeException("El DNI no pertenece a un votante registrado");
		} else {
			if (v.voto()) {
				b = false;
			} else {
				v.votar();
				votantesQueVotaron.put(dni, v);
				b = true;
			}
			return b;
		}

	}

	public String consultarTipoDeMesa(int dni) {
		Votante v = votantes.get(dni);
		String tipoMesa = "";
		if (v.consultarSiEsTrabajador()) {
			tipoMesa = "Trabajador";
		} else {
			if (v.consultarSiEsEnfermo()) {
				tipoMesa = "Enf_Preex";
				if (v.consultarSiEsMayor()) {
					tipoMesa = "EnfYMayor";
				}
			} else {
				if (v.consultarSiEsMayor()) {
					tipoMesa = "Mayor65";
				} else {
					tipoMesa = "General";
				}
			}
		}
		return tipoMesa;
	}

	/*
	 * Dado un número de mesa, devuelve una Map cuya clave es la franja horaria y el
	 * valor es una lista con los DNI de los votantes asignados a esa franja. Sin
	 * importar si se presentaron o no a votar. - Si el número de mesa no es válido
	 * genera una excepción. - Si no hay asignados devuelve null.
	 */

	public Map<Integer, List<Integer>> asignadosAMesa(int numMesa) {
		if (numMesa > mesas.cantElementos()) {
			throw new RuntimeException("El numero de mesa no es valido");
		}
		int i = 0;
		Mesa m = mesas.dameElemento();
		while (m.getNumero() != numMesa && i != mesas.cantElementos()) {
			m = mesas.dameElemento();
			i++;
		}
		return m.dameVotantesPorHorario();
	}

	/*
	 * Cantidad de votantes con Turno asignados al tipo de mesa que se pide. -
	 * Permite conocer cuántos votantes se asignaron hasta el momento a alguno de
	 * los tipos de mesa que componen el sistema de votación. - Si la clase de mesa
	 * solicitada no es válida debe generar una excepción
	 */
	public int votantesConTurno(String tipoMesa) {
		if (!tipoMesa.equals("Enf_Preex") && !tipoMesa.equals("Mayor65") && !tipoMesa.equals("General")
				&& !tipoMesa.equals("Trabajador")) {
			throw new RuntimeException("El tipo de mesa no es válido");
		}
		int i = 0;
		int cantVotantes = 0;
		Mesa m = null;
		while (i != mesas.cantElementos()) {
			m = (Mesa) mesas.dameElemento();
			if (mismoTipoDeMesa(m, tipoMesa)) {
				cantVotantes += m.getVotantesAsignados().size();
			}
			i++;
		}
		return cantVotantes;
	}

	/*
	 * Consultar la cantidad de votantes sin turno asignados a cada tipo de mesa.
	 * Devuelve una Lista de Tuplas donde se vincula el tipo de mesa con la cantidad
	 * de votantes sin turno que esperan ser asignados a ese tipo de mesa. La lista
	 * no puede tener 2 elementos para el mismo tipo de mesa.
	 */
	public List<Tupla<String, Integer>> sinTurnoSegunTipoMesa() {
		LinkedList<Tupla<String, Integer>> sinTurno = new LinkedList<Tupla<String, Integer>>();
		String mensaje = "Votantes sin turno en mesa de ";
		Tupla<String, Integer> enfermos = new Tupla<String, Integer>(mensaje + "enfermos: ",
				votantesSinTurnoEnMesaDeEnfermos());
		Tupla<String, Integer> trabajadores = new Tupla<String, Integer>(mensaje + "trabajadores: ",
				votantesSinTurnoEnMesaDeTrabajadores());
		Tupla<String, Integer> mayores = new Tupla<String, Integer>(mensaje + "mayores: ",
				votantesSinTurnoEnMesaDeMayores());
		Tupla<String, Integer> general = new Tupla<String, Integer>(mensaje + "general: ",
				votantesSinTurnoEnMesaGeneral());
		sinTurno.add(enfermos);
		sinTurno.add(trabajadores);
		sinTurno.add(mayores);
		sinTurno.add(general);
		return sinTurno;
	}

	public int votantesSinTurnoEnMesaDeEnfermos() {
		int cantVotantes = 0;
		Set<Integer> votantesDni = votantes.keySet();
		Iterator<Integer> it = votantesDni.iterator();
		while (it.hasNext()) {
			int dniDelVotante = it.next();
			if (consultarTipoDeMesa(dniDelVotante).equals("Enf_Preex")
					&& votantes.get(dniDelVotante).consultarTurno() == null) {
				cantVotantes++;
			}
		}
		return cantVotantes;
	}

	public int votantesSinTurnoEnMesaDeTrabajadores() {
		int cantVotantes = 0;
		Set<Integer> votantesDni = votantes.keySet();
		Iterator<Integer> it = votantesDni.iterator();
		while (it.hasNext()) {
			int dniDelVotante = it.next();
			if (consultarTipoDeMesa(dniDelVotante).equals("Trabajador")
					&& votantes.get(dniDelVotante).consultarTurno() == null) {
				cantVotantes++;
			}
		}
		return cantVotantes;
	}

	public int votantesSinTurnoEnMesaDeMayores() {
		int cantVotantes = 0;
		Set<Integer> votantesDni = votantes.keySet();
		Iterator<Integer> it = votantesDni.iterator();
		while (it.hasNext()) {
			int dniDelVotante = it.next();
			if (consultarTipoDeMesa(dniDelVotante).equals("Mayor65")
					&& votantes.get(dniDelVotante).consultarTurno() == null) {
				cantVotantes++;
			}
		}
		return cantVotantes;
	}

	public int votantesSinTurnoEnMesaGeneral() {
		int cantVotantes = 0;
		Set<Integer> votantesDni = votantes.keySet();
		Iterator<Integer> it = votantesDni.iterator();
		while (it.hasNext()) {
			int dniDelVotante = it.next();
			if (consultarTipoDeMesa(dniDelVotante).equals("General")
					&& votantes.get(dniDelVotante).consultarTurno() == null) {
				cantVotantes++;
			}
		}
		return cantVotantes;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Sistema de turnos para Votacion - UNGS - ").append(nombreSistema);
		s.append("\nVotantes en espera de un turno: \n");
		Collection<Votante> vots = votantes.values();
		for (Votante v : vots) {
			if (v.consultarTurno() == null)
				s.append("- ").append(v.toString()).append("\n");
		}
		s.append("Votantes con turno: \n");
		for (Votante v : vots) {
			if (v.consultarTurno() != null) {
				s.append(v.toString());
				if (v.voto()) {
					s.append(" Ya votó\n");
				} else {
					s.append(" Aún no votó\n");
				}
			}
		}
		s.append("Mesas:\n");
		int i = 0;
		while (i != mesas.cantElementos()) {
			s.append(mesas.dameElemento().toString()).append(" \n");
			i++;
		}
		return s.toString();
	}

}