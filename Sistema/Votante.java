package Sistema;

import java.util.*;

import Estructuras.Tupla;

public class Votante {

	private int dni;
	private String nombre;
	private int edad;
	private Tupla<Integer, Integer> turno;
	private Boolean certificadoVotacion;
	private Boolean certificadoTrabajo;
	private Boolean certificadoDiscapacidad;
	private Boolean certificadoPresidente;

	public Votante(int dni, String nombre, int edad, Boolean certificadoDiscapacidad, Boolean certificadoTrabajo) {

		this.dni = dni;
		this.nombre = nombre;
		this.edad = edad;
		this.turno = null;
		this.certificadoVotacion = false;
		this.certificadoTrabajo = certificadoTrabajo;
		this.certificadoDiscapacidad = certificadoDiscapacidad;
		this.certificadoPresidente = false;
	}

	public void votar() {
		certificadoVotacion = true;
	}

	public Boolean voto() {
		return certificadoVotacion;
	}

	public Boolean consultarSiEsPresidenteDeMesa() {
		return certificadoPresidente;
	}

	public Boolean consultarSiEsTrabajador() {
		return certificadoTrabajo;
	}

	public Boolean consultarSiEsEnfermo() {
		return certificadoDiscapacidad;
	}

	public Boolean consultarSiEsMayor() {
		return edad >= 65;
	}

	public Tupla<Integer, Integer> consultarTurno() {
		return turno;
	}

	public int getDni() {
		return dni;
	}

	public int getEdad() {
		return edad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setTurno(Tupla<Integer, Integer> turnoAsignado) {
		this.turno = turnoAsignado;
	}

	@Override
	public String toString() {
		if (turno != null)
			return "Votante [DNI: " + dni + ", nombre: " + nombre + ", edad: " + edad + ", turno = Mesa: "
					+ turno.getX() + ", Hora:" + turno.getY() + "]";
		else
			return "Votante [DNI: " + dni + ", nombre: " + nombre + ", edad: " + edad + "]";
	}
}